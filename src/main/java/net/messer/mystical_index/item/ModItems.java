package net.messer.mystical_index.item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.messer.mystical_index.MysticalIndex;
import net.messer.mystical_index.item.custom.book.MysticalBookItem;
import net.messer.mystical_index.item.custom.page.PageItem;
import net.messer.mystical_index.item.custom.page.action.BuildingActionPage;
import net.messer.mystical_index.item.custom.page.action.FeedingActionPage;
import net.messer.mystical_index.item.custom.page.attribute.*;
import net.messer.mystical_index.item.custom.page.type.*;
import net.messer.mystical_index.util.PageRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import vazkii.patchouli.api.PatchouliAPI;

public class ModItems {
    public static final ItemGroup MYSTICAL_INDEX_GROUP = FabricItemGroupBuilder.build(
            MysticalIndex.id("mystical_index"),
            () -> PatchouliAPI.get().getBookStack(MysticalIndex.id("guide_book")));

    public static final Item ENDER_PEARL_SHARD = registerItem("ender_pearl_shard", new Item(new FabricItemSettings().group(MYSTICAL_INDEX_GROUP)));

    public static final MysticalBookItem MYSTICAL_BOOK = registerItem("mystical_book", new MysticalBookItem(new FabricItemSettings().maxCount(1)));
    public static final Item EMPTY_PAGE = registerItem("empty_page", new Item(new FabricItemSettings().group(MYSTICAL_INDEX_GROUP)));
    public static final Item TATTERED_PAGE = registerItem("tattered_page", new Item(new FabricItemSettings().group(MYSTICAL_INDEX_GROUP)));

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
        var id = MysticalIndex.id(name);
        Registry.register(Registry.ITEM, id, item);
        PageRegistry.registerPage(id, item);
        return item;
    }

    private static <T extends Item> T registerItem(String name, T item){
        return Registry.register(Registry.ITEM, new Identifier(MysticalIndex.MOD_ID, name), item);
    }

    public static void registerModItems(){
        MysticalIndex.LOGGER.info("Registering items for " + MysticalIndex.MOD_ID);
    }
}
