package net.messer.mystical_index.item.custom.page.attribute;

import net.messer.mystical_index.item.ModItems;
import net.messer.mystical_index.item.custom.page.AttributePageItem;
import net.messer.mystical_index.item.custom.page.TypePageItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class MagnetismAttributePage extends AttributePageItem {
    public MagnetismAttributePage(String id) {
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
    public List<TypePageItem> getCompatibleTypes(ItemStack page) {
        return List.of(ModItems.ITEM_STORAGE_TYPE_PAGE);
    }

    @Override
    public void book$inventoryTick(ItemStack book, World world, Entity entity, int slot, boolean selected) {
        super.book$inventoryTick(book, world, entity, slot, selected);

        if(entity instanceof PlayerEntity player){
            var pos = player.getPos();
            var target = pos.add(.05, .05, .05);
            float pickUpRange = 5;
            var box = Box.from(target).expand(pickUpRange);
            var entityList = world.getNonSpectatingEntities(ItemEntity.class, box);
            entityList.forEach(item -> {
                var velocity = item.getPos().relativize(target).normalize().multiply(0.1);
                item.addVelocity(velocity.x, velocity.y, velocity.z);
            });
        }
    }
}
