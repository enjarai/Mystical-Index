package dev.enjarai.arcane_repository.item;

import dev.enjarai.arcane_repository.ArcaneRepository;
import dev.enjarai.arcane_repository.item.recipe.MysticalBookRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;

public class ModRecipes {
    public static final SpecialRecipeSerializer<MysticalBookRecipe> MYSTICAL_BOOK = RecipeSerializer.register(
            ArcaneRepository.id("crafting_special_custom_book").toString(),
            new SpecialRecipeSerializer<>(MysticalBookRecipe::new));

    public static void registerModRecipes(){
        ArcaneRepository.LOGGER.info("Registering recipes for " + ArcaneRepository.MOD_ID);
    }
}
