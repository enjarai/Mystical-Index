package net.messer.mystical_index.item.custom.page.attribute;

import net.messer.mystical_index.item.ModItems;
import net.messer.mystical_index.item.custom.book.MysticalBookItem;
import net.messer.mystical_index.item.custom.page.ActionPageItem;
import net.messer.mystical_index.item.custom.page.AttributePageItem;
import net.messer.mystical_index.item.custom.page.TypePageItem;
import net.messer.mystical_index.item.custom.page.type.FoodStorageTypePage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.messer.mystical_index.item.ModItems.FEEDING_ACTION_PAGE;

public class AutoFeedingAttributePage extends AttributePageItem {
    public AutoFeedingAttributePage(String id) {
        super(id);
    }

    @Override
    public void appendAttributes(ItemStack page, NbtCompound nbt) {
    }

    @Override
    public int getColor() {
        return 0;
    }

    @Override
    public @Nullable List<ActionPageItem> getCompatibleActions(ItemStack page) {
        return List.of(FEEDING_ACTION_PAGE);
    }

    @Override
    public List<TypePageItem> getCompatibleTypes(ItemStack page) {
        return List.of(ModItems.FOOD_STORAGE_TYPE_PAGE);
    }

    @Override
    public void book$inventoryTick(ItemStack book, World world, Entity entity, int slot, boolean selected) {
        super.book$inventoryTick(book, world, entity, slot, selected);

        if(entity instanceof PlayerEntity player && !player.isCreative() && player.canConsume(false)){
            var usedBook = (MysticalBookItem) book.getItem();

            if (usedBook.getTypePage(book) instanceof FoodStorageTypePage foodPage) {
                var stack = foodPage.tryRemoveFirstStack(book, 1, itemStack -> {
                    var foodComponent = itemStack.getItem().getFoodComponent();
                    if (!itemStack.isFood() || foodComponent == null) {
                        return false;
                    }

                    return player.canConsume(foodComponent.isAlwaysEdible());
                });
                if (stack.isPresent()) {
                    player.eatFood(world, stack.get());
                }
            }
        }
    }
}
