package dev.enjarai.arcane_repository.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.stream.Stream;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Inject(
            method = "onItemEntityDestroyed",
            at = @At("HEAD")
    )
    protected void onItemEntityDestroyedHead(ItemEntity entity, CallbackInfo ci) {
        if (entity.getWorld() instanceof ServerWorld serverWorld) {
            var position = entity.getPos();

            ItemUsage.spawnItemContents(entity, getItemEntityDestroyedDrops(serverWorld, entity));

            var soundEvent = getItemEntityDestroyedSound(serverWorld, entity);
            if (soundEvent != null) {
                float randomVolume = 1.0F + serverWorld.getRandom().nextFloat() * 0.2F;
                float randomPitch = 0.9F + serverWorld.getRandom().nextFloat() * 0.2F;
                serverWorld.playSound(null, position.x, position.y, position.z, soundEvent, SoundCategory.BLOCKS, randomVolume, randomPitch);
            }
        }
    }

    protected Iterable<ItemStack> getItemEntityDestroyedDrops(ServerWorld serverWorld, ItemEntity entity) {
        return List.of();
    }

    @Nullable
    protected SoundEvent getItemEntityDestroyedSound(ServerWorld serverWorld, ItemEntity entity) {
        return null;
    }
}
