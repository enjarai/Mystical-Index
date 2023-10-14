package dev.enjarai.arcane_repository.item.custom.page.attribute;

import dev.enjarai.arcane_repository.item.custom.page.TypePageItem;
import dev.enjarai.arcane_repository.item.custom.page.type.IndexingTypePage;
import net.minecraft.item.ItemStack;

import java.util.List;

import static dev.enjarai.arcane_repository.item.ModItems.INDEXING_TYPE_PAGE;
import static dev.enjarai.arcane_repository.item.ModItems.INDEX_SLAVE_TYPE_PAGE;

public class RangePage extends IndexingTypePage.IndexingAttributePage {
    public RangePage(String id) {
        super(id);
    }

    @Override
    public double getRangeMultiplier(ItemStack page, boolean linked) {
        return 2;
    }

    @Override
    public List<TypePageItem> getCompatibleTypes(ItemStack page) {
        return List.of(INDEXING_TYPE_PAGE, INDEX_SLAVE_TYPE_PAGE);
    }

    @Override
    public int getColor() {
        return 0xff2222;
    }
}
