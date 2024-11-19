package dev.enjarai.arcane_repository.item;

import dev.enjarai.arcane_repository.ArcaneRepository;
import dev.enjarai.arcane_repository.mixin.accessor.LootTablesAccessor;
import net.fabricmc.fabric.mixin.loot.LootTableAccessor;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModLootTables {
    public static final RegistryKey<LootTable> DROP_ENDER_PEARL_SHARDS = register("drop_ender_pearl_shards");

    private static RegistryKey<LootTable> register(String id) {
        return registerLootTable(RegistryKey.of(RegistryKeys.LOOT_TABLE, ArcaneRepository.id(id)));
    }

    private static RegistryKey<LootTable> registerLootTable(RegistryKey<LootTable> key) {
        if (LootTablesAccessor.getLootTableRegistry().add(key)) {
            return key;
        } else {
            throw new IllegalArgumentException(key.getValue() + " is already a registered built-in loot table");
        }
    }
}
