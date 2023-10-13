package net.messer.mystical_index.util;

import net.messer.mystical_index.mixin.accessor.ItemUsageContextAccessor;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;

public class ProxiedItemPlacementContext extends ItemPlacementContext {
    public ProxiedItemPlacementContext(ItemUsageContext context, ItemStack stack) {
        super(context.getWorld(), context.getPlayer(), context.getHand(), stack, ((ItemUsageContextAccessor) context).getHit());
    }
}
