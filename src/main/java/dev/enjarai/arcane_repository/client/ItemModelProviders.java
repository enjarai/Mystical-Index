package dev.enjarai.arcane_repository.client;

import dev.enjarai.arcane_repository.item.ModDataComponentTypes;
import dev.enjarai.arcane_repository.item.component.MysticalBookComponent;
import dev.enjarai.arcane_repository.item.custom.book.MysticalBookItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.mixin.object.builder.client.ModelPredicateProviderRegistryAccessor;
import dev.enjarai.arcane_repository.item.ModItems;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import static dev.enjarai.arcane_repository.item.ModItems.MYSTICAL_BOOK;

@Environment(value = EnvType.CLIENT)
public class ItemModelProviders {
    public static void register() {
        registerBookModel(Registries.ITEM.getId(ModItems.ITEM_STORAGE_TYPE_PAGE), "item_storage_type");
        registerBookModel(Registries.ITEM.getId(ModItems.INDEXING_TYPE_PAGE), "indexing_type");
        registerBookModel(Registries.ITEM.getId(ModItems.FOOD_STORAGE_TYPE_PAGE), "food_storage_type");
        registerBookModel(Registries.ITEM.getId(ModItems.BLOCK_STORAGE_TYPE_PAGE), "block_storage_type");
        registerBookModel(Registries.ITEM.getId(ModItems.INDEX_SLAVE_TYPE_PAGE), "index_slave_type");

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex == 0 ? -1 : MYSTICAL_BOOK.getColor(stack), MYSTICAL_BOOK);
    }

    private static void registerBookModel(Identifier id, String name) {
        ModelPredicateProviderRegistryAccessor.callRegister(Identifier.of(name), (itemStack, clientWorld, livingEntity, i) -> {
            MysticalBookComponent component;
            if (itemStack.getItem() == MYSTICAL_BOOK && (component = itemStack.get(ModDataComponentTypes.MYSTICAL_BOOK)) != null) {
                if (id.equals(component.typePage())) {
                    return 1;
                }
            }
            return 0;
        });
    }
}
