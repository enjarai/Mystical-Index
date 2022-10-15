package net.messer.mystical_index.mixin;

import net.messer.mystical_index.item.ModLootTables;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderPearlEntity.class)
public abstract class EnderPearlEntityMixin extends ThrownItemEntity {

    public EnderPearlEntityMixin(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
            method = "onCollision",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/projectile/thrown/EnderPearlEntity;discard()V"
            )
    )
    private void mysticalIndex$onEnderpearlCollide(HitResult hitResult, CallbackInfo ci) {
        var server = getServer();

        if (server != null) {
            var serverWorld = (ServerWorld) getWorld();
            var pos = hitResult.getPos();
            var lootContext = new LootContext.Builder(serverWorld).build(LootContextTypes.EMPTY);
            var lootTable = server.getLootManager().getTable(ModLootTables.DROP_ENDER_PEARL_SHARDS);
            var stacks = lootTable.generateLoot(lootContext);

            stacks.forEach(stack -> {
                ItemEntity itemEntity = new ItemEntity(serverWorld, pos.x, pos.y, pos.z, stack.copy());
                itemEntity.setToDefaultPickupDelay();
                serverWorld.spawnEntity(itemEntity);
            });
            serverWorld.playSoundFromEntity(
                    null, this,
                    SoundEvents.BLOCK_GLASS_BREAK, getSoundCategory(),
                    1.0F, 1.0F
            );
        }
    }
}
