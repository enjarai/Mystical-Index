package dev.enjarai.arcane_repository.block;

import dev.enjarai.arcane_repository.block.entity.MysticalLecternBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import dev.enjarai.arcane_repository.ArcaneRepository;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModBlockEntities {

    public static BlockEntityType<MysticalLecternBlockEntity> MYSTICAL_LECTERN_BLOCK_ENTITY;

    public static void registerBlockEntities() {
        MYSTICAL_LECTERN_BLOCK_ENTITY =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, ArcaneRepository.id("mystical_lectern"),
                        FabricBlockEntityTypeBuilder.create(MysticalLecternBlockEntity::new,
                                ModBlocks.MYSTICAL_LECTERN).build(null));
    }
}
