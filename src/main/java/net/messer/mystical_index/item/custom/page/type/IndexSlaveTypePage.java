package net.messer.mystical_index.item.custom.page.type;

import net.messer.mystical_index.block.entity.MysticalLecternBlockEntity;
import net.messer.mystical_index.item.custom.book.MysticalBookItem;
import net.messer.mystical_index.util.request.LibraryIndex;
import net.messer.mystical_index.util.state.PageLecternState;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

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
    public boolean hasRangedLinking(ItemStack book) {
        return true;
    }

    @Nullable
    public BlockPos getMasterPos(ItemStack book) {
        var nbt = book.getOrCreateNbt();
        var links = nbt.getList(LINKED_BLOCKS_TAG, NbtElement.LIST_TYPE);

        if (links.isEmpty()) return null;
        var master = links.getList(0);

        if (master.size() != 3) return null;
        return blockPosFromList(master);
    }

    @Nullable
    public LibraryIndex getMasterIndex(ItemStack book, World world, BlockPos pos) {
        var masterPos = getMasterPos(book);

        if (masterPos != null) {
            var masterBe = world.getBlockEntity(masterPos);

            if (masterBe instanceof MysticalLecternBlockEntity masterLectern) {
                var masterBook = masterLectern.getBook();
                var masterBookItem = (MysticalBookItem) masterBook.getItem();
                var masterPage = masterBookItem.getTypePage(masterBook);

                if (masterPage instanceof IndexingTypePage masterIndexingPage && masterLectern.typeState != null) {
                    return masterIndexingPage.getLecternIndex(masterLectern);
                }
            }
        }

        return null;
    }

    @Override
    public LibraryIndex getIndex(ItemStack book, World world, BlockPos pos) {
        var index = getMasterIndex(book, world, pos);

        return index == null ? LibraryIndex.EMPTY : index;
    }
//
//    @Override
//    public LibraryIndex getLecternIndex(MysticalLecternBlockEntity lectern) {
//        return getIndex(lectern.getBook(), lectern.getWorld(), lectern.getPos());
//    }

    public LibraryIndex getMasterLecternIndex(MysticalLecternBlockEntity lectern) {
        return ((IndexSlaveLecternState) lectern.typeState).getMasterIndex();
    }

    @Override
    public LibraryIndex getInteractionLecternIndex(MysticalLecternBlockEntity lectern) {
        return getMasterLecternIndex(lectern);
    }

    @Override
    public PageLecternState lectern$getState(MysticalLecternBlockEntity lectern) {
        return new IndexSlaveLecternState(lectern, this);
    }

    @Override
    public void lectern$onRemoved(PlayerEntity player, MysticalLecternBlockEntity lectern) {
        // Make sure to remove the slave from the master when the book is taken.
        getMasterLecternIndex(lectern).remove(getLecternIndex(lectern));
    }

    @Override
    public void lectern$afterPlaced(MysticalLecternBlockEntity lectern) {
        if (lectern.typeState instanceof IndexSlaveLecternState slaveState) {
            slaveState.loadIndexes(lectern.getBook(), this);
        }
        super.lectern$afterPlaced(lectern);
    }

    //    @Override
//    public void lectern$onPlaced(MysticalLecternBlockEntity lectern) {
//
//
////        var range = getMaxRange(lectern.getBook(), false);
////        var index = LibraryIndex.fromRange(lectern.getWorld(), lectern.getPos(), range);
////
////        getLecternIndex(lectern).merge(index);
//    }

    public static class IndexSlaveLecternState extends PageLecternState {
        private LibraryIndex masterIndex;
        private LibraryIndex index;

        // This also acts as an onLoad method, connecting us to the master and instantiating the local index.
        public IndexSlaveLecternState(MysticalLecternBlockEntity lectern, IndexSlaveTypePage page) {
            super(lectern);
        }

        @Override
        public void onUnload() {
            // Remove the slave from the master when the lectern is unloaded.
            masterIndex.remove(index);
        }

        public void loadIndexes(ItemStack book, IndexSlaveTypePage page) {
            var world = lectern.getWorld();
            var pos = lectern.getPos();
            var range = page.getMaxRange(book, false);
            var index = LibraryIndex.fromRange(world, pos, range);
            var master = page.getMasterIndex(book, world, pos);

            if (master == null) {
                masterIndex = LibraryIndex.EMPTY;
                this.index = LibraryIndex.EMPTY;
            } else {
                masterIndex = master;
                this.index = index;
                masterIndex.add(index);
            }
        }

        public LibraryIndex getIndex() {
            return index;
        }

        public LibraryIndex getMasterIndex() {
            return masterIndex;
        }
    }
}
