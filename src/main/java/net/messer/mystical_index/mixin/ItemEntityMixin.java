package net.messer.mystical_index.mixin;

import net.messer.mystical_index.item.custom.book.MysticalBookItem;
import net.messer.mystical_index.item.custom.page.type.ItemStorageTypePage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;

@Mixin(value = ItemEntity.class, priority = 800)
public abstract class ItemEntityMixin {
    // Shamelessly stolen from Spectrum
    // See: https://github.com/DaFuqs/Spectrum/blob/1.19/src/main/java/de/dafuqs/spectrum/mixin/ItemEntityMixin.java
    @Inject(at = @At("HEAD"), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z")
    public void spectrumItemStackDamageActions(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (DamageSource.ANVIL.equals(source)) {
            doAnvilSmash(amount);
        }
    }

    private void doAnvilSmash(float damageAmount) {
        ItemEntity thisEntity = (ItemEntity) (Object) this;
        ItemStack thisItemStack = thisEntity.getStack();
        World world = thisEntity.getEntityWorld();

        if (thisItemStack.getItem() instanceof MysticalBookItem book) {

            if (damageAmount > 0) {
                Vec3d position = thisEntity.getPos();

                // Grab the components of the book
                var resultStacks = new ArrayList<ItemStack>();
                resultStacks.add(book.getCatalyst(thisItemStack).getDefaultStack());
                book.forEachPage(thisItemStack, page -> resultStacks.add(page.getDefaultStack()));

                // Grab the items from the book if it's a storage book
                if (book.getTypePage(thisItemStack) instanceof ItemStorageTypePage typePage) {
                    typePage.getContents(thisItemStack).forEach(bigStack -> resultStacks.addAll(bigStack.toStacks()));
                }

                // Delete the destroyed item
                thisEntity.remove(Entity.RemovalReason.DISCARDED);

                // Spawn the resulting item stack in the world
                for (var stack : resultStacks) {
                    ItemEntity itemEntity = new ItemEntity(world, position.x, position.y, position.z, stack);
                    world.spawnEntity(itemEntity);
                }

                // Play sound
                SoundEvent soundEvent = SoundEvents.BLOCK_WOOL_BREAK;
                if (soundEvent != null) {
                    float randomVolume = 1.0F + world.getRandom().nextFloat() * 0.2F;
                    float randomPitch = 0.9F + world.getRandom().nextFloat() * 0.2F;
                    world.playSound(null, position.x, position.y, position.z, soundEvent, SoundCategory.PLAYERS, randomVolume, randomPitch);
                }
            }
        }
    }
}
