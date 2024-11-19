package dev.enjarai.arcane_repository.item.custom.page;

import dev.enjarai.arcane_repository.block.entity.MysticalLecternBlockEntity;
import dev.enjarai.arcane_repository.util.state.PageLecternState;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface InteractingPage {
    default TypedActionResult<ItemStack> book$use(World world, PlayerEntity user, Hand hand) {
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    default ActionResult book$useOnBlock(ItemUsageContext context) {
        return ActionResult.PASS;
    }

    default boolean book$onClicked(ItemStack book, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        return false;
    }

    default boolean book$onStackClicked(ItemStack book, Slot slot, ClickType clickType, PlayerEntity player) {
        return false;
    }

    default boolean book$hasGlint(ItemStack book) {
        return false;
    }

    /**
     * This should be a check to see if a chat message should be intercepted.
     * <b>It is not run on the main server thread, so most world operations are off-limits.</b>
     */
    default boolean book$interceptsChatMessage(ItemStack book, PlayerEntity player, String message) {
        return false;
    }

    @Nullable
    default ItemStack book$finishUsing(ItemStack book, World world, LivingEntity user) {
        return null;
    }

    default UseAction book$getUseAction(ItemStack book) {
        return UseAction.NONE;
    }

    default int book$getMaxUseTime(ItemStack book) {
        return 0;
    }

    default boolean book$onInventoryScroll(ItemStack book, PlayerEntity player, byte scrollDirection) {
        return false;
    }

    default Optional<TooltipData> book$getTooltipData(ItemStack book) {
        return Optional.empty();
    }

    default PageLecternState lectern$getState(MysticalLecternBlockEntity lectern) {
        return new PageLecternState(lectern);
    }

    /**
     * This should be a check to see if a chat message should be intercepted.
     * <b>It is not run on the main server thread, so most world operations are off-limits.</b>
     */
    default boolean lectern$interceptsChatMessage(MysticalLecternBlockEntity lectern, PlayerEntity player, String message) {
        return false;
    }

    default ActionResult lectern$onUse(MysticalLecternBlockEntity lectern, BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        return ActionResult.PASS;
    }

    default ItemActionResult lectern$onUseWithItem(MysticalLecternBlockEntity lectern, ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
