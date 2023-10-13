package net.messer.mystical_index.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.messer.mystical_index.MysticalIndex;
import net.messer.mystical_index.block.custom.LibraryBlock;
import net.messer.mystical_index.block.custom.MysticalLecternBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static net.messer.mystical_index.item.ModItems.MYSTICAL_INDEX_GROUP;

public class ModBlocks {

    public static final Block LIBRARY = registerBlock("library",
            new LibraryBlock(FabricBlockSettings.create().strength(1.5f)), true);

    public static final Block MYSTICAL_LECTERN = registerBlock("mystical_lectern",
            new MysticalLecternBlock(FabricBlockSettings.create().strength(1.5f)), false);


    private static Block registerBlock(String name, Block block, boolean hasItem) {
        if (hasItem) {
            registerBlockItem(name, block);
        }
        return Registry.register(Registries.BLOCK, new Identifier(MysticalIndex.MOD_ID, name), block);
    }

    private static BlockItem registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(MysticalIndex.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings().maxCount(64)));
    }

    public static void registerModBlocks(){
        MysticalIndex.LOGGER.info("Registering blocks for " + MysticalIndex.MOD_ID);
    }
}
