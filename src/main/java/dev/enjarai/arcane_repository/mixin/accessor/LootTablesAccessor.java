package dev.enjarai.arcane_repository.mixin.accessor;

import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(LootTables.class)
public interface LootTablesAccessor {
    static @Accessor("LOOT_TABLES") Set<RegistryKey<LootTable>> getLootTableRegistry() {
        throw new IllegalStateException("Accessor Mixin failed, this should never happen!");
    };
}
