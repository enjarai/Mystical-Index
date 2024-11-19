package dev.enjarai.arcane_repository.item;

import net.minecraft.item.Item;
import net.minecraft.util.Rarity;

public class ItemSettings extends Item.Settings {
    protected boolean rarityChanged = false;

    @Override
    public Item.Settings rarity(Rarity rarity) {
        if (rarityChanged) return this;
        rarityChanged = true;
        return super.rarity(rarity);
    }
}
