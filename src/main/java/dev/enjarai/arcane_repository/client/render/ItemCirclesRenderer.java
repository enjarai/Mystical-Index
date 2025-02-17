package dev.enjarai.arcane_repository.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.enjarai.arcane_repository.ArcaneRepository;
import dev.enjarai.arcane_repository.util.BigStack;
import dev.enjarai.arcane_repository.util.MathUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.List;
import java.util.Map;

public class ItemCirclesRenderer {
    private static final Quaternionf ITEM_LIGHT_ROTATION_3D = RotationAxis.POSITIVE_X.rotationDegrees(-15).mul(RotationAxis.POSITIVE_Y.rotationDegrees(15));
    private static final Quaternionf ITEM_LIGHT_ROTATION_FLAT = RotationAxis.POSITIVE_X.rotationDegrees(-45);
    public static final Identifier CIRCLES_TEXTURE = ArcaneRepository.id("textures/gui/circles.png");
    public static final int CIRCLE_TEXTURES_SIZE = 256;
    public static final Map<Integer, Identifier> CIRCLE_TEXTURES = Map.of(
            24, ArcaneRepository.id("textures/gui/circle_24.png"),
            48, ArcaneRepository.id("textures/gui/circle_48.png"),
            72, ArcaneRepository.id("textures/gui/circle_72.png")
    );
    public static final int SECONDARY_CIRCLE_ITEM_COUNT = 1 + 6;
    public static final int TERNARY_CIRCLE_ITEM_COUNT = 1 + 6 + 12;
    public static final int QUATERNARY_CIRCLE_ITEM_COUNT = 1 + 6 + 12 + 18;

    private final boolean inWorld;

    public ItemCirclesRenderer(boolean inWorld) {
        this.inWorld = inWorld;
    }

    public void render(MatrixStack matrices, double x, double y, double z, int light, float progress, List<BigStack> stacks) {
        matrices.push();
        matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(180));

        var drawContext = new DrawContext(
                MinecraftClient.getInstance(), matrices,
                MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers()
        );
        render(drawContext, x, y, z, light, progress, stacks);

        matrices.pop();
    }

    public void render(DrawContext context, double x, double y, List<BigStack> stacks) {
        render(context, x, y, 0, 0xF000F0, 1, stacks);
    }

    public void render(DrawContext context, double x, double y, double z, int light, float progress, List<BigStack> stacks) {
        if (stacks.isEmpty()) return;

        var matrices = context.getMatrices();
        matrices.push();

        var primary = stacks.get(0);
        var secondary = stacks.size() > 1 ?
                stacks.subList(1, Math.min(SECONDARY_CIRCLE_ITEM_COUNT, stacks.size())) : null;
        var ternary = stacks.size() > SECONDARY_CIRCLE_ITEM_COUNT ?
                stacks.subList(SECONDARY_CIRCLE_ITEM_COUNT, Math.min(TERNARY_CIRCLE_ITEM_COUNT, stacks.size())) : null;
        var quaternary = stacks.size() > TERNARY_CIRCLE_ITEM_COUNT ?
                stacks.subList(TERNARY_CIRCLE_ITEM_COUNT, Math.min(QUATERNARY_CIRCLE_ITEM_COUNT, stacks.size())) : null;

        if (secondary != null) drawItemCircle(context, x, y, z, light, progress, 24, secondary);
        if (ternary != null) drawItemCircle(context, x, y, z, light, progress, 48, ternary);
        if (quaternary != null) drawItemCircle(context, x, y, z, light, progress, 72, quaternary);

        var scale = calculateRingScale(0, progress);
        matrices.scale(scale, scale, scale);
        drawItemCircle(context, x, y, z, true);
        drawItemStack(context, x, y, z, light, primary);

        matrices.pop();
    }

    private void drawItemCircle(DrawContext context, double x, double y, double z, int light, float progress, int radius, List<BigStack> items) {
        var itemCount = items.size();
        var circleTexture = CIRCLE_TEXTURES.get(radius);
        var matrices = context.getMatrices();
        var scale = calculateRingScale(radius, progress);

        matrices.push();
        matrices.scale(scale, scale, scale);

        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();

        context.drawTexture(
                circleTexture,
                (int) x - CIRCLE_TEXTURES_SIZE / 2, (int) y - CIRCLE_TEXTURES_SIZE / 2,
                0, 0, CIRCLE_TEXTURES_SIZE, CIRCLE_TEXTURES_SIZE
        );

        for (int i = 0; i < itemCount; i++) {
            var stack = items.get(i);
            var offset = (2 * Math.PI) / itemCount * i - (Math.PI / 2);

            var itemX = (int) x + (radius * Math.cos(offset));
            var itemY = (int) y + (radius * Math.sin(offset));

            drawItemCircle(context, itemX, itemY, z, false);
            drawItemStack(context, itemX, itemY, z, light, stack);
        }

        matrices.pop();
    }

    private float calculateRingScale(int radius, float progress) {
        return inverseCubeEasing(128f / (radius + 24) * progress);
    }

    private float inverseCubeEasing(float in) {
        return (float) (1 - Math.pow(1 - MathHelper.clamp(in, 0, 1), 3));
    }

    protected void drawItemCircle(DrawContext context, double x, double y, double z, boolean isPrimary) {
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();

        var matrices = context.getMatrices();

        matrices.push();
        matrices.translate(0, 0, z);
        context.drawTexture(CIRCLES_TEXTURE, (int) x - 12, (int) y - 12, isPrimary ? 24 : 0, 0, 24, 24);
        matrices.pop();
    }

    protected void drawItemStack(DrawContext context, double x, double y, double z, int light, BigStack stack) {
        var count = stack.getAmount();
        var matrices = context.getMatrices();

        if (inWorld) {
            matrices.push();
            matrices.translate(0, 0, -1);
            matrices.multiplyPositionMatrix(new Matrix4f().scale(1, 1, 0.01f));

            drawItem(matrices, context.getVertexConsumers(), stack.getItemStack(), (int) x, (int) y, light);
            matrices.pop();
        } else {
            context.drawItem(stack.getItemStack(), (int) x - 8, (int) y - 8);

            context.drawItemInSlot(MinecraftClient.getInstance().textRenderer, stack.getItemStack(), (int) x - 8, (int) y - 8,
                    count > 1 ? MathUtil.shortNumberFormat(count) : "");
        }
    }

    private void drawItem(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, int x, int y, int light) {
        if (!stack.isEmpty()) {
            var itemRenderer = MinecraftClient.getInstance().getItemRenderer();
            var bakedModel = itemRenderer.getModel(stack, null, null, 0);
            matrices.push();
            matrices.translate(x, y, inWorld ? 0 : 150);
            if (inWorld) {
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
            }

            try {
                if (!inWorld) {
                    matrices.multiplyPositionMatrix(new Matrix4f().scaling(1.0F, -1.0F, 1.0F));
                }
                matrices.scale(16.0F, 16.0F, 16.0F);

                boolean sideLit = bakedModel.isSideLit();
                if (!inWorld) {
                    if (!sideLit) DiffuseLighting.disableGuiDepthLighting();
                } else {
                    // Stolen from Extended Drawers, who in turn stole it from Storage Drawers
                    if (sideLit) {
                        matrices.peek().getNormalMatrix().mul(new Matrix3f().set(ITEM_LIGHT_ROTATION_3D));
                    } else {
                        matrices.peek().getNormalMatrix().mul(new Matrix3f().set(ITEM_LIGHT_ROTATION_FLAT));
                    }
                }
                RenderSystem.disableDepthTest();

                itemRenderer.renderItem(stack, ModelTransformationMode.GUI, false, matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV, bakedModel);

                RenderSystem.enableDepthTest();
                if (!inWorld) {
                    if (!sideLit) DiffuseLighting.enableGuiDepthLighting();
                }
            } catch (Throwable e) {
                CrashReport crashReport = CrashReport.create(e, "Rendering item");
                CrashReportSection crashReportSection = crashReport.addElement("Item being rendered");
                crashReportSection.add("Item Type", () -> String.valueOf(stack.getItem()));
                crashReportSection.add("Item Damage", () -> String.valueOf(stack.getDamage()));
                crashReportSection.add("Item Components", () -> String.valueOf(stack.getComponents()));
                crashReportSection.add("Item Foil", () -> String.valueOf(stack.hasGlint()));
                throw new CrashException(crashReport);
            }

            matrices.pop();
        }
    }
}
