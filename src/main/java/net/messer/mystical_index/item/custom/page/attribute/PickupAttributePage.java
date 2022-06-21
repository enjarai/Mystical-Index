package net.messer.mystical_index.item.custom.page.attribute;

import net.messer.mystical_index.item.ModItems;
import net.messer.mystical_index.item.custom.page.AttributePageItem;
import net.messer.mystical_index.item.custom.page.TypePageItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.List;

public class PickupAttributePage extends AttributePageItem {
    public PickupAttributePage(String id) {
        super(id);
    }

    @Override
    public void appendAttributes(ItemStack page, NbtCompound nbt) {

    }

    @Override
    public int getColor() {
        return 0;
    }

    @Override
    public List<TypePageItem> getCompatibleTypes(ItemStack page) {
        return List.of(ModItems.ITEM_STORAGE_TYPE_PAGE);
    }
}
