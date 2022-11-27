package net.messer.mystical_index.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BigStack {
    private final ItemStack itemStack;
    private int amount;

    public BigStack(ItemStack itemStack) {
        this(itemStack, itemStack.getCount());
    }

    public BigStack(ItemStack itemStack, int amount) {
        itemStack.setCount(1);
        this.itemStack = itemStack;
        this.amount = amount;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public int getAmount() {
        return amount;
    }

    public Item getItem() {
        return itemStack.getItem();
    }

    public boolean canCombine(BigStack other) {
        return canCombine(other.getItemStack());
    }

    public boolean canCombine(ItemStack other) {
        return ItemStack.canCombine(itemStack, other);
    }

    public void increment(int amount) {
        this.amount += amount;
    }

    public List<ItemStack> toStacks() {
        var maxCount = itemStack.getItem().getMaxCount();
        var iterations = amount / maxCount;
        var remainder = amount % maxCount;
        var stacks = new ArrayList<ItemStack>();

        for (int i = 0; i < iterations; i++) {
            var stack = itemStack.copy();
            stack.setCount(maxCount);
            stacks.add(stack);
        }
        if (remainder > 0) {
            var stack = itemStack.copy();
            stack.setCount(remainder);
            stacks.add(stack);
        }

        return stacks;
    }
}
