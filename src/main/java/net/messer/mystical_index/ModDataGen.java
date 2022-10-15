package net.messer.mystical_index;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

public class ModDataGen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        fabricDataGenerator.addProvider(ModTagGenerator::new);
    }

    private static class ModTagGenerator extends FabricTagProvider<Item> {
        public ModTagGenerator(FabricDataGenerator dataGenerator) {
            super(dataGenerator, Registry.ITEM, MysticalIndex.MOD_ID);
        }

        private static final TagKey<Item> BLOCK_ITEMS = TagKey.of(Registry.ITEM_KEY, MysticalIndex.id("block_items"));
        private static final TagKey<Item> FOOD_ITEMS = TagKey.of(Registry.ITEM_KEY, MysticalIndex.id("food_items"));

        @Override
        protected void generateTags() {
            var blockItems = getOrCreateTagBuilder(BLOCK_ITEMS);
            var foodItems = getOrCreateTagBuilder(FOOD_ITEMS);

            Registry.ITEM.stream().filter(BlockItem.class::isInstance).forEach(blockItems::add);
            Registry.ITEM.stream().filter(Item::isFood).forEach(foodItems::add);
        }
    }
}
