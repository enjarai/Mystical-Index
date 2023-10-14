package dev.enjarai.arcane_repository.item.custom.page;

import dev.enjarai.arcane_repository.block.entity.MysticalLecternBlockEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static dev.enjarai.arcane_repository.item.custom.page.AttributePageItem.ATTRIBUTES_TAG;

public abstract class PageItem extends Item {
    public final String id;

    public PageItem(String id) {
        super(new FabricItemSettings());
        this.id = id;
    }

    public void onCraftToBook(ItemStack page, ItemStack book) {
    }

    public void book$inventoryTick(ItemStack book, World world, Entity entity, int slot, boolean selected) {
    }

    public void book$appendTooltip(ItemStack book, @Nullable World world, List<Text> properties, TooltipContext context) {
    }

    public void book$appendPropertiesTooltip(ItemStack book, @Nullable World world, List<Text> properties, TooltipContext context) {
    }

    /**
     * The actual handling of intercepted chat messages should happen here.
     * This is run on the main server thread.
     */
    public void book$onInterceptedChatMessage(ItemStack book, ServerPlayerEntity player, String message) {
    }

    /**
     * The actual handling of intercepted chat messages should happen here.
     * This is run on the main server thread.
     */
    public void lectern$onInterceptedChatMessage(MysticalLecternBlockEntity lectern, ServerPlayerEntity player, String message) {
    }

    public void lectern$onEntityCollision(MysticalLecternBlockEntity lectern, BlockState state, World world, BlockPos pos, Entity entity) {
    }

    public void lectern$onPlaced(MysticalLecternBlockEntity lectern) {
    }

    public void lectern$afterPlaced(MysticalLecternBlockEntity lectern) {
    }

    public void lectern$onRemoved(PlayerEntity player, MysticalLecternBlockEntity lectern) {
    }

    public void lectern$serverTick(World world, BlockPos pos, BlockState state, MysticalLecternBlockEntity lectern) {
    }

    @Override
    public Rarity getRarity(ItemStack page) {
        return Rarity.UNCOMMON;
    }

    public abstract int getColor();

    public NbtCompound getAttributes(ItemStack book) {
        return book.getOrCreateSubNbt(ATTRIBUTES_TAG);
    }

    // TODO display color on item texture somehow
//    @Override
//    public ItemStack getPolymerItemStack(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
//        var returnStack = PolymerItem.super.getPolymerItemStack(itemStack, player);
//        returnStack.getOrCreateSubNbt("display").putInt("MapColor", getColor());
//        return returnStack;
//    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.add(Text.literal(""));
        tooltip.add(Text.translatable("item.arcane_repository.page.tooltip.when_applied").formatted(Formatting.GRAY));
    }
}
