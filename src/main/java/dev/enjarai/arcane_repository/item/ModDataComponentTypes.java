package dev.enjarai.arcane_repository.item;

import dev.enjarai.arcane_repository.ArcaneRepository;
import dev.enjarai.arcane_repository.item.component.StorageFilterComponent;
import dev.enjarai.arcane_repository.item.component.MysticalBookComponent;
import net.minecraft.component.ComponentType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.function.UnaryOperator;

public class ModDataComponentTypes {
    public static final ComponentType<MysticalBookComponent> MYSTICAL_BOOK = register("mystical_book", builder -> builder.codec(MysticalBookComponent.CODEC));
    public static final ComponentType<NbtCompound> PAGE_ITEM_ATTRIBUTES = register("page_item/attributes", builder -> builder.codec(NbtCompound.CODEC));
    public static final ComponentType<List<BlockPos>> INDEXING_PAGE_ITEM_LINKED_BLOCKS = register("page_item/indexing/linked_blocks", builder -> builder.codec(BlockPos.CODEC.listOf()));
    public static final ComponentType<StorageFilterComponent> STORAGE_FILTERS = register("storage_filters", builder -> builder.codec(StorageFilterComponent.CODEC));

    private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, ArcaneRepository.id(id), builderOperator.apply(ComponentType.builder()).build());
    }

    public static void registerDataComponentTypes() {
        ArcaneRepository.LOGGER.debug("Registering data components for " + ArcaneRepository.MOD_ID);
    }
}
