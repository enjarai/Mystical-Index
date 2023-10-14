package dev.enjarai.arcane_repository.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ContentsIndex implements Iterable<BigStack> {
    private final ArrayList<BigStack> contents;

    public ContentsIndex() {
        contents = new ArrayList<>();
    }

    public ContentsIndex(ArrayList<BigStack> contents) {
        this.contents = contents;
    }

    public void merge(ContentsIndex contentsIndex) {
        contentsIndex.contents.forEach(this::add);
    }

    public void merge(Stream<BigStack> stream) {
        stream.forEach(this::add);
    }

    public void add(BigStack bigStack) {
        add(bigStack.getItemStack(), bigStack.getAmount());
    }

    public void add(ItemStack itemStack) {
        add(itemStack, itemStack.getCount());
    }

    public void add(Item item, int amount) {
        add(item.getDefaultStack(), amount);
    }

    public void add(ItemStack item, int amount) {
        for (BigStack content : contents) {
            if (ItemStack.canCombine(content.getItemStack(), item)) {
                content.increment(amount);
                return;
            }
        }
        contents.add(new BigStack(item, amount));
    }

    public List<BigStack> getAll() {
        return contents;
    }

    public List<Text> getTextList() {
        return getTextList(null);
    }

    public List<Text> getTextList(Comparator<BigStack> sorter) {
        Stream<BigStack> stream = getAll().stream();
        if (sorter != null) stream = stream.sorted(sorter);
        return stream
                .map(bigStack -> Text.literal(bigStack.getItemStack().getName().getString() + " x" + bigStack.getAmount()))
                .collect(Collectors.toList());
    }

    public ContentsIndex sorted(Comparator<BigStack> sorter) {
        return new ContentsIndex(new ArrayList<>(contents.stream().sorted(sorter).collect(Collectors.toList())));
    }

    @NotNull
    @Override
    public Iterator<BigStack> iterator() {
        return contents.iterator();
    }

    public Stream<BigStack> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
}
