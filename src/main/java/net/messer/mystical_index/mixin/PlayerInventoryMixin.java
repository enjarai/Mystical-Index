package net.messer.mystical_index.mixin;

import net.messer.mystical_index.MysticalIndex;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
    @Inject(method = "insertStack(Lnet/minecraft/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    private void onItemPickup(ItemStack stack, CallbackInfoReturnable<Boolean> cir){
        if(canInterceptPickup((PlayerInventory)(Object) this, stack)) cir.setReturnValue(true);
    }

    private static boolean canInterceptPickup(PlayerInventory inventory, ItemStack itemPickedUp){
        MysticalIndex.LOGGER.info("I am trying to intercept a pickup.");
        return false;
    }
}
