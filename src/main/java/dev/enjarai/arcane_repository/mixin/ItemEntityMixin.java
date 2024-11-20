package dev.enjarai.arcane_repository.mixin;

import dev.enjarai.arcane_repository.duck.RepositoryDrop;
import dev.enjarai.arcane_repository.item.custom.book.MysticalBookItem;
import dev.enjarai.arcane_repository.item.custom.page.attribute.PickupAttributePage;
import dev.enjarai.arcane_repository.item.custom.page.type.ItemInsertableTypePage;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

@Mixin(ItemEntity.class)
public class ItemEntityMixin implements RepositoryDrop {
    @Unique
    boolean isDrop = false;

    @WrapOperation(
        method = "onPlayerCollision(Lnet/minecraft/entity/player/PlayerEntity;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerInventory;insertStack(Lnet/minecraft/item/ItemStack;)Z"
        )
    )
    private boolean tryInterceptPickup(PlayerInventory inventory, ItemStack stack, Operation<Boolean> op, @Local(argsOnly = true) PlayerEntity player) {
        return canInterceptPickup(player, stack) || op.call(inventory, stack);
    }

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

    private boolean canInterceptPickup(PlayerEntity player, ItemStack itemPickedUp) {
        if (arcane_repository$isRepositoryDrop())
            return false;

        var foundBooks = player.getInventory().main.stream()
                .filter(itemStack -> itemStack.getItem() instanceof MysticalBookItem)
                .toList();

        for (ItemStack bookStack : foundBooks) {
            var bookitem = (MysticalBookItem) bookStack.getItem();
            if (bookitem.getTypePage(bookStack).orElse(null) instanceof ItemInsertableTypePage storageTypePage) {
                var foundPage = bookitem.getAttributePage(bookStack, "pickup");
                if (foundPage != null && bookitem.getAttributePage(bookStack, "pickup").orElse(null) instanceof PickupAttributePage) {
                    storageTypePage.book$tryInsertItemStack(bookStack, player, itemPickedUp);
                    return itemPickedUp.getCount() <= 0;
                }
            }
        }

        return false;
    }
}
