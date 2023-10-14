package net.messer.mystical_index.util;

import net.minecraft.util.math.BlockPos;

public interface ModifiedChiseledBookshelfBlockEntity {
    float getElapsed();

    void setElapsed(float elapsed);

    int getLastSlot();

    void setLastSlot(int lastSlot);

    BlockPos getLastHitPos();

    void setLastHitPos(BlockPos lastHitPos);
}
