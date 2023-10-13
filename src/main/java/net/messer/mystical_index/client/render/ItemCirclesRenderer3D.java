package net.messer.mystical_index.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.messer.mystical_index.MysticalIndex;
import net.messer.mystical_index.util.BigStack;
import net.messer.mystical_index.util.MathUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

import java.util.List;
import java.util.Map;

public class ItemCirclesRenderer3D {

    public static final Identifier CIRCLES_TEXTURE = MysticalIndex.id("textures/gui/circles.png");
    public static final int CIRCLE_TEXTURES_SIZE = 256;
    public static final Map<Integer, Identifier> CIRCLE_TEXTURES = Map.of(
            24, MysticalIndex.id("textures/gui/circle_24.png"),
            48, MysticalIndex.id("textures/gui/circle_48.png")
    );
    public static final int SECONDARY_CIRCLE_ITEM_COUNT = 7;
    public static final int TERNARY_CIRCLE_ITEM_COUNT = 19;

    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, double x, double y, double z, List<BigStack> stacks) {
        if (stacks.isEmpty()) return;

        var primary = stacks.get(0);
        var secondary = stacks.size() > 1 ?
                stacks.subList(1, Math.min(SECONDARY_CIRCLE_ITEM_COUNT, stacks.size())) : null;
        var ternary = stacks.size() > SECONDARY_CIRCLE_ITEM_COUNT ?
                stacks.subList(SECONDARY_CIRCLE_ITEM_COUNT, Math.min(TERNARY_CIRCLE_ITEM_COUNT, stacks.size())) : null;

        if (secondary != null) drawItemCircle(matrices, vertexConsumers, x, y, z, 24, secondary);
        if (ternary != null) drawItemCircle(matrices, vertexConsumers, x, y, z, 48, ternary);
        drawItemCircle(matrices, vertexConsumers, x, y, z, true);
        drawItem(matrices, vertexConsumers, x, y, z, primary);
    }

    private void drawItemCircle(MatrixStack matrices, VertexConsumerProvider vertexConsumers, double x, double y, double z, int radius, List<BigStack> items) {
        var itemCount = items.size();
        var circleTexture = CIRCLE_TEXTURES.get(radius);

        drawTexturedQuad(matrices, circleTexture, 0, 1, 0, 1);

//        var positionMatrix = matrices.peek().getPositionMatrix();
//        var tessellator = Tessellator.getInstance();
//        var buffer = tessellator.getBuffer();
//
//        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
//        buffer.vertex(positionMatrix, 20, 20, 0).color(1, 1, 1, 1).texture(0, 0).next();
//        buffer.vertex(positionMatrix, 20, 60, 0).color(1, 0, 0, 1).texture(0, 1).next();
//        buffer.vertex(positionMatrix, 60, 60, 0).color(0, 1, 0, 1).texture(1, 1).next();
//        buffer.vertex(positionMatrix, 60, 20, 0).color(0, 0, 1, 1).texture(1, 0).next();
//
//        RenderSystem.disableDepthTest();
//        RenderSystem.enableBlend();
//        RenderSystem.setShaderTexture(0, circleTexture);
//
//        tessellator.draw();

        //============

//        context.drawTexture(
//                ,
//                (int) x - CIRCLE_TEXTURES_SIZE / 2, (int) y - CIRCLE_TEXTURES_SIZE / 2,
//                0, 0, CIRCLE_TEXTURES_SIZE, CIRCLE_TEXTURES_SIZE
//        );

        for (int i = 0; i < itemCount; i++) {
            var stack = items.get(i);
            var offset = (2 * Math.PI) / itemCount * i - (Math.PI / 2);

            var itemX = (int) x + (radius * Math.cos(offset));
            var itemY = (int) y + (radius * Math.sin(offset));

            drawItemCircle(matrices, vertexConsumers, itemX, itemY, z, false);
            drawItem(matrices, vertexConsumers, itemX, itemY, z, stack);
        }
    }

    protected void drawItemCircle(MatrixStack matrices, VertexConsumerProvider vertexConsumers, double x, double y, double z, boolean isPrimary) {
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();

        matrices.push();
        matrices.translate(0, 0, z);
//        context.drawTexture(CIRCLES_TEXTURE, (int) x - 12, (int) y - 12, isPrimary ? 24 : 0, 0, 24, 24);
        matrices.pop();
    }

    protected void drawItem(MatrixStack matrices, VertexConsumerProvider vertexConsumers, double x, double y, double z, BigStack stack) {
        var count = stack.getAmount();
        var itemRenderer = MinecraftClient.getInstance().getItemRenderer();

//        itemRenderer.renderItem(stack.getItemStack());
//
//        context.drawItem(stack.getItemStack(), (int) x - 8, (int) y - 8);
//        context.drawItemInSlot(MinecraftClient.getInstance().textRenderer, stack.getItemStack(), (int) x - 8, (int) y - 8,
//                count > 1 ? MathUtil.shortNumberFormat(count) : "");
    }

    private static void drawTexturedQuad(MatrixStack matrices, Identifier texture, float u1, float u2, float v1, float v2) {
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix4f, -4, -4, 0).texture(u1, v1).next();
        bufferBuilder.vertex(matrix4f, -4, 4, 0).texture(u1, v2).next();
        bufferBuilder.vertex(matrix4f, 4, 4, 0).texture(u2, v2).next();
        bufferBuilder.vertex(matrix4f, 4, -4, 0).texture(u2, v1).next();
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }
}
