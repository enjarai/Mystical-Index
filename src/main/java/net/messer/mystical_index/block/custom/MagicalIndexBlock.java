package net.messer.mystical_index.block.custom;

import net.messer.mystical_index.MysticalIndex;
import net.messer.mystical_index.block.entity.LibraryBlockEntity;
import net.messer.mystical_index.block.entity.ModBlockEntities;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MagicalIndexBlock extends Block{
    public MagicalIndexBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);

        if (world.isClient) return;

        var startPos = new BlockPos.Mutable(0,0,0);

        for(int x = -5; x <= 10 ;x++){
            for(int y = -5; y <= 10 ;y++){
                for(int z = -5; z <= 10 ;z++){
                    var blockEntity = world.getBlockEntity(startPos.set(pos.getX() + x,pos.getY() + y,pos.getZ() + z));
                    if(blockEntity == null) continue;

                    if(blockEntity.getType() == ModBlockEntities.LIBRARY_BLOCK_ENTITY){
                        LibraryBlockEntity libraryBlockEntity = (LibraryBlockEntity)blockEntity;
                        var storedItems = libraryBlockEntity.getItems();
                        for(ItemStack item: storedItems){
                            MysticalIndex.LOGGER.info(item.getItem().toString());
                        }
                    }
                }
            }
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        if (world.isClient) return ActionResult.SUCCESS;

        var startPos = new BlockPos.Mutable(0,0,0);

        for(int x = -5; x <= 10 ;x++){
            for(int y = -5; y <= 10 ;y++){
                for(int z = -5; z <= 10 ;z++){
                    var blockEntity = world.getBlockEntity(startPos.set(pos.getX() + x,pos.getY() + y,pos.getZ() + z));
                    if(blockEntity == null) continue;

                    if(blockEntity.getType() == ModBlockEntities.LIBRARY_BLOCK_ENTITY){
                        LibraryBlockEntity libraryBlockEntity = (LibraryBlockEntity)blockEntity;
                        var storedItems = libraryBlockEntity.getItems();
                        for(ItemStack item: storedItems){
                            MysticalIndex.LOGGER.info(item.getItem().toString());
                        }
                    }
                }
            }
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }
}
