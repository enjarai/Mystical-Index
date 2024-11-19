package dev.enjarai.arcane_repository.mixin;

import dev.enjarai.arcane_repository.duck.RepositoryDrop;
import net.minecraft.entity.ItemEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityMixin implements RepositoryDrop {
    @Unique boolean isDrop = false;

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readExtraData(NbtCompound nbt, CallbackInfo ci) {
        isDrop = nbt.getBoolean("arcane_repository$RepositoryDrop");
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeExtraData(NbtCompound nbt, CallbackInfo ci) {
        if (isDrop) {
            nbt.putBoolean("arcane_repository$RepositoryDrop", true);
        }
    }

    @Override
    public boolean arcane_repository$isRepositoryDrop() {
        return isDrop;
    }

    @Override
    public void arcane_repository$setRepositoryDrop(boolean isDrop) {
        this.isDrop = isDrop;
    }
}
