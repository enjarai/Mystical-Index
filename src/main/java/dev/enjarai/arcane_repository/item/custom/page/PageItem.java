package dev.enjarai.arcane_repository.item.custom.page;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import dev.enjarai.arcane_repository.block.entity.MysticalLecternBlockEntity;
import dev.enjarai.arcane_repository.item.ModDataComponentTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class PageItem extends Item {
    public static final Codec<PageItem> CODEC = Registries.ITEM.getCodec().comapFlatMap(i -> {
        if (i instanceof PageItem item) {
            return DataResult.success(item);
        }
        return DataResult.error(() -> "Not a page item");
    }, i -> i);

    public final String id;

    public PageItem(Item.Settings settings, String id) {
        super(settings.rarity(Rarity.UNCOMMON));
        this.id = id;
    }

    public void onCraftToBook(ItemStack page, ItemStack book) {
    }

    public void book$inventoryTick(ItemStack book, World world, Entity entity, int slot, boolean selected) {
    }

    public void book$appendTooltip(ItemStack book, @Nullable TooltipContext world, List<Text> properties, TooltipType type) {
    }

    public void book$appendPropertiesTooltip(ItemStack book, @Nullable TooltipContext context, List<Text> properties, TooltipType type) {
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

    public abstract int getColor();

    public NbtCompound getAttributes(ItemStack book) {
        return book.getOrDefault(ModDataComponentTypes.PAGE_ITEM_ATTRIBUTES, new NbtCompound());
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);

        tooltip.add(Text.literal(""));
        tooltip.add(Text.translatable("item.arcane_repository.page.tooltip.when_applied").formatted(Formatting.GRAY));
    }
}
