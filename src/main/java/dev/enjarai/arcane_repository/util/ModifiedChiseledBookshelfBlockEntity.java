package dev.enjarai.arcane_repository.util;

import net.minecraft.util.math.BlockPos;

public interface ModifiedChiseledBookshelfBlockEntity {
    float getElapsed();

    void setElapsed(float elapsed);

    int getLastSlot();

    void setLastSlot(int lastSlot);

    BlockPos getLastHitPos();

    void setLastHitPos(BlockPos lastHitPos);
}
