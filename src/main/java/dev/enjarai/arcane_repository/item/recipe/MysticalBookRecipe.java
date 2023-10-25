package dev.enjarai.arcane_repository.item.recipe;

import dev.enjarai.arcane_repository.item.custom.book.MysticalBookItem;
import dev.enjarai.arcane_repository.item.ModItems;
import dev.enjarai.arcane_repository.item.ModRecipes;
import dev.enjarai.arcane_repository.item.custom.page.ActionPageItem;
import dev.enjarai.arcane_repository.item.custom.page.AttributePageItem;
import dev.enjarai.arcane_repository.item.custom.page.PageItem;
import dev.enjarai.arcane_repository.item.custom.page.TypePageItem;
import dev.enjarai.arcane_repository.util.Colors;
import dev.enjarai.arcane_repository.util.PageRegistry;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtString;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Map;

public class MysticalBookRecipe extends SpecialCraftingRecipe {
    private static final Ingredient BINDING = Ingredient.ofItems(Items.LEATHER);
    // Defines how many pages are supported by each catalyst item.
    private static final Map<Item, Integer> CATALYSTS = Map.of(
        Items.AMETHYST_SHARD, 2,
        Items.EMERALD, 4,
        Items.DIAMOND, 6
    );
    private static final Ingredient CATALYST = Ingredient.ofItems(CATALYSTS.keySet().toArray(new Item[0]));
    private static final Ingredient TYPE_PAGES = Ingredient.ofItems(PageRegistry.getPages(TypePageItem.class).toArray(new Item[0]));
    private static final Ingredient ATTRIBUTE_PAGES = Ingredient.ofItems(PageRegistry.getPages(AttributePageItem.class).toArray(new Item[0]));
    private static final Ingredient ACTION_PAGES = Ingredient.ofItems(PageRegistry.getPages(ActionPageItem.class).toArray(new Item[0]));

    public MysticalBookRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(RecipeInputInventory craftingInventory, World world) {
        var binding = false;
        var catalyst = 0;
        TypePageItem typePage = null;
        ActionPageItem actionPage = null;
        var pages = new ArrayList<PageItem>();

        // Check binding and catalyst, and store type page.
        for (int i = 0; i < craftingInventory.size(); ++i) {
            var itemStack = craftingInventory.getStack(i);
            if (itemStack.isEmpty()) continue;
            if (BINDING.test(itemStack)) {
                if (binding) {
                    return false;
                }
                binding = true;
                continue;
            }
            if (CATALYST.test(itemStack)) {
                if (catalyst > 0) {
                    return false;
                }
                catalyst = CATALYSTS.get(itemStack.getItem());
                continue;
            }
            if (itemStack.getItem() instanceof TypePageItem page) {
                if (typePage != null) {
                    return false;
                }
                typePage = page;
                pages.add(page);
                continue;
            }
            if (itemStack.getItem() instanceof PageItem) {
                continue;
            }
            return false;
        }

        // Store action page.
        for (int i = 0; i < craftingInventory.size(); ++i) {
            var itemStack = craftingInventory.getStack(i);
            if (itemStack.isEmpty()) continue;
            if (itemStack.getItem() instanceof ActionPageItem page) {
                if (typePage == null || !page.getCompatibleTypes(itemStack).contains(typePage)) {
                    return false;
                }
                if (actionPage != null) {
                    return false;
                }
                actionPage = page;
                pages.add(page);
            }
        }

        // Get attribute pages and check if all pages are compatible.
        for (int i = 0; i < craftingInventory.size(); ++i) {
            var itemStack = craftingInventory.getStack(i);
            if (itemStack.isEmpty()) continue;
            if (itemStack.getItem() instanceof AttributePageItem page) {
                if (typePage == null || !page.getCompatibleTypes(itemStack).contains(typePage)) {
                    return false;
                }
                var compatibleActions = page.getCompatibleActions(itemStack);
                if (compatibleActions != null && (actionPage == null || !compatibleActions.contains(actionPage))) {
                    return false;
                }
                var incompatiblePages = page.getIncompatiblePages(itemStack);
                if (!page.bookCanHaveMultiple(itemStack) && (pages.contains(page) ||
                        pages.stream().anyMatch(i1 -> incompatiblePages.stream().anyMatch(i1::equals)))) {
                    return false;
                }
                pages.add(page);
            }
        }

        // Return true if all requirements are met.
        return binding && catalyst > 0 && typePage != null && pages.size() <= catalyst;
    }

    @Override
    public ItemStack craft(RecipeInputInventory craftingInventory, DynamicRegistryManager registryManager) {
        var book = new ItemStack(ModItems.MYSTICAL_BOOK);
        var nbt = book.getOrCreateNbt();
        var typeColor = -1;
        var otherColors = new ArrayList<Integer>();

        for (int i = 0; i < craftingInventory.size(); ++i) {
            var stack = craftingInventory.getStack(i);
            if (CATALYST.test(stack)) {
                nbt.putString(MysticalBookItem.CATALYST_TAG, Registries.ITEM.getId(stack.getItem()).toString());
                break;
            }
        }

        for (int i = 0; i < craftingInventory.size(); ++i) {
            var stack = craftingInventory.getStack(i);
            if (stack.getItem() instanceof TypePageItem pageItem) {
                pageItem.onCraftToBook(stack, book);
                typeColor = pageItem.getColor();
                if (pageItem.mixColor(stack)) otherColors.add(typeColor);
                nbt.put(MysticalBookItem.TYPE_PAGE_TAG, NbtString.of(Registries.ITEM.getId(pageItem).toString()));
                break;
            }
        }

        var pagesList = nbt.getList(MysticalBookItem.ATTRIBUTE_PAGES_TAG, NbtElement.STRING_TYPE);
        for (int i = 0; i < craftingInventory.size(); ++i) {
            var stack = craftingInventory.getStack(i);
            if (stack.getItem() instanceof AttributePageItem pageItem) {
                pageItem.onCraftToBook(stack, book);
                otherColors.add(pageItem.getColor());
                pagesList.add(NbtString.of(Registries.ITEM.getId(pageItem).toString()));
            }
        }
        nbt.put(MysticalBookItem.ATTRIBUTE_PAGES_TAG, pagesList);

        for (int i = 0; i < craftingInventory.size(); ++i) {
            var stack = craftingInventory.getStack(i);
            if (stack.getItem() instanceof ActionPageItem pageItem) {
                pageItem.onCraftToBook(stack, book);
                otherColors.add(pageItem.getColor());
                nbt.put(MysticalBookItem.ACTION_PAGE_TAG, NbtString.of(Registries.ITEM.getId(pageItem).toString()));
                break;
            }
        }

        if (otherColors.isEmpty()) {
            ((MysticalBookItem) book.getItem()).setColor(book, typeColor);
        } else {
            ((MysticalBookItem) book.getItem()).setColor(book, Colors.mixColors(otherColors));
        }

        return book;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return DefaultedList.copyOf(
                Ingredient.EMPTY, BINDING, CATALYST, TYPE_PAGES, ATTRIBUTE_PAGES, ATTRIBUTE_PAGES, ACTION_PAGES
        );
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getResult(DynamicRegistryManager registryManager) {
        return new ItemStack(ModItems.MYSTICAL_BOOK);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.MYSTICAL_BOOK;
    }
}
