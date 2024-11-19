package dev.enjarai.arcane_repository.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import dev.enjarai.arcane_repository.ArcaneRepository;
import dev.enjarai.arcane_repository.block.entity.MysticalLecternBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.LecternBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

@Environment(value = EnvType.CLIENT)
public class MysticalLecternBlockEntityRenderer implements BlockEntityRenderer<MysticalLecternBlockEntity> {
    public static final SpriteIdentifier BOOK_TEXTURE =
            new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE,
                    ArcaneRepository.id("entity/index_lectern_book"));
    private static final double ITEMS_RADIUS = 0.4;

    private final BookModel book;

    public MysticalLecternBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.book = new BookModel(ctx.getLayerModelPart(EntityModelLayers.BOOK));
    }

    @Override
    public void render(MysticalLecternBlockEntity be, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BlockState blockState = be.getCachedState();
        if (!blockState.get(LecternBlock.HAS_BOOK)) {
            return;
        }

        float anim = be.tick + tickDelta;
        float rotationDelta = (be.bookRotationTarget - be.bookRotation) * 0.1f;
        float bookRotation = be.bookRotation + rotationDelta * tickDelta;

        matrices.push();
        double bookHeightOffset = 0.06 + Math.sin(anim * 0.08) * 0.03;
        matrices.translate(0.5, 1.0625 + bookHeightOffset, 0.5);
        var facing = blockState.get(LecternBlock.FACING);
        float g = facing.rotateYClockwise().asRotation();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-g));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotation(-bookRotation));

        var itemCount = be.items.size();
        for (int i = 0; i < itemCount; i++) {
            matrices.push();

            var itemStack = be.items.get(i);
            var offset = (2 * Math.PI) / itemCount * i;
            var animationPos = offset + anim / 20;

            matrices.translate(0.2, 0.18, 0);
            var itemX = ITEMS_RADIUS * Math.cos(animationPos);
            var itemZ = ITEMS_RADIUS * Math.sin(animationPos);
            matrices.translate(itemX, itemX * -0.35, itemZ);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotation(anim * 0.05f));
            matrices.scale(0.75f, 0.75f, 0.75f);
            MinecraftClient.getInstance().getItemRenderer()
                    .renderItem(itemStack, ModelTransformationMode.GROUND, light, overlay, matrices, vertexConsumers, be.getWorld(), 0);

            matrices.pop();
        }

        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(67.5f));
        matrices.translate(0.0, -0.125, 0.0);
        this.book.setPageAngles(0.0f, 0.1f, 0.9f, 1.2f);
        VertexConsumer vertexConsumer = BOOK_TEXTURE.getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid);
        this.book.renderBook(matrices, vertexConsumer, light, overlay, 0xffffff);

        matrices.pop();
    }
}
