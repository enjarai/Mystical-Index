package net.messer.mystical_index.event;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.messer.mystical_index.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.condition.RandomChanceWithLootingLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;

public class LootTableEvent {
    public static void registerLootTable(){
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (EntityType.ZOMBIE.getLootTableId().equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .with(ItemEntry.builder(ModItems.TATTERED_PAGE))
                        .conditionally(RandomChanceWithLootingLootCondition.builder(0.1f, 0.05f));
                tableBuilder.pool(poolBuilder);
            } else if (EntityType.ENDERMAN.getLootTableId().equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .with(ItemEntry.builder(ModItems.ENDER_PEARL_SHARD))
                        .rolls(UniformLootNumberProvider.create(0, 2))
                        .conditionally(RandomChanceLootCondition.builder(0.5f));
                tableBuilder.pool(poolBuilder);
            } else if (LootTables.STRONGHOLD_LIBRARY_CHEST.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .with(ItemEntry.builder(ModItems.TATTERED_PAGE))
                        .rolls(UniformLootNumberProvider.create(5, 15))
                        .conditionally(RandomChanceLootCondition.builder(0.5f));
                tableBuilder.pool(poolBuilder);
            } else if (LootTables.ABANDONED_MINESHAFT_CHEST.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .with(ItemEntry.builder(ModItems.TATTERED_PAGE))
                        .rolls(UniformLootNumberProvider.create(2, 8))
                        .conditionally(RandomChanceLootCondition.builder(0.2f));
                tableBuilder.pool(poolBuilder);
            } else if (LootTables.SIMPLE_DUNGEON_CHEST.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .with(ItemEntry.builder(ModItems.TATTERED_PAGE))
                        .rolls(UniformLootNumberProvider.create(2, 8))
                        .conditionally(RandomChanceLootCondition.builder(0.3f));
                tableBuilder.pool(poolBuilder);
            }
        });
    }
}
