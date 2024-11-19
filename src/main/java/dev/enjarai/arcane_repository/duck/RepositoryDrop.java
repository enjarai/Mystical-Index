package dev.enjarai.arcane_repository.duck;

import net.minecraft.entity.ItemEntity;

public interface RepositoryDrop {
    boolean arcane_repository$isRepositoryDrop();
    void arcane_repository$setRepositoryDrop(boolean isDrop);

    static RepositoryDrop cast(ItemEntity entity) {
        return (RepositoryDrop) entity;
    }
}
