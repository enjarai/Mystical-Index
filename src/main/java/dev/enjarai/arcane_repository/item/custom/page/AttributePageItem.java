package dev.enjarai.arcane_repository.item.custom.page;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import dev.enjarai.arcane_repository.item.ItemSettings;
import dev.enjarai.arcane_repository.item.ModDataComponentTypes;
import net.minecraft.component.ComponentType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AttributePageItem extends PageItem implements TypeDependentPage, ActionDependablePage {
    public static final Codec<AttributePageItem> CODEC = Registries.ITEM.getCodec().comapFlatMap(i -> {
        if (i instanceof AttributePageItem item) {
            return DataResult.success(item);
        }
        return DataResult.error(() -> "Not an attribute page item");
    }, i -> i);

    public AttributePageItem(String id) {
        super(new ItemSettings(), id);
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

        var compound = book.getOrDefault(ModDataComponentTypes.PAGE_ITEM_ATTRIBUTES, new NbtCompound()).copy();
        appendAttributes(page, compound);
        book.set(ModDataComponentTypes.PAGE_ITEM_ATTRIBUTES, compound);
    }

    @Nullable
    public MutableText getAttributeDisplayName() {
        return Text.translatable("item.arcane_repository.page.tooltip.attribute." + id)
                .fillStyle(Style.EMPTY.withColor(getColor()));
    }

    @Nullable
    public MutableText book$getAttributeDisplayName() {
        return getAttributeDisplayName();
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);

        var name = getAttributeDisplayName();
        if (name != null) tooltip.add(name);
    }

    @Override
    public void book$appendPropertiesTooltip(ItemStack book, @Nullable TooltipContext context, List<Text> properties, TooltipType type) {
        super.book$appendPropertiesTooltip(book, context, properties, type);

        var name = book$getAttributeDisplayName();
        if (name != null) properties.add(name);
    }
}
