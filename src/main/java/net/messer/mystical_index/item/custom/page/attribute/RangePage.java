package net.messer.mystical_index.item.custom.page.attribute;

import net.messer.mystical_index.item.custom.page.TypePageItem;
import net.messer.mystical_index.item.custom.page.type.IndexingTypePage;
import net.minecraft.item.ItemStack;

import java.util.List;

import static net.messer.mystical_index.item.ModItems.INDEXING_TYPE_PAGE;
import static net.messer.mystical_index.item.ModItems.INDEX_SLAVE_TYPE_PAGE;

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
