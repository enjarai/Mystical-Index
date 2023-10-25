package dev.enjarai.arcane_repository.util.request;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import dev.enjarai.arcane_repository.block.ModTags;
import dev.enjarai.arcane_repository.util.WorldEffects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class LibraryIndex implements IndexInteractable {
    public static final LibraryIndex EMPTY = new LibraryIndex();

    private final Set<IndexInteractable> interactables;

    public LibraryIndex() {
        this.interactables = new HashSet<>();
    }

    public boolean isEmpty() {
        return interactables.isEmpty();
    }

    public static LibraryIndex fromRange(World world, BlockPos pos, int searchRange) {
        return fromRange(world, pos, searchRange, true);
    }

    public static LibraryIndex fromRange(World world, BlockPos pos, int searchRange, boolean particles) {
        // Iterate over nearby blocks and generate the index
        var result = new LibraryIndex();
        for (int x = -searchRange; x <= searchRange; x++) {
            for (int z = -searchRange; z <= searchRange; z++) {
                for (int y = -searchRange; y <= searchRange; y++) {
                    BlockPos testPos = pos.add(x, y, z);

                    if (world.getBlockState(testPos).isIn(ModTags.INDEX_INTRACTABLE) &&
                            world.getBlockEntity(testPos) instanceof IndexInteractable entity) {
                        result.add(entity, particles ? WorldEffects::registrationParticles : i -> {});
                    }
                }
            }
        }

        return result;
    }

    public void add(IndexInteractable interactable) {
        add(interactable, i -> {});
    }

    public void add(IndexInteractable interactable, Consumer<IndexInteractable> callback) {
        interactables.add(interactable);
        callback.accept(interactable);
    }

    public boolean remove(IndexInteractable interactable) {
        return interactables.remove(interactable);
    }

    public void merge(LibraryIndex other) {
        interactables.addAll(other.interactables);
    }

    @Override
    public Set<IndexSource> getSources() {
        ImmutableSet.Builder<IndexSource> builder = ImmutableSet.builder();

        for (IndexInteractable entity : interactables) {
            builder.addAll(entity.getSources());
        }

        return builder.build();
    }
}
