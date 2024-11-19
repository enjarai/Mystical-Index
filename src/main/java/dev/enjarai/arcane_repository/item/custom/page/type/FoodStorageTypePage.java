package dev.enjarai.arcane_repository.item.custom.page.type;

import dev.enjarai.arcane_repository.item.custom.page.TypePageItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;

import java.util.List;

import static dev.enjarai.arcane_repository.item.ModItems.FOOD_STORAGE_TYPE_PAGE;

public class FoodStorageTypePage extends ItemStorageTypePage {
    public FoodStorageTypePage(String id) {
        super(id);
    }

    @Override
    public int getColor() {
        return 0x008811;
    }

    @Override
    public MutableText getTypeDisplayName() {
        return super.getTypeDisplayName().formatted(Formatting.DARK_GREEN);
    }

    @Override
    protected int getBaseInsertPriority(ItemStack book) {
        return 2;
    }

    @Override
    protected boolean canInsert(ItemStack book, ItemStack itemStack) {
        if (itemStack.get(DataComponentTypes.FOOD) == null) return false;

        return super.canInsert(book, itemStack);
    }

    public static abstract class FoodStorageAttributePage extends ItemStorageAttributePage {
        public FoodStorageAttributePage(String id) {
            super(id);
        }

        @Override
        public List<TypePageItem> getCompatibleTypes(ItemStack page) {
            return List.of(FOOD_STORAGE_TYPE_PAGE);
        }
    }
}
