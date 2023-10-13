package net.messer.mystical_index.mixin.accessor;

import net.minecraft.entity.ItemEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.UUID;

@Mixin(ItemEntity.class)
public interface ItemEntityAccessor {
    @Accessor
    @Nullable
    UUID getThrower();
}
