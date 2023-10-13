package net.messer.mystical_index.block;

import net.messer.mystical_index.MysticalIndex;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModTags {
    //public static final Tag<Block> INDEX_INTRACTABLE = TagFactory.BLOCK.create(new Identifier(MysticalIndex.MOD_ID, "index_intractable"));
    public static final TagKey<Block> INDEX_INTRACTABLE = TagKey.of(RegistryKeys.BLOCK, MysticalIndex.id("index_intractable"));
}
