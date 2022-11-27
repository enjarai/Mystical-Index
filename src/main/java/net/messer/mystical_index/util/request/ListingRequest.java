package net.messer.mystical_index.util.request;

import com.google.common.collect.ImmutableList;
import net.messer.mystical_index.item.custom.book.MysticalBookItem;
import net.messer.mystical_index.item.custom.page.type.ItemStorageTypePage;
import net.messer.mystical_index.util.BigStack;
import net.messer.mystical_index.util.ContentsIndex;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ListingRequest extends QueryBasedRequest {
    private final String[] expression;
    private ContentsIndex stacks = new ContentsIndex();

    protected ListingRequest(int amount, String itemQuery) {
        super(amount, itemQuery);
        expression = ("*" + itemQuery + "*").replace(' ', '_').split("\\*+", -1);
    }

    @Override
    public Text getMessage() {
        var result = Text.translatable("chat.mystical_index.listing");
        for (BigStack stack : stacks.getAll()) {
            result
                    .append("\n  ")
                    .append(stack.getAmount() + "x ")
                    .append(stack.getItemStack().getName());
        }
        if (stacks.getAll().size() < 1) {
            result
                    .append("\n  ")
                    .append(Text.translatable("chat.mystical_index.no_results"));
        }
        return result;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void apply(LibraryIndex index) {
        var result = new ContentsIndex();

        do {
            for (IndexSource source : index.getSources()) {
                if (isSatisfied()) break;

                var book = source.getBook();
                if (book.getItem() instanceof MysticalBookItem bookItem) {
                    if (bookItem.getTypePage(book) instanceof ItemStorageTypePage page) {
                        result.merge(page.getContents(book).stream().filter(this::matches));

                        runBlockAffectedCallback(source.getBlockEntity());
                    }
                }
            }
        } while (shouldDoAnotherPass());

        stacks = result;
    }

    private boolean matches(BigStack stack) {
        return itemMatchesExpression(expression, stack.getItem());
    }
}
