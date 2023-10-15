package dev.enjarai.arcane_repository.item;

import dev.enjarai.arcane_repository.ArcaneRepository;
import dev.enjarai.arcane_repository.item.custom.book.MysticalBookItem;
import dev.enjarai.arcane_repository.item.custom.page.PageItem;
import dev.enjarai.arcane_repository.item.custom.page.action.BuildingActionPage;
import dev.enjarai.arcane_repository.item.custom.page.action.FeedingActionPage;
import dev.enjarai.arcane_repository.item.custom.page.attribute.*;
import dev.enjarai.arcane_repository.item.custom.page.type.*;
import dev.enjarai.arcane_repository.util.PageRegistry;
import io.wispforest.lavender.book.LavenderBookItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final LavenderBookItem GUIDE_BOOK = LavenderBookItem.registerForBook(ArcaneRepository.id("guide_book"), new FabricItemSettings().maxCount(1));

    public static final ItemGroup MYSTICAL_INDEX_GROUP = FabricItemGroup.builder()
            .icon(GUIDE_BOOK::getDefaultStack)
            .displayName(Text.translatable("item_group.arcane_repository.arcane_repository"))
            .build();

    public static final Item ENDER_PEARL_SHARD = registerItem("ender_pearl_shard", new Item(new FabricItemSettings()));

    public static final MysticalBookItem MYSTICAL_BOOK = registerItem("repository_book", new MysticalBookItem(new FabricItemSettings().maxCount(1)));
    public static final Item EMPTY_PAGE = registerItem("empty_page", new Item(new FabricItemSettings()));
    public static final Item TATTERED_PAGE = registerItem("tattered_page", new Item(new FabricItemSettings()));

    // Type Pages
    public static final ItemStorageTypePage ITEM_STORAGE_TYPE_PAGE = registerPageItem("item_storage_type_page", new ItemStorageTypePage("item_storage"));
    public static final FoodStorageTypePage FOOD_STORAGE_TYPE_PAGE = registerPageItem("food_storage_type_page", new FoodStorageTypePage("food_storage"));
    public static final BlockStorageTypePage BLOCK_STORAGE_TYPE_PAGE = registerPageItem("block_storage_type_page", new BlockStorageTypePage("block_storage"));
    public static final IndexingTypePage INDEXING_TYPE_PAGE = registerPageItem("indexing_type_page", new IndexingTypePage("indexing"));
    public static final IndexSlaveTypePage INDEX_SLAVE_TYPE_PAGE = registerPageItem("index_slave_type_page", new IndexSlaveTypePage("index_slave"));

    // Action Pages
    public static final FeedingActionPage FEEDING_ACTION_PAGE = registerPageItem("feeding_action_page", new FeedingActionPage("feeding"));
    public static final BuildingActionPage BUILDING_ACTION_PAGE = registerPageItem("building_action_page", new BuildingActionPage("building"));

    // Attribute Pages
    public static final StacksPage STACKS_PAGE = registerPageItem("stacks_page", new StacksPage("stacks"));
    public static final TypesPage TYPES_PAGE = registerPageItem("types_page", new TypesPage("types"));
    public static final RangePage RANGE_PAGE = registerPageItem("range_page", new RangePage("range"));
    public static final LinksPage LINKS_PAGE = registerPageItem("links_page", new LinksPage("links"));
    public static final AutoFeedingAttributePage AUTO_FEEDING_ATTRIBUTE_PAGE = registerPageItem("auto_feeding_attribute_page", new AutoFeedingAttributePage("auto_feeding"));
    public static final MagnetismAttributePage MAGNETISM_ATTRIBUTE_PAGE = registerPageItem("magnetism_attribute_page", new MagnetismAttributePage("magnetism"));
    public static final PickupAttributePage PICKUP_ATTRIBUTE_PAGE = registerPageItem("pickup_attribute_page", new PickupAttributePage("pickup"));

    private static <T extends PageItem> T registerPageItem(String name, T item) {
        var id = ArcaneRepository.id(name);
        Registry.register(Registries.ITEM, id, item);
        PageRegistry.registerPage(id, item);
        return item;
    }

    private static <T extends Item> T registerItem(String name, T item){
        return Registry.register(Registries.ITEM, new Identifier(ArcaneRepository.MOD_ID, name), item);
    }

    public static void registerModItems(){
        ArcaneRepository.LOGGER.info("Registering items for " + ArcaneRepository.MOD_ID);

        Registry.register(Registries.ITEM_GROUP, ArcaneRepository.id("arcane_repository"), MYSTICAL_INDEX_GROUP);
        ItemGroupEvents.modifyEntriesEvent(RegistryKey.of(RegistryKeys.ITEM_GROUP, ArcaneRepository.id("arcane_repository"))).register(entries -> {
            entries.add(GUIDE_BOOK);

            entries.add(ENDER_PEARL_SHARD);
            entries.add(EMPTY_PAGE);
            entries.add(TATTERED_PAGE);

            entries.add(ITEM_STORAGE_TYPE_PAGE);
            entries.add(FOOD_STORAGE_TYPE_PAGE);
            entries.add(BLOCK_STORAGE_TYPE_PAGE);
            entries.add(INDEXING_TYPE_PAGE);
            entries.add(INDEX_SLAVE_TYPE_PAGE);

            entries.add(FEEDING_ACTION_PAGE);
            entries.add(BUILDING_ACTION_PAGE);

            entries.add(STACKS_PAGE);
            entries.add(TYPES_PAGE);
            entries.add(RANGE_PAGE);
            entries.add(LINKS_PAGE);
            entries.add(AUTO_FEEDING_ATTRIBUTE_PAGE);
            entries.add(MAGNETISM_ATTRIBUTE_PAGE);
            entries.add(PICKUP_ATTRIBUTE_PAGE);
        });
    }
}
