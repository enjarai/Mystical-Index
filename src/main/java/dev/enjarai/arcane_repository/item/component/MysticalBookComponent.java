package dev.enjarai.arcane_repository.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.enjarai.arcane_repository.item.custom.page.ActionPageItem;
import dev.enjarai.arcane_repository.item.custom.page.AttributePageItem;
import dev.enjarai.arcane_repository.item.custom.page.TypePageItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;

import java.util.List;
import java.util.Optional;

public record MysticalBookComponent(
        int color,
        Item catalyst,
        List<AttributePageItem> attributePages,
        TypePageItem typePage,
        Optional<ActionPageItem> actionPage
) {
    public static final Codec<MysticalBookComponent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.optionalFieldOf("color", 0).forGetter(MysticalBookComponent::color),
                    Registries.ITEM.getCodec().fieldOf("catalyst").forGetter(MysticalBookComponent::catalyst),
                    AttributePageItem.CODEC.listOf().optionalFieldOf("attributePages", List.of()).forGetter(MysticalBookComponent::attributePages),
                    TypePageItem.CODEC.fieldOf("typePage").forGetter(MysticalBookComponent::typePage),
                    ActionPageItem.CODEC.optionalFieldOf("actionPage").forGetter(MysticalBookComponent::actionPage)
            ).apply(instance, MysticalBookComponent::new)
    );

    public MysticalBookComponent withColor(int newColor) {
        return new MysticalBookComponent(newColor, catalyst, attributePages, typePage, actionPage);
    }

    public MysticalBookComponent withCatalyst(Item newCatalyst) {
        return new MysticalBookComponent(color, newCatalyst, attributePages, typePage, actionPage);
    }

    public MysticalBookComponent withAttributePages(List<AttributePageItem> newAttributePages) {
        return new MysticalBookComponent(color, catalyst, newAttributePages, typePage, actionPage);
    }

    public MysticalBookComponent withTypePage(TypePageItem newTypePage) {
        return new MysticalBookComponent(color, catalyst, attributePages, newTypePage, actionPage);
    }

    public MysticalBookComponent withActionPage(Optional<ActionPageItem> newActionPage) {
        return new MysticalBookComponent(color, catalyst, attributePages, typePage, newActionPage);
    }
}
