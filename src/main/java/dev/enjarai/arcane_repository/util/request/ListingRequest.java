package dev.enjarai.arcane_repository.util.request;

import dev.enjarai.arcane_repository.item.custom.book.MysticalBookItem;
import dev.enjarai.arcane_repository.item.custom.page.type.ItemStorageTypePage;
import dev.enjarai.arcane_repository.util.BigStack;
import dev.enjarai.arcane_repository.util.ContentsIndex;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;

public class ListingRequest extends QueryBasedRequest {
    private final String[] expression;
    private ContentsIndex stacks = new ContentsIndex();

    protected ListingRequest(int amount, String itemQuery) {
        super(amount, itemQuery);
        expression = ("*" + itemQuery + "*").replace(' ', '_').split("\\*+", -1);
    }

    @Override
    public Text getMessage() {
        var result = Text.translatable("chat.arcane_repository.listing");
        for (BigStack stack : stacks.getAll()) {
            result.append(Text.literal("\n  ")
                    .append(stack.getAmount() + "x ")
                    .append(stack.getItemStack().getName())
                    .styled(style -> style
                            .withClickEvent(new ClickEvent(
                                    ClickEvent.Action.RUN_COMMAND,
                                    stack.getAmount() + " " + stack.getItemStack().getName()
                            ))
                            .withUnderline(true)
                    )
            );
        }
        if (stacks.getAll().size() < 1) {
            result
                    .append("\n  ")
                    .append(Text.translatable("chat.arcane_repository.no_results"));
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
