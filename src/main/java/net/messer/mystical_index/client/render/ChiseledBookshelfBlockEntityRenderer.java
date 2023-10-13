package net.messer.mystical_index.client.render;

import net.messer.mystical_index.util.BigStack;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

import java.util.List;

public class ChiseledBookshelfBlockEntityRenderer implements BlockEntityRenderer<ChiseledBookshelfBlockEntity> {
    private final ItemCirclesRenderer circleRenderer = new ItemCirclesRenderer(true);

    public ChiseledBookshelfBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(ChiseledBookshelfBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.scale(-0.01f, -0.01f, -0.01f);
        circleRenderer.render(matrices, 0, 0, 0, List.of(
                new BigStack(entity.getStack(0)),
                new BigStack(entity.getStack(1))
        ));


//        var size = 4;
//        var matrix = matrices.peek().getPositionMatrix();
//        var color = 0xffffffff;
//        var vertices = vertexConsumers.getBuffer(RenderLayer.getText(MysticalIndex.id("textures/gui/circle_24.png")));
//        vertices.vertex(matrix, -size, -size, 0).color(color).texture(0, 0).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).next();
//        vertices.vertex(matrix, -size, size, 0).color(color).texture(0, 1).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).next();
//        vertices.vertex(matrix, size, size, 0).color(color).texture(1, 1).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).next();
//        vertices.vertex(matrix, size, -size, 0).color(color).texture(1, 0).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).next();
    }
}
