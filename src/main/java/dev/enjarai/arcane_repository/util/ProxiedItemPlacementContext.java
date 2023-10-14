package dev.enjarai.arcane_repository.util;

import dev.enjarai.arcane_repository.mixin.accessor.ItemUsageContextAccessor;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;

public class ProxiedItemPlacementContext extends ItemPlacementContext {
    public ProxiedItemPlacementContext(ItemUsageContext context, ItemStack stack) {
        super(context.getWorld(), context.getPlayer(), context.getHand(), stack, ((ItemUsageContextAccessor) context).getHit());
    }
}
