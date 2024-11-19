package dev.enjarai.arcane_repository.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;

import java.util.List;

public record StorageFilterComponent(
  List<Identifier> items,
  int occupiedStacks,
  int occupiedTypes
) {
    public static final Codec<StorageFilterComponent> CODEC = RecordCodecBuilder.create(instance ->
      instance.group(
        Identifier.CODEC.listOf().optionalFieldOf("items", List.of()).forGetter(StorageFilterComponent::items),
        Codec.INT.optionalFieldOf("occupiedStacks", 0).forGetter(StorageFilterComponent::occupiedStacks),
        Codec.INT.optionalFieldOf("occupiedTypes", 0).forGetter(StorageFilterComponent::occupiedTypes)
      ).apply(instance, StorageFilterComponent::new)
    );

    public StorageFilterComponent withItems(List<Identifier> newItems) {
        return new StorageFilterComponent(newItems, occupiedStacks, occupiedTypes);
    }

    public StorageFilterComponent withOccupiedStacks(int newOccupiedStacks) {
        return new StorageFilterComponent(items, newOccupiedStacks, occupiedTypes);
    }

    public StorageFilterComponent withOccupiedTypes(int newOccupiedTypes) {
        return new StorageFilterComponent(items, occupiedStacks, newOccupiedTypes);
    }
}
