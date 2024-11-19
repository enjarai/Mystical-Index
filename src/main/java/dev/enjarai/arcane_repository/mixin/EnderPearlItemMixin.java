package dev.enjarai.arcane_repository.mixin;

import dev.enjarai.arcane_repository.item.ModLootTables;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.stream.Stream;

@Mixin(EnderPearlItem.class)
public abstract class EnderPearlItemMixin extends ItemMixin {
    @Override
    protected Iterable<ItemStack> getItemEntityDestroyedDrops(ServerWorld serverWorld, ItemEntity entity) {
        var lootContext = new LootContextParameterSet.Builder(serverWorld).build(LootContextTypes.EMPTY);
        var lootTable = serverWorld.getServer().getReloadableRegistries().getLootTable(ModLootTables.DROP_ENDER_PEARL_SHARDS);
        return lootTable.generateLoot(lootContext);
    }

    @Override
    protected @Nullable SoundEvent getItemEntityDestroyedSound(ServerWorld serverWorld, ItemEntity entity) {
        return SoundEvents.BLOCK_GLASS_BREAK;
    }
}
