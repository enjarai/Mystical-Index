package net.messer.mystical_index.util.request;

import com.google.common.collect.ImmutableList;
import net.messer.mystical_index.item.custom.book.MysticalBookItem;
import net.messer.mystical_index.item.custom.page.type.ItemStorageTypePage;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class ExtractionRequest extends QueryBasedRequest {

    private final List<String[]> expressions;
    private final BiFunction<Integer, Item, Integer> amountModifier;
    private Item match;
    private List<ItemStack> stacks = List.of();

    protected ExtractionRequest(String itemQuery, int amount, BiFunction<Integer, Item, Integer> amountModifier) {
        super(amount, itemQuery);

        this.expressions = Stream.of(itemQuery, "*" + itemQuery + "*")
                .map(string -> string.replace(' ', '_').split("\\*+", -1))
                .toList();

        this.amountModifier = amountModifier;
        this.totalPasses = expressions.size();
    }

    public void apply(LibraryIndex index) {
        ImmutableList.Builder<ItemStack> builder = ImmutableList.builder();

        do {
            for (IndexSource source : index.getSources()) {
                if (isSatisfied()) break;

                var book = source.getBook();
                if (book.getItem() instanceof MysticalBookItem bookItem) {
                    if (bookItem.getTypePage(book) instanceof ItemStorageTypePage page) {
                        builder.addAll(page.extractItems(book, this, shouldApply()));

                        runBlockAffectedCallback(source.getBlockEntity());
                    }
                }
            }
        } while (shouldDoAnotherPass());

        stacks = builder.build();
    }

    public boolean hasMatched() {
        return match != null;
    }

    public boolean matches(Item matchItem) {
        if (hasMatched()) {
            return match == matchItem;
        } else {
            var expression = expressions.get(passesCompleted);
            boolean matched = itemMatchesExpression(expression, matchItem);

            if (matched) {
                match = matchItem;
                amountUnsatisfied = amountModifier.apply(amountUnsatisfied, matchItem);
            }

            return matched;
        }
    }

    @Override
    public void satisfy(int amount) {
        if (!hasMatched()) throw new IllegalStateException("Can't satisfy before a match has been found.");
        super.satisfy(amount);
    }

    @Override
    public int getAmountUnsatisfied() {
        if (!hasMatched()) throw new IllegalStateException("Can't get amount before a match has been found.");
        return super.getAmountUnsatisfied();
    }

    public Item getMatchedItem() {
        if (!hasMatched()) throw new IllegalStateException("Can't get matched item before a match has been found.");

        return match;
    }

    @Override
    public Text getMessage() {
        if (hasMatched()) {
            return Text.translatable("chat.mystical_index.extracted", getTotalAmountAffected(), getMatchedItem().getName().getString());
        }
        return Text.translatable("chat.mystical_index.no_match", getFullQuery());
    }

    @Override
    public List<ItemStack> getReturnedStacks() {
        return stacks;
    }

}
