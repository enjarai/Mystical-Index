package net.messer.mystical_index.item.custom.page;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AttributePageItem extends PageItem implements TypeDependentPage, ActionDependablePage {
    public static final String ATTRIBUTES_TAG = "attributes";

    public AttributePageItem(String id) {
        super(id);
    }

    public static void multiplyIntAttribute(NbtCompound nbt, String attribute, double amount) {
        var attributeValue = nbt.getInt(attribute);
        nbt.putInt(attribute, (int) (attributeValue * amount));
    }

    public List<TypeDependentPage> getIncompatiblePages(ItemStack page) {
        return List.of();
    }

    public boolean bookCanHaveMultiple(ItemStack page) {
        return true;
    }

    public abstract void appendAttributes(ItemStack page, NbtCompound nbt);

    @Override
    public void onCraftToBook(ItemStack page, ItemStack book) {
        super.onCraftToBook(page, book);

        appendAttributes(page, book.getOrCreateSubNbt(ATTRIBUTES_TAG));
    }

    @Nullable
    public MutableText getAttributeDisplayName() {
        return Text.translatable("item.mystical_index.page.tooltip.attribute." + id)
                .fillStyle(Style.EMPTY.withColor(getColor()));
    }

    @Nullable
    public MutableText book$getAttributeDisplayName() {
        return getAttributeDisplayName();
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        var name = getAttributeDisplayName();
        if (name != null) tooltip.add(name);
    }

    @Override
    public void book$appendPropertiesTooltip(ItemStack book, @Nullable World world, List<Text> properties, TooltipContext context) {
        super.book$appendPropertiesTooltip(book, world, properties, context);

        var name = book$getAttributeDisplayName();
        if (name != null) properties.add(name);
    }
}
