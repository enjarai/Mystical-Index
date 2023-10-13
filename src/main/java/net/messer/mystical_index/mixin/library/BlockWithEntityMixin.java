package net.messer.mystical_index.mixin.library;

import net.minecraft.block.BlockWithEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockWithEntity.class)
public abstract class BlockWithEntityMixin extends BlockMixin {
    public BlockWithEntityMixin(Settings settings) {
        super(settings);
    }
}
