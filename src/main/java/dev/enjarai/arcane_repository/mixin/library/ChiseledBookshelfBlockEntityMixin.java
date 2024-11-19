package dev.enjarai.arcane_repository.mixin.library;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import dev.enjarai.arcane_repository.item.custom.book.MysticalBookItem;
import dev.enjarai.arcane_repository.item.custom.page.type.ItemStorageTypePage;
import dev.enjarai.arcane_repository.util.ModifiedChiseledBookshelfBlockEntity;
import dev.enjarai.arcane_repository.util.request.IndexInteractable;
import dev.enjarai.arcane_repository.util.request.IndexSource;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Set;

@Mixin(ChiseledBookshelfBlockEntity.class)
public abstract class ChiseledBookshelfBlockEntityMixin extends BlockEntity implements IndexInteractable, ModifiedChiseledBookshelfBlockEntity {
    public ChiseledBookshelfBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Shadow public abstract int size();
    @Shadow public abstract ItemStack getStack(int slot);

    float elapsed = 0f;
    int lastSlot = -1;
    BlockPos lastHitPos = BlockPos.ORIGIN;

    @Override
    public Set<IndexSource> getSources() {
        ImmutableSet.Builder<IndexSource> builder = ImmutableSet.builder();

        for (int i = 0; i < size(); i++) {
            var book = getStack(i);
            if (book.getItem() instanceof MysticalBookItem mysticalBookItem) {
                if (mysticalBookItem.getTypePage(book) instanceof ItemStorageTypePage) {
                    builder.add(new IndexSource(book, this));
                }
            }
        }

        return builder.build();
    }

    @Override
    public void onInteractionComplete() {
        // Send new state to clients if applicable
        if (getWorld() != null) {
            getWorld().updateListeners(
                    getPos(), getCachedState(),
                    getCachedState(), Block.NOTIFY_LISTENERS
            );
        }
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    @Override
    public float getElapsed() {
        return elapsed;
    }

    @Override
    public void setElapsed(float elapsed) {
        this.elapsed = elapsed;
    }

    @Override
    public int getLastSlot() {
        return lastSlot;
    }

    @Override
    public void setLastSlot(int lastSlot) {
        this.lastSlot = lastSlot;
    }

    @Override
    public BlockPos getLastHitPos() {
        return lastHitPos;
    }

    @Override
    public void setLastHitPos(BlockPos lastHitPos) {
        this.lastHitPos = lastHitPos;
    }
}
