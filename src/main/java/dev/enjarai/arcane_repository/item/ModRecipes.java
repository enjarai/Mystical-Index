package dev.enjarai.arcane_repository.item;

import dev.enjarai.arcane_repository.ArcaneRepository;
import dev.enjarai.arcane_repository.item.recipe.MysticalBookRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModRecipes {
    public static final SpecialRecipeSerializer<MysticalBookRecipe> MYSTICAL_BOOK = register(
      "crafting_special_custom_book", new SpecialRecipeSerializer<>(MysticalBookRecipe::new)
    );

    private static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String id, S serializer) {
        return Registry.register(Registries.RECIPE_SERIALIZER, ArcaneRepository.id(id), serializer);
    }

    public static void registerModRecipes() {
        ArcaneRepository.LOGGER.info("Registering recipes for " + ArcaneRepository.MOD_ID);
    }
}
