package net.messer.mystical_index.util.request;

import com.google.common.collect.ImmutableList;
import net.messer.mystical_index.util.WorldEffects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static net.messer.mystical_index.block.ModTags.INDEX_INTRACTABLE;

public class LibraryIndex implements IndexInteractable {
    public static final LibraryIndex EMPTY = new LibraryIndex();
    public final Set<IndexInteractable> interactables;

    public LibraryIndex() {
        this.interactables = new HashSet<>();
    }

    public LibraryIndex(Set<IndexInteractable> interactables) {
        this.interactables = interactables;
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

                    if (world.getBlockState(testPos).isIn(INDEX_INTRACTABLE) &&
                            world.getBlockEntity(testPos) instanceof IndexInteractable entity) {
                        result.add(entity, particles ? WorldEffects::registrationParticles : i -> {});
                    }
                }
            }
        }

        return result;
    }

    public void add(IndexInteractable interactable, Consumer<IndexInteractable> callback) {
        interactables.add(interactable);
        callback.accept(interactable);
    }

    @Override
    public List<IndexSource> getSources() {
        ImmutableList.Builder<IndexSource> builder = ImmutableList.builder();

        for (IndexInteractable entity : interactables) {
            builder.addAll(entity.getSources());
        }

        return builder.build();
    }
}
