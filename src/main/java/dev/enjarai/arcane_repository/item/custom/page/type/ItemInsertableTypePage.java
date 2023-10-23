package dev.enjarai.arcane_repository.item.custom.page.type;

import dev.enjarai.arcane_repository.block.entity.MysticalLecternBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface ItemInsertableTypePage {
    /**
     * Insert a stack into the book this type page is a part of.
     * Any implementations <b>should</b> mutate the inserted ItemStack.
     * @param book The book stack this type page is a part of.
     * @param player The player interacting with this book.
     * @param insert The stack to be inserted and potentially mutated.
     * @return The amount of items inserted.
     */
    int book$tryInsertItemStack(ItemStack book, PlayerEntity player, ItemStack insert);

    /**
     * Insert a stack into the book this type page is a part of while on a lectern.
     * Any implementations <b>should</b> mutate the inserted ItemStack.
     * @param lectern The lectern this book is on.
     * @param insert The stack to be inserted and potentially mutated.
     * @return The amount of items inserted.
     */
    int lectern$tryInsertItemStack(MysticalLecternBlockEntity lectern, ItemStack insert);
}
