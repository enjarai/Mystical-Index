package dev.enjarai.arcane_repository.block.custom;

import dev.enjarai.arcane_repository.item.custom.book.MysticalBookItem;
import dev.enjarai.arcane_repository.block.ModBlockEntities;
import dev.enjarai.arcane_repository.block.entity.MysticalLecternBlockEntity;
import dev.enjarai.arcane_repository.client.Particles;
import dev.enjarai.arcane_repository.util.LecternTracker;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LecternBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class MysticalLecternBlock extends LecternBlock {
    private static final VoxelShape LECTERN_INSIDE_SHAPE = Block.createCuboidShape(2.0, 11.0, 2.0, 14.0, 16.0, 14.0);
    private static final VoxelShape LECTERN_ABOVE_SHAPE = Block.createCuboidShape(0.0, 16.0, 0.0, 16.0, 32.0, 16.0);
    public static final VoxelShape LECTERN_INPUT_AREA_SHAPE = VoxelShapes.union(LECTERN_INSIDE_SHAPE, LECTERN_ABOVE_SHAPE);

    public MysticalLecternBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        if (blockEntity instanceof MysticalLecternBlockEntity lectern) {
            LecternTracker.removeIndexLectern(lectern);

            if (state.get(HAS_BOOK) && lectern.getBook().getItem() instanceof MysticalBookItem book) {
                book.lectern$onRemoved(player, lectern);
            }
        }

        super.afterBreak(world, player, pos, state, blockEntity, stack);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (world.getBlockEntity(pos) instanceof MysticalLecternBlockEntity lectern) {
            if (state.get(HAS_BOOK) && lectern.getBook().getItem() instanceof MysticalBookItem book) {
                book.lectern$onEntityCollision(lectern, state, world, pos, entity);
            }
        }
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!player.isSneaking()) {
            if (world.getBlockEntity(pos) instanceof MysticalLecternBlockEntity lectern) {
                if (state.get(HAS_BOOK) && lectern.getBook().getItem() instanceof MysticalBookItem book) {
                    var result = book.lectern$onUse(lectern, state, world, pos, player, hit);
                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
            }
        } else {
            if (state.get(HAS_BOOK)) {
                var blockEntity = world.getBlockEntity(pos);
                if (blockEntity instanceof MysticalLecternBlockEntity lecternBlockEntity) {
                    var book = lecternBlockEntity.getBook();
                    player.getInventory().offerOrDrop(book);

                    if (book.getItem() instanceof MysticalBookItem bookItem) {
                        bookItem.lectern$onRemoved(player, lecternBlockEntity);
                    }

                    world.setBlockState(pos, Blocks.LECTERN.getStateWithProperties(state).with(LecternBlock.HAS_BOOK, false));

                    return ActionResult.success(world.isClient());
                }
                return ActionResult.CONSUME;
            }
        }
        return ActionResult.CONSUME;
    }

    @Override //TODO: check this
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!player.isSneaking()) {
            if (world.getBlockEntity(pos) instanceof MysticalLecternBlockEntity lectern) {
                if (state.get(HAS_BOOK) && lectern.getBook().getItem() instanceof MysticalBookItem book) {
                    var result = book.lectern$onUseWithItem(lectern, stack, state, world, pos, player, hit);
                    if (result != ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION) {
                        return result;
                    }
                }
            }
        } else {
            if (state.get(HAS_BOOK)) {
                var blockEntity = world.getBlockEntity(pos);
                if (blockEntity instanceof MysticalLecternBlockEntity lecternBlockEntity) {
                    var book = lecternBlockEntity.getBook();
                    player.getInventory().offerOrDrop(book);

                    if (book.getItem() instanceof MysticalBookItem bookItem) {
                        bookItem.lectern$onRemoved(player, lecternBlockEntity);
                    }

                    world.setBlockState(pos, Blocks.LECTERN.getStateWithProperties(state).with(LecternBlock.HAS_BOOK, false));

                    return ItemActionResult.success(world.isClient());
                }
                return ItemActionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
            }
        }
        return ItemActionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MysticalLecternBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, ModBlockEntities.MYSTICAL_LECTERN_BLOCK_ENTITY, MysticalLecternBlockEntity::serverTick);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);

        Particles.spawnParticles(
                world, Vec3d.ofCenter(pos).add(0, 0.7, 0), ParticleTypes.SOUL_FIRE_FLAME,
                0.3, 0.3, 0.3, UniformIntProvider.create(1, 1), 0);
    }

    public static boolean putBookIfAbsent(@Nullable PlayerEntity player, World world, BlockPos pos, BlockState state, ItemStack stack) {
        if (!state.get(HAS_BOOK)) {
            if (!world.isClient) {
                putBook(player, world, pos, state, stack);
            }
            return true;
        }
        return false;
    }

    private static void putBook(@Nullable PlayerEntity player, World world, BlockPos pos, BlockState state, ItemStack stack) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof MysticalLecternBlockEntity lectern) {
            lectern.setBook(stack.split(1));
            LecternBlock.setHasBook(player, world, pos, state, true);
            world.playSound(null, pos, SoundEvents.ITEM_BOOK_PUT, SoundCategory.BLOCKS, 1.0f, 1.0f);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            if (lectern.getBook().getItem() instanceof MysticalBookItem book) {
                book.lectern$onPlaced(lectern);
            }
        }
    }
}
