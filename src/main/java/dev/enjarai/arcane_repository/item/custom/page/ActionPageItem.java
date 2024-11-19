package dev.enjarai.arcane_repository.item.custom.page;

import dev.enjarai.arcane_repository.item.ItemSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class ActionPageItem extends PageItem implements TypeDependentPage, InteractingPage {
    public ActionPageItem(String id) {
        super(new ItemSettings(), id);
    }

    public MutableText getActionDisplayName() {
        return Text.translatable("item.arcane_repository.page.tooltip.action." + id).fillStyle(Style.EMPTY.withColor(getColor()));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);

        tooltip.add(getActionDisplayName());
    }

    @Override
    public void book$appendPropertiesTooltip(ItemStack stack, @Nullable TooltipContext context, List<Text> properties, TooltipType type) {
        super.book$appendPropertiesTooltip(stack, context, properties, type);

        properties.add(getActionDisplayName());
    }
}
