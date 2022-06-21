package net.messer.mystical_index.item.custom.page.type;

import net.messer.mystical_index.block.entity.MysticalLecternBlockEntity;
import net.messer.mystical_index.item.custom.book.MysticalBookItem;
import net.messer.mystical_index.util.request.LibraryIndex;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static net.messer.mystical_index.block.ModBlocks.MYSTICAL_LECTERN;

public class IndexSlaveTypePage extends IndexingTypePage {
    public IndexSlaveTypePage(String id) {
        super(id);
    }

    @Override
    public int getColor() {
        return 0x225555;
    }

    @Override
    public boolean mixColor(ItemStack stack) {
        return true;
    }

    @Override
    public int getMaxLinks(ItemStack book) {
        return 1;
    }

    @Override
    public boolean isLinkableBlock(ItemStack book, BlockState state) {
        return state.isOf(MYSTICAL_LECTERN);
    }

    @Override
    public LibraryIndex getIndex(ItemStack book, World world, BlockPos pos) {
        var nbt = book.getOrCreateNbt();
        var links = nbt.getList(LINKED_BLOCKS_TAG, NbtElement.LIST_TYPE);

        if (links.isEmpty()) return LibraryIndex.EMPTY;
        var master = links.getList(0);

        if (master.size() == 3) {
            var masterPos = blockPosFromList(master);
            var masterBe = world.getBlockEntity(masterPos);

            if (masterBe instanceof MysticalLecternBlockEntity masterLectern) {
                var masterBook = masterLectern.getBook();
                var masterBookItem = (MysticalBookItem) masterBook.getItem();
                var masterPage = masterBookItem.getTypePage(masterBook);

                if (masterPage instanceof IndexingTypePage masterIndexingPage) {
                    return masterIndexingPage.getLecternIndex(masterLectern);
                }
            }
        }

        return LibraryIndex.EMPTY;
    }
}
