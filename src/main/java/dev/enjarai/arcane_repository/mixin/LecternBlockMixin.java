package dev.enjarai.arcane_repository.mixin;

import dev.enjarai.arcane_repository.block.ModBlocks;
import dev.enjarai.arcane_repository.block.custom.MysticalLecternBlock;
import dev.enjarai.arcane_repository.item.ModItems;
import dev.enjarai.arcane_repository.item.custom.book.MysticalBookItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LecternBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LecternBlock.class)
public class LecternBlockMixin {
    @Inject(
            method = "onUseWithItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/LecternBlock;putBookIfAbsent(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/item/ItemStack;)Z"
            ),
            cancellable = true
    )
    private void tryConvertToMysticalLectern(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ItemActionResult> cir) {
        if (stack.getItem() instanceof MysticalBookItem) {
            var newState = ModBlocks.MYSTICAL_LECTERN.getStateWithProperties(state);

            world.setBlockState(pos, newState);
            ItemStack moved = stack.copy();
            stack.decrement(1);

            cir.setReturnValue(MysticalLecternBlock.putBookIfAbsent(
                    player, world,
                    pos, newState,
                    moved
            ) ? ItemActionResult.success(world.isClient) : ItemActionResult.SKIP_DEFAULT_BLOCK_INTERACTION);
        }
    }
}
