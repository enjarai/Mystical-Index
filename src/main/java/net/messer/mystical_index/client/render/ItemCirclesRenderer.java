package net.messer.mystical_index.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.messer.mystical_index.MysticalIndex;
import net.messer.mystical_index.util.BigStack;
import net.messer.mystical_index.util.MathUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

public class ItemCirclesRenderer {
    public static final Identifier CIRCLES_TEXTURE = MysticalIndex.id("textures/gui/circles.png");
    public static final int CIRCLE_TEXTURES_SIZE = 256;
    public static final Map<Integer, Identifier> CIRCLE_TEXTURES = Map.of(
            24, MysticalIndex.id("textures/gui/circle_24.png"),
            48, MysticalIndex.id("textures/gui/circle_48.png")
    );
    public static final int SECONDARY_CIRCLE_ITEM_COUNT = 7;
    public static final int TERNARY_CIRCLE_ITEM_COUNT = 19;

    private final boolean inWorld;

    public ItemCirclesRenderer(boolean inWorld) {
        this.inWorld = inWorld;
    }

    public void render(MatrixStack matrices, double x, double y, double z, List<BigStack> stacks) {
        var drawContext = new DrawContext(
                MinecraftClient.getInstance(),
                matrices,
                VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer())
        );
        render(drawContext, x, y, z, stacks);
    }

    public void render(DrawContext context, double x, double y, List<BigStack> stacks) {
        render(context, x, y, 0, stacks);
    }

    public void render(DrawContext context, double x, double y, double z, List<BigStack> stacks) {
        if (stacks.isEmpty()) return;

        var primary = stacks.get(0);
        var secondary = stacks.size() > 1 ?
                stacks.subList(1, Math.min(SECONDARY_CIRCLE_ITEM_COUNT, stacks.size())) : null;
        var ternary = stacks.size() > SECONDARY_CIRCLE_ITEM_COUNT ?
                stacks.subList(SECONDARY_CIRCLE_ITEM_COUNT, Math.min(TERNARY_CIRCLE_ITEM_COUNT, stacks.size())) : null;

        if (secondary != null) drawItemCircle(context, x, y, z, 24, secondary);
        if (ternary != null) drawItemCircle(context, x, y, z, 48, ternary);
        drawItemCircle(context, x, y, z, true);
        drawItem(context, x, y, z, primary);
    }

    private void drawItemCircle(DrawContext context, double x, double y, double z, int radius, List<BigStack> items) {
        var itemCount = items.size();
        var circleTexture = CIRCLE_TEXTURES.get(radius);

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
            drawItem(context, itemX, itemY, z, stack);
        }
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

    protected void drawItem(DrawContext context, double x, double y, double z, BigStack stack) {
        var count = stack.getAmount();
        var matrices = context.getMatrices();

        matrices.push();

        if (inWorld) matrices.translate(0, 0, -150);
        context.drawItem(stack.getItemStack(), (int) x - 8, (int) y - 8);

        if (inWorld) matrices.translate(0, 0, -48);
        context.drawItemInSlot(MinecraftClient.getInstance().textRenderer, stack.getItemStack(), (int) x - 8, (int) y - 8,
                count > 1 ? MathUtil.shortNumberFormat(count) : "0");

        matrices.pop();
    }
}
