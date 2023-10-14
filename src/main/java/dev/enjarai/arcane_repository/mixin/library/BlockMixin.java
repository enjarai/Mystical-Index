package dev.enjarai.arcane_repository.mixin.library;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin extends AbstractBlock {
    public BlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(
            method = "onPlaced",
            at = @At("HEAD")
    )
    protected void onPlacedHead(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
    }

    @Inject(
            method = "afterBreak",
            at = @At("HEAD")
    )
    protected void afterBreakHead(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack tool, CallbackInfo ci) {
    }
}
