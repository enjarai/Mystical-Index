package dev.enjarai.arcane_repository.mixin;

import dev.enjarai.arcane_repository.item.ModLootTables;
import dev.enjarai.arcane_repository.item.custom.book.MysticalBookItem;
import dev.enjarai.arcane_repository.item.custom.page.type.ItemStorageTypePage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;

@Mixin(value = ItemEntity.class, priority = 800)
public abstract class ItemEntityMixin extends Entity {
    @Shadow public abstract ItemStack getStack();

    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    // Shamelessly stolen from Spectrum
    // See: https://github.com/DaFuqs/Spectrum/blob/1.19/src/main/java/de/dafuqs/spectrum/mixin/ItemEntityMixin.java
    @Inject(at = @At("HEAD"), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z")
    public void spectrumItemStackDamageActions(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (source.isOf(DamageTypes.FALLING_ANVIL) && getWorld() instanceof ServerWorld serverWorld) {
            doAnvilSmash(amount, serverWorld);
        }
    }

    private void doAnvilSmash(float damageAmount, ServerWorld serverWorld) {
        ItemStack thisItemStack = getStack();

        if (damageAmount > 0) {
            Vec3d position = getPos();

            // Grab the components of the book
            var resultStacks = new ArrayList<ItemStack>();
            var broken = false;
            SoundEvent soundEvent = null;

            if (thisItemStack.getItem() instanceof MysticalBookItem book) {
                resultStacks.add(book.getCatalyst(thisItemStack).getDefaultStack());
                book.forEachPage(thisItemStack, page -> resultStacks.add(page.getDefaultStack()));

                // Grab the items from the book if it's a storage book
                if (book.getTypePage(thisItemStack) instanceof ItemStorageTypePage typePage) {
                    typePage.getContents(thisItemStack).forEach(bigStack -> resultStacks.addAll(bigStack.toStacks()));
                }

                broken = true;
                soundEvent = SoundEvents.BLOCK_WOOL_BREAK;
            } else if (thisItemStack.isOf(Items.ENDER_PEARL)) {
                var lootContext = new LootContextParameterSet.Builder(serverWorld).build(LootContextTypes.EMPTY);
                var lootTable = serverWorld.getServer().getLootManager().getLootTable(ModLootTables.DROP_ENDER_PEARL_SHARDS);
                var stacks = lootTable.generateLoot(lootContext);

                resultStacks.addAll(stacks);

                broken = true;
                soundEvent = SoundEvents.BLOCK_GLASS_BREAK;
            }

            if (broken) {
                // Delete the destroyed item
                remove(Entity.RemovalReason.DISCARDED);

                // Spawn the resulting item stack in the world
                for (var stack : resultStacks) {
                    ItemEntity itemEntity = new ItemEntity(serverWorld, position.x, position.y, position.z, stack);
                    serverWorld.spawnEntity(itemEntity);
                }

                // Play sound
                if (soundEvent != null) {
                    float randomVolume = 1.0F + serverWorld.getRandom().nextFloat() * 0.2F;
                    float randomPitch = 0.9F + serverWorld.getRandom().nextFloat() * 0.2F;
                    serverWorld.playSound(null, position.x, position.y, position.z, soundEvent, SoundCategory.BLOCKS, randomVolume, randomPitch);
                }
            }
        }
    }
}
