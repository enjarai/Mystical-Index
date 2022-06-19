package net.messer.mystical_index.item.custom.page.action;

import net.messer.mystical_index.item.custom.book.MysticalBookItem;
import net.messer.mystical_index.item.custom.page.ActionPageItem;
import net.messer.mystical_index.item.custom.page.TypePageItem;
import net.messer.mystical_index.item.custom.page.type.BlockStorageTypePage;
import net.messer.mystical_index.util.ProxiedItemPlacementContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

import java.util.List;

import static net.messer.mystical_index.item.ModItems.BLOCK_STORAGE_TYPE_PAGE;

public class BuildingActionPage extends ActionPageItem {
    public BuildingActionPage(String id) {
        super(id);
    }

    @Override
    public int getColor() {
        return 0x993333;
    }

    @Override
    public List<TypePageItem> getCompatibleTypes(ItemStack page) {
        return List.of(BLOCK_STORAGE_TYPE_PAGE);
    }

    @Override
    public ActionResult book$useOnBlock(ItemUsageContext context) {
        var book = context.getStack();
        var bookItem = (MysticalBookItem) book.getItem();

        if (bookItem.getTypePage(book) instanceof BlockStorageTypePage page) {
            var stack = page.tryRemoveFirstStack(
                    book, 1,
                    itemStack -> itemStack.getItem() instanceof BlockItem
            );
            if (stack.isPresent()) {
                var placeStack = stack.get();
                var blockItem = (BlockItem) placeStack.getItem();
                var itemContext = new ProxiedItemPlacementContext(context, placeStack);

                return blockItem.place(itemContext);
            }
        }

        return ActionResult.FAIL;
    }
}
