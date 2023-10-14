package dev.enjarai.arcane_repository.util.request;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.Vec3d;

import java.util.function.BiConsumer;

public abstract class Request {
    protected int totalPasses = 1;
    protected int amountUnsatisfied;
    protected int amountAffected = 0;
    protected int passesCompleted = 0;
    private int lastCheck = 0;
    private BiConsumer<Request, BlockEntity> blockAffectedCallback;
    private Vec3d sourcePosition;
    private boolean shouldApply = true;

    public Request(int amount) {
        this.amountUnsatisfied = amount;
    }

    public abstract void apply(LibraryIndex index);

    public void satisfy(int amount) {
        if (amountUnsatisfied != -1) {
            amountUnsatisfied -= amount;
        }
        amountAffected += amount;
    }

    public boolean isSatisfied() {
        return amountUnsatisfied <= 0;
    }

    public int getAmountUnsatisfied() {
        return amountUnsatisfied;
    }

    public int getTotalAmountAffected() {
        return amountAffected;
    }

    public int getAmountAffected() {
        int amount = amountAffected - lastCheck;
        lastCheck = amountAffected;
        return amount;
    }

    public boolean hasAffected() {
        return amountAffected > 0;
    }

    public void setSourcePosition(Vec3d position) {
        sourcePosition = position;
    }

    public Vec3d getSourcePosition() {
        return sourcePosition;
    }

    public void setBlockAffectedCallback(BiConsumer<Request, BlockEntity> callback) {
        this.blockAffectedCallback = callback;
    }

    public void runBlockAffectedCallback(BlockEntity blockEntity) {
        if (sourcePosition == null || blockAffectedCallback == null) return;
        this.blockAffectedCallback.accept(this, blockEntity);
    }

    public void setShouldApply(boolean shouldApply) {
        this.shouldApply = shouldApply;
    }

    public boolean shouldApply() {
        return shouldApply;
    }

    public boolean shouldDoAnotherPass() {
        passesCompleted++;
        return passesCompleted < totalPasses && !isSatisfied();
    }
}
