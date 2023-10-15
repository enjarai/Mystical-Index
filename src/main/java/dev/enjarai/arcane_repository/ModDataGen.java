package dev.enjarai.arcane_repository;

import dev.enjarai.arcane_repository.item.custom.page.ActionPageItem;
import dev.enjarai.arcane_repository.item.custom.page.AttributePageItem;
import dev.enjarai.arcane_repository.item.custom.page.TypePageItem;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;

import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class ModDataGen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        fabricDataGenerator.createPack().addProvider(ModTagGenerator::new);
    }

    private static class ModTagGenerator extends FabricTagProvider<Item> {
        public ModTagGenerator(FabricDataOutput dataGenerator, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(dataGenerator, RegistryKeys.ITEM, registriesFuture);
        }

        private static final TagKey<Item> BLOCK_ITEMS = TagKey.of(RegistryKeys.ITEM, ArcaneRepository.id("block_items"));
        private static final TagKey<Item> FOOD_ITEMS = TagKey.of(RegistryKeys.ITEM, ArcaneRepository.id("food_items"));

        private static final TagKey<Item> TYPE_PAGES = TagKey.of(RegistryKeys.ITEM, ArcaneRepository.id("type_pages"));
        private static final TagKey<Item> ACTION_PAGES = TagKey.of(RegistryKeys.ITEM, ArcaneRepository.id("action_pages"));
        private static final TagKey<Item> ATTRIBUTE_PAGES = TagKey.of(RegistryKeys.ITEM, ArcaneRepository.id("attribute_pages"));

        @Override
        protected void configure(RegistryWrapper.WrapperLookup arg) {
            var blockItems = getOrCreateTagBuilder(BLOCK_ITEMS);
            var foodItems = getOrCreateTagBuilder(FOOD_ITEMS);

            Registries.ITEM.stream().filter(BlockItem.class::isInstance).forEach(blockItems::add);
            Registries.ITEM.stream().filter(Item::isFood).forEach(foodItems::add);

            createTagByFilter(TYPE_PAGES, TypePageItem.class::isInstance);
            createTagByFilter(ACTION_PAGES, ActionPageItem.class::isInstance);
            createTagByFilter(ATTRIBUTE_PAGES, AttributePageItem.class::isInstance);
        }

        private void createTagByFilter(TagKey<Item> tag, Predicate<Item> predicate) {
            var adder = getOrCreateTagBuilder(tag);
            Registries.ITEM.stream().filter(predicate).forEach(adder::add);
        }
    }
}
