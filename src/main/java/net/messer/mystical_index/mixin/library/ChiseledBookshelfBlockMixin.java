package net.messer.mystical_index.mixin.library;

import net.messer.mystical_index.util.LecternTracker;
import net.messer.mystical_index.util.request.IndexInteractable;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChiseledBookshelfBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChiseledBookshelfBlock.class)
public abstract class ChiseledBookshelfBlockMixin extends BlockWithEntityMixin {
    public ChiseledBookshelfBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    protected void onPlacedHead(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
        if (!world.isClient() && world.getBlockEntity(pos) instanceof IndexInteractable library) {
            LecternTracker.tryRegisterToLectern(library);
        }
    }

    @Override
    protected void afterBreakHead(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack tool, CallbackInfo ci) {
        if (!world.isClient() && world.getBlockEntity(pos) instanceof IndexInteractable library) {
            LecternTracker.tryRegisterToLectern(library);
        }
    }
}
