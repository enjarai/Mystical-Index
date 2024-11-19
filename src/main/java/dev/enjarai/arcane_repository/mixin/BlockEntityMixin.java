package dev.enjarai.arcane_repository.mixin;

import dev.enjarai.arcane_repository.block.ModBlockEntities;
import dev.enjarai.arcane_repository.block.entity.MysticalLecternBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public class BlockEntityMixin {
    @Shadow @Final @Mutable
    private BlockEntityType<?> type;

    @Inject(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/entity/BlockEntity;validateSupports(Lnet/minecraft/block/BlockState;)V"
            )
    )
    private void modifyLecternType(BlockEntityType<?> type, BlockPos pos, BlockState state, CallbackInfo ci) {
        if (((Object) this) instanceof MysticalLecternBlockEntity) {
            this.type = ModBlockEntities.MYSTICAL_LECTERN_BLOCK_ENTITY;
        }
    }
}
