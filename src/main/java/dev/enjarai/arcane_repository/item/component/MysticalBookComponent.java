package dev.enjarai.arcane_repository.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;

import java.util.List;

public record MysticalBookComponent(
  Integer color,
  Identifier catalyst,
  List<Identifier> attributePages,
  Identifier typePage,
  Identifier actionPage
) {
    public static final Codec<MysticalBookComponent> CODEC = RecordCodecBuilder.create(instance ->
      instance.group(
        Codec.INT.optionalFieldOf("color", 0).forGetter(MysticalBookComponent::color),
        Identifier.CODEC.fieldOf("catalyst").forGetter(MysticalBookComponent::catalyst),
        Identifier.CODEC.listOf().optionalFieldOf("attributePages", List.of()).forGetter(MysticalBookComponent::attributePages),
        Identifier.CODEC.fieldOf("typePage").forGetter(MysticalBookComponent::typePage),
        Identifier.CODEC.fieldOf("actionPage").forGetter(MysticalBookComponent::actionPage)
        ).apply(instance, MysticalBookComponent::new)
    );

    public MysticalBookComponent withColor(int newColor) {
        return new MysticalBookComponent(newColor, catalyst, attributePages, typePage, actionPage);
    }

    public MysticalBookComponent withCatalyst(Identifier newCatalyst) {
        return new MysticalBookComponent(color, newCatalyst, attributePages, typePage, actionPage);
    }

    public MysticalBookComponent withAttributePages(List<Identifier> newAttributePages) {
        return new MysticalBookComponent(color, catalyst, newAttributePages, typePage, actionPage);
    }

    public MysticalBookComponent withTypePage(Identifier newTypePage) {
        return new MysticalBookComponent(color, catalyst, attributePages, newTypePage, actionPage);
    }

    public MysticalBookComponent withActionPage(Identifier newActionPage) {
        return new MysticalBookComponent(color, catalyst, attributePages, typePage, newActionPage);
    }
}
