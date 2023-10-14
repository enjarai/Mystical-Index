package net.messer.mystical_index.client.render;

import net.messer.mystical_index.item.custom.book.MysticalBookItem;
import net.messer.mystical_index.item.custom.page.type.ItemStorageTypePage;
import net.messer.mystical_index.mixin.accessor.ChiseledBookshelfBlockInvoker;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.RotationAxis;

public class ChiseledBookshelfBlockEntityRenderer implements BlockEntityRenderer<ChiseledBookshelfBlockEntity> {
    private final ItemCirclesRenderer circleRenderer = new ItemCirclesRenderer(true);

    public ChiseledBookshelfBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(ChiseledBookshelfBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        var hitResult = MinecraftClient.getInstance().crosshairTarget;

        if (hitResult instanceof BlockHitResult blockHitResult && entity.getPos().equals(blockHitResult.getBlockPos())) {

            var facing = entity.getCachedState().get(HorizontalFacingBlock.FACING);
            ChiseledBookshelfBlockInvoker.invokeGetHitPos(blockHitResult, facing).ifPresent(hitPos -> {

                var slot = ChiseledBookshelfBlockInvoker.invokeGetSlotForHitPos(hitPos);
                var bookStack = entity.getStack(slot);
                var book = bookStack.getItem();

                if (book instanceof MysticalBookItem bookItem && bookItem.getTypePage(bookStack) instanceof ItemStorageTypePage storagePage) {
                    var stacks = storagePage.getContents(bookStack).getAll();
                    var x = (slot % 3 / 3f + 1f / 6f) / 16f * 15f + 1f / 32f;
                    var y = slot / 3 / 2f + 1f / 4f;

                    matrices.push();

                    matrices.translate(1.0f - x, 1.0f - y, 0f);
//                    matrices.translate(0.5f, 0f, 0.5f);
//                    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180 + facing.asRotation()));
//                    matrices.translate(-0.5f, 0f, -0.5f);
                    matrices.scale(0.01f, 0.01f, 0.01f);
                    circleRenderer.render(matrices, 0, 0, 0, 0xF000F0, stacks);

                    matrices.pop();
                }
            });
        }
    }
}
