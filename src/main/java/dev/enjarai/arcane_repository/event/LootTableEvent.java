package dev.enjarai.arcane_repository.event;

import dev.enjarai.arcane_repository.item.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.condition.RandomChanceWithEnchantedBonusLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;

public class LootTableEvent {
    public static void registerLootTable() {
        LootTableEvents.MODIFY.register((registryKey, builder, lootTableSource, wrapperLookup) -> {
            if (EntityType.ZOMBIE.getLootTableId().equals(registryKey)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                  .with(ItemEntry.builder(ModItems.TATTERED_PAGE))
                  .conditionally(RandomChanceWithEnchantedBonusLootCondition.builder(wrapperLookup, 0.1f, 0.05f));
                builder.pool(poolBuilder);
            } else if (EntityType.ENDERMAN.getLootTableId().equals(registryKey)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                  .with(ItemEntry.builder(ModItems.ENDER_PEARL_SHARD))
                  .rolls(UniformLootNumberProvider.create(0, 2))
                  .conditionally(RandomChanceLootCondition.builder(0.5f));
                builder.pool(poolBuilder);
            } else if (LootTables.STRONGHOLD_LIBRARY_CHEST.equals(registryKey)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                  .with(ItemEntry.builder(ModItems.TATTERED_PAGE))
                  .rolls(UniformLootNumberProvider.create(5, 15))
                  .conditionally(RandomChanceLootCondition.builder(0.5f));
                builder.pool(poolBuilder);
            } else if (LootTables.ABANDONED_MINESHAFT_CHEST.equals(registryKey)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                  .with(ItemEntry.builder(ModItems.TATTERED_PAGE))
                  .rolls(UniformLootNumberProvider.create(2, 8))
                  .conditionally(RandomChanceLootCondition.builder(0.2f));
                builder.pool(poolBuilder);
            } else if (LootTables.SIMPLE_DUNGEON_CHEST.equals(registryKey)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                  .with(ItemEntry.builder(ModItems.TATTERED_PAGE))
                  .rolls(UniformLootNumberProvider.create(2, 8))
                  .conditionally(RandomChanceLootCondition.builder(0.3f));
                builder.pool(poolBuilder);
            }
        });
    }
}
