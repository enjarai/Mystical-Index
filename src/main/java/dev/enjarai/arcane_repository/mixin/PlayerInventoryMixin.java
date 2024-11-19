package dev.enjarai.arcane_repository.mixin;

import dev.enjarai.arcane_repository.item.custom.book.MysticalBookItem;
import dev.enjarai.arcane_repository.item.custom.page.attribute.PickupAttributePage;
import dev.enjarai.arcane_repository.item.custom.page.type.ItemInsertableTypePage;
import dev.enjarai.arcane_repository.item.custom.page.type.ItemStorageTypePage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {
    @Shadow @Final public PlayerEntity player;

    @Inject(method = "insertStack(Lnet/minecraft/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    private void mysticalIndex$onItemPickup(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (canInterceptPickup((PlayerInventory)(Object) this, stack)) cir.setReturnValue(true);
    }

    private boolean canInterceptPickup(PlayerInventory inventory, ItemStack itemPickedUp){
        var foundBooks = inventory.main.stream()
                .filter(itemStack -> itemStack.getItem() instanceof MysticalBookItem)
                .toList();


        for (ItemStack bookStack : foundBooks){
            var bookitem = (MysticalBookItem) bookStack.getItem();
            if (bookitem.getTypePage(bookStack).orElse(null) instanceof ItemInsertableTypePage storageTypePage) {
                var foundPage = bookitem.getAttributePage(bookStack,"pickup");
                if (foundPage != null && bookitem.getAttributePage(bookStack, "pickup").orElse(null) instanceof PickupAttributePage pickupAttributePage) {
                    storageTypePage.book$tryInsertItemStack(bookStack, player, itemPickedUp);
                    return itemPickedUp.getCount() <= 0;
                }
            }
        }

        return false;
    }
}
