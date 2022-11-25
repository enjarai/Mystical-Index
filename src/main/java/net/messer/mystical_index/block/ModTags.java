package net.messer.mystical_index.block;

import net.messer.mystical_index.MysticalIndex;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;

public class ModTags {
    public static final TagKey<Block> INDEX_INTRACTABLE = TagKey.of(Registries.BLOCK.getKey(), MysticalIndex.id("index_intractable"));
}
