package net.messer.mystical_index.event;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.messer.mystical_index.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.util.Identifier;

public class LootTableEvent {
    private static final Identifier ZOMBIE_LOOT_TABLE_ID = EntityType.ZOMBIE.getLootTableId();

    public static void registerLootTable(){
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if(ZOMBIE_LOOT_TABLE_ID.equals(id)){
                LootPool.Builder poolBuilder = LootPool.builder()
                                .with(ItemEntry.builder(ModItems.EMPTY_PAGE))
                                .conditionally(RandomChanceLootCondition.builder(0.1f));
                tableBuilder.pool(poolBuilder);
            }
        });
    }
}
