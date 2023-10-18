package dev.enjarai.arcane_repository.item.custom.page.attribute;

import dev.enjarai.arcane_repository.item.custom.book.MysticalBookItem;
import dev.enjarai.arcane_repository.item.custom.page.AttributePageItem;
import dev.enjarai.arcane_repository.item.custom.page.TypePageItem;
import dev.enjarai.arcane_repository.item.custom.page.type.ItemStorageTypePage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

import static dev.enjarai.arcane_repository.item.ModItems.*;

public class MagnetismAttributePage extends AttributePageItem {
    public MagnetismAttributePage(String id) {
        super(id);
    }

    @Override
    public void appendAttributes(ItemStack page, NbtCompound nbt) {
    }

    @Override
    public int getColor() {
        return 0x4b6ca5;
    }

    @Override
    public List<TypePageItem> getCompatibleTypes(ItemStack page) {
        return List.of(ITEM_STORAGE_TYPE_PAGE, FOOD_STORAGE_TYPE_PAGE, BLOCK_STORAGE_TYPE_PAGE);
    }

    @Override
    public boolean bookCanHaveMultiple(ItemStack page) {
        return false;
    }

    @Override
    public void book$inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.book$inventoryTick(stack, world, entity, slot, selected);

        if (entity instanceof PlayerEntity player
                && stack.getItem() instanceof MysticalBookItem book
                && book.getTypePage(stack) instanceof ItemStorageTypePage typePage
                && typePage.isFiltered(stack)
        ) {
            var pos = player.getPos();
            var target = pos.add(.05, .05, .05);
            var pickUpRange = 5;
            var box = Box.from(target).expand(pickUpRange);
            var entityList = world.getNonSpectatingEntities(ItemEntity.class, box);
            entityList.forEach(item -> {
                if (typePage.isFilteredTo(stack, item.getStack())) {
                    var velocity = item.getPos().relativize(target).normalize().multiply(0.1);
                    item.addVelocity(velocity.x, velocity.y, velocity.z);
                }
            });
        }
    }
}
