package dev.enjarai.arcane_repository.item.custom.page.attribute;

import dev.enjarai.arcane_repository.item.custom.page.AttributePageItem;
import dev.enjarai.arcane_repository.item.custom.page.TypePageItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.List;

import static dev.enjarai.arcane_repository.item.ModItems.*;

public class PickupAttributePage extends AttributePageItem {
    public PickupAttributePage(String id) {
        super(id);
    }

    @Override
    public void appendAttributes(ItemStack page, NbtCompound nbt) {

    }

    @Override
    public int getColor() {
        return 0x00884c;
    }

    @Override
    public List<TypePageItem> getCompatibleTypes(ItemStack page) {
        return List.of(ITEM_STORAGE_TYPE_PAGE, FOOD_STORAGE_TYPE_PAGE, BLOCK_STORAGE_TYPE_PAGE);
    }

    @Override
    public boolean bookCanHaveMultiple(ItemStack page) {
        return false;
    }
}
