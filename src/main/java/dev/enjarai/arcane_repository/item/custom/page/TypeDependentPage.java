package dev.enjarai.arcane_repository.item.custom.page;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface TypeDependentPage {
    List<TypePageItem> getCompatibleTypes(ItemStack page);
}
