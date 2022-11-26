package net.messer.mystical_index.block;

import eu.pb4.polymer.api.block.PolymerBlockUtils;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.messer.mystical_index.MysticalIndex;
import net.messer.mystical_index.block.entity.LibraryBlockEntity;
import net.messer.mystical_index.block.entity.MysticalLecternBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModBlockEntities {

    public static BlockEntityType<MysticalLecternBlockEntity> MYSTICAL_LECTERN_BLOCK_ENTITY;
//    public static BlockEntityType<LibraryBlockEntity> LIBRARY_BLOCK_ENTITY;

    public static void registerBlockEntities() {
//        LIBRARY_BLOCK_ENTITY =
//                Registry.register(Registries.BLOCK_ENTITY_TYPE, MysticalIndex.id("library"),
//                        FabricBlockEntityTypeBuilder.create(LibraryBlockEntity::new,
//                                ModBlocks.LIBRARY).build(null));

        MYSTICAL_LECTERN_BLOCK_ENTITY =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, MysticalIndex.id("mystical_lectern"),
                        FabricBlockEntityTypeBuilder.create(MysticalLecternBlockEntity::new,
                                ModBlocks.MYSTICAL_LECTERN).build(null));

        PolymerBlockUtils.registerBlockEntity(MYSTICAL_LECTERN_BLOCK_ENTITY);
    }
}
