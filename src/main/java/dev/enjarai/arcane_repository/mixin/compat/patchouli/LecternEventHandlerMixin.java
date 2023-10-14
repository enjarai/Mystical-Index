package dev.enjarai.arcane_repository.mixin.compat.patchouli;

import dev.enjarai.arcane_repository.block.ModBlocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.patchouli.common.handler.LecternEventHandler;

@Mixin(LecternEventHandler.class)
public abstract class LecternEventHandlerMixin {
    @Inject(
            method = "rightClick",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private static void mysticalIndex$plsDontOverrideOurOnUseFunctionThanks(PlayerEntity player, World world, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (world.getBlockState(hit.getBlockPos()).isOf(ModBlocks.MYSTICAL_LECTERN)) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }
}
