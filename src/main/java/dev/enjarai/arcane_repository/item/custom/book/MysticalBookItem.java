package dev.enjarai.arcane_repository.item.custom.book;

import dev.enjarai.arcane_repository.block.ModBlocks;
import dev.enjarai.arcane_repository.block.custom.MysticalLecternBlock;
import dev.enjarai.arcane_repository.block.entity.MysticalLecternBlockEntity;
import dev.enjarai.arcane_repository.item.ModDataComponentTypes;
import dev.enjarai.arcane_repository.item.component.MysticalBookComponent;
import dev.enjarai.arcane_repository.item.custom.page.*;
import dev.enjarai.arcane_repository.util.PageRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LecternBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static dev.enjarai.arcane_repository.item.custom.page.AttributePageItem.ATTRIBUTES_TAG;

public class MysticalBookItem extends Item {
    public MysticalBookItem(Settings settings) {
        super(
          settings
            .component(ModDataComponentTypes.MYSTICAL_BOOK, new MysticalBookComponent(
                0,
                Identifier.ofVanilla("stone"),
                List.of(),
                Identifier.ofVanilla("stone"),
                Identifier.ofVanilla("stone")
              )
            ).component(ATTRIBUTES_TAG, new NbtCompound())
        );
    }

    private MysticalBookComponent getComponent(ItemStack stack) {
        return stack.get(ModDataComponentTypes.MYSTICAL_BOOK);
    }

    public int getColor(ItemStack stack) {
        return getComponent(stack).color();
    }

    public void setColor(ItemStack stack, int color) {
        stack.set(ModDataComponentTypes.MYSTICAL_BOOK, getComponent(stack).withColor(color));
    }

    public Item getCatalyst(ItemStack book) {
        return Registries.ITEM.get(getComponent(book).catalyst());
    }

    /**
     * Safely get a single page from a tag.
     */
    @Nullable
    public PageItem getPage(ItemStack book, Function<MysticalBookComponent, Identifier> tag) {
        try {
            return PageRegistry.getPage(tag.apply(getComponent(book)));
        } catch (Throwable e) {
            return null;
        }
    }

    @Nullable
    public AttributePageItem getAttributePage(ItemStack book, String id) {
        List<AttributePageItem> attributePagesList = new ArrayList<>();

        getComponent(book).attributePages().forEach(pageId -> {
            if (pageId != null) {
                var page = PageRegistry.getPage(pageId);
                if (page != null) {
                    attributePagesList.add((AttributePageItem) page);
                }
            }
        });

        if (attributePagesList.isEmpty())
            return null;

        for (AttributePageItem page : attributePagesList) {
            if (Objects.equals(page.id, id)) {
                return page;
            }
        }
        return null;
    }

    /**
     * Returns the current type page of the book.
     */
    @Nullable
    public TypePageItem getTypePage(ItemStack book) {
        return (TypePageItem) getPage(book, MysticalBookComponent::typePage);
    }

    /**
     * Returns the current action page of the book.
     */
    @Nullable
    public ActionPageItem getActionPage(ItemStack book) {
        return (ActionPageItem) getPage(book, MysticalBookComponent::actionPage);
    }

    /**
     * Checks if the type page of this book is of a certain class, and if so, returns it.
     */
    @Nullable
    public static <T extends TypePageItem> T isType(ItemStack book, Class<T> clazz) {
        try {
            return clazz.cast(((MysticalBookItem) book.getItem()).getTypePage(book));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Runs the given consumer on the page in the given tag without a return value.
     */
    private void forPage(ItemStack book, Function<MysticalBookComponent, Identifier> tag, Consumer<PageItem> consumer) {
        forPage(book, tag, pageItem -> {
            consumer.accept(pageItem);
            return null;
        }, null);
    }

    /**
     * Runs a function for the page with the given tag,
     * providing the returned result or a default value if the page is not found.
     */
    private <R> R forPage(ItemStack book, Function<MysticalBookComponent, Identifier> component, Function<PageItem, R> function, R defaultValue) {
        var pageId = component.apply(getComponent(book));
        if (pageId != null) {
            var page = PageRegistry.getPage(pageId);
            if (page != null) {
                return function.apply(page);
            }
        }
        return defaultValue;
    }

    /**
     * Similar to {@link MysticalBookItem#forPage(ItemStack, Function, Function, Object)}, runs a function for the page with the given tag,
     * but safely casts it to {@link InteractingPage} first.
     * This opens up page functions that have return values.
     */
    private <R> R forInteractingPage(ItemStack book, Function<MysticalBookComponent, Identifier> tag, Function<InteractingPage, R> function, R defaultValue) {
        return forPage(book, tag, pageItem -> {
            if (pageItem instanceof InteractingPage) {
                return function.apply((InteractingPage) pageItem);
            }
            return defaultValue;
        }, defaultValue);
    }

    /**
     * <p>
     * Similar to {@link MysticalBookItem#forInteractingPage(ItemStack, Function, Function, Object)},
     * but runs the function for all pages supporting interaction. (Namely type pages and action pages)
     * </p>
     * <p>
     * Requires a return condition, which determines whether the value
     * returned by the function lambda is accepted or ignored.
     * If it is ignored, the next page will be run and checked.
     * </p>
     * <p>
     * <b>Once the return value is accepted, the following pages will be ignored.</b>
     * </p>
     */
    private <R> R forInteractingPages(ItemStack book, Predicate<R> returnCondition, Function<InteractingPage, R> function, R defaultValue) {
        for (var tag : List.<Function<MysticalBookComponent, Identifier>>of(MysticalBookComponent::typePage, MysticalBookComponent::actionPage)) {
            R result = forInteractingPage(book, tag, function, defaultValue);

            if (returnCondition.test(result)) {
                return result;
            }
        }
        return defaultValue;
    }

    /**
     * Runs the given consumer for every page in the book.
     */
    public void forEachPage(ItemStack book, Consumer<PageItem> consumer) {
        forPage(book, MysticalBookComponent::typePage, consumer);

        getComponent(book).attributePages().forEach(pageId -> {
            if (pageId != null) {
                var page = PageRegistry.getPage(pageId);
                if (page != null) {
                    consumer.accept(page);
                }
            }
        });

        forPage(book, MysticalBookComponent::actionPage, consumer);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return forInteractingPages(user.getStackInHand(hand), result -> result.getResult() != ActionResult.PASS,
          page -> page.book$use(world, user, hand), TypedActionResult.pass(user.getStackInHand(hand)));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos blockPos;
        World world = context.getWorld();
        BlockState blockState = world.getBlockState(blockPos = context.getBlockPos());

        // Try to put book on lectern
        if (blockState.isOf(Blocks.LECTERN) && !blockState.get(LecternBlock.HAS_BOOK)) {
            var newState = ModBlocks.MYSTICAL_LECTERN.getStateWithProperties(blockState);

            world.setBlockState(blockPos, newState);
            ItemStack stack = context.getStack().copy();
            context.getStack().decrement(1);

            return MysticalLecternBlock.putBookIfAbsent(
              context.getPlayer(), world,
              blockPos, newState,
              stack
            ) ? ActionResult.success(world.isClient) : ActionResult.PASS;
        }

        return forInteractingPages(context.getStack(), result -> result != ActionResult.PASS,
          page -> page.book$useOnBlock(context), ActionResult.PASS);
    }

    @Override
    public boolean onClicked(ItemStack book, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        return forInteractingPages(book, result -> result,
          page -> page.book$onClicked(book, otherStack, slot, clickType, player, cursorStackReference), false);
    }

    @Override
    public boolean onStackClicked(ItemStack book, Slot slot, ClickType clickType, PlayerEntity player) {
        return forInteractingPages(book, result -> result,
          page -> page.book$onStackClicked(book, slot, clickType, player), false);
    }

    @Override
    public void inventoryTick(ItemStack book, World world, Entity entity, int slot, boolean selected) {
        forEachPage(book, page -> page.book$inventoryTick(book, world, entity, slot, selected));
    }

    @Override
    public void appendTooltip(ItemStack book, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(book, context, tooltip, type);

        if (getTypePage(book) == null) return;

        forEachPage(book, page -> page.book$appendTooltip(book, context, tooltip, type));

        tooltip.add(Text.literal(""));
        tooltip.add(Text.translatable("item.arcane_repository.repository_book.tooltip.properties")
          .formatted(Formatting.GRAY));

        forEachPage(book, page -> page.book$appendPropertiesTooltip(book, context, tooltip, type));
    }

    @Override
    public boolean hasGlint(ItemStack book) {
        return forInteractingPages(book, result -> result,
          page -> page.book$hasGlint(book), false);
    }

    public boolean interceptsChatMessage(ItemStack book, PlayerEntity player, String message) {
        return forInteractingPages(book, result -> result,
          page -> page.book$interceptsChatMessage(book, player, message), false);
    }

    public void onInterceptedChatMessage(ItemStack book, ServerPlayerEntity player, String message) {
        forEachPage(book, page -> page.book$onInterceptedChatMessage(book, player, message));
    }

    @Override
    public ItemStack finishUsing(ItemStack book, World world, LivingEntity user) {
        return forInteractingPages(book, Objects::nonNull,
          page -> page.book$finishUsing(book, world, user), book);
    }

    @Override
    public UseAction getUseAction(ItemStack book) {
        return forInteractingPages(book, result -> result != UseAction.NONE,
          page -> page.book$getUseAction(book), UseAction.NONE);
    }

    @Override
    public int getMaxUseTime(ItemStack book, LivingEntity user) {
        return forInteractingPages(book, result -> result != 0,
          page -> page.book$getMaxUseTime(book), 0);
    }

    public void onInventoryScroll(ItemStack book, PlayerEntity player, byte scrollDirection) {
        forInteractingPages(book, result -> result,
          page -> page.book$onInventoryScroll(book, player, scrollDirection), false);
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack book) {
        return forInteractingPages(book, Optional::isPresent,
          page -> page.book$getTooltipData(book), Optional.empty());
    }

    public boolean lectern$interceptsChatMessage(MysticalLecternBlockEntity lectern, PlayerEntity player, String message) {
        return forInteractingPages(lectern.getBook(), result -> result,
          page -> page.lectern$interceptsChatMessage(lectern, player, message), false);
    }

    public void lectern$onInterceptedChatMessage(MysticalLecternBlockEntity lectern, ServerPlayerEntity player, String message) {
        forEachPage(lectern.getBook(), page -> page.lectern$onInterceptedChatMessage(lectern, player, message));
    }

    public void lectern$onEntityCollision(MysticalLecternBlockEntity lectern, BlockState state, World world, BlockPos pos, Entity entity) {
        forEachPage(lectern.getBook(), page -> page.lectern$onEntityCollision(lectern, state, world, pos, entity));
    }

    public ActionResult lectern$onUse(MysticalLecternBlockEntity lectern, BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        return forInteractingPages(lectern.getBook(), result -> result != ActionResult.PASS,
          page -> page.lectern$onUse(lectern, state, world, pos, player, hit), ActionResult.PASS);
    }

    public ItemActionResult lectern$onUseWithItem(MysticalLecternBlockEntity lectern, ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        return forInteractingPages(lectern.getBook(), result -> result != ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION,
          page -> page.lectern$onUseWithItem(lectern, stack, state, world, pos, player, hit), ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION);
    }

    public void lectern$serverTick(World world, BlockPos pos, BlockState state, MysticalLecternBlockEntity lectern) {
        forEachPage(lectern.getBook(), page -> page.lectern$serverTick(world, pos, state, lectern));
    }

    public void lectern$onPlaced(MysticalLecternBlockEntity lectern) {
        forEachPage(lectern.getBook(), page -> page.lectern$onPlaced(lectern));
    }

    public void lectern$afterPlaced(MysticalLecternBlockEntity lectern) {
        forEachPage(lectern.getBook(), page -> page.lectern$afterPlaced(lectern));
    }

    public void lectern$onRemoved(PlayerEntity player, MysticalLecternBlockEntity lectern) {
        forEachPage(lectern.getBook(), page -> page.lectern$onRemoved(player, lectern));
    }

    @Override
    public Text getName(ItemStack book) {
        var page = getTypePage(book);
        if (page != null) {
            return page.getBookDisplayName();
        }
        return super.getName(book);
    }

    @Override
    public boolean canBeNested() {
        return false;
    }
}
