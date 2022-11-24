package net.messer.mystical_index.compat.patchouli;

import com.mojang.blaze3d.systems.RenderSystem;
import net.messer.mystical_index.MysticalIndex;
import net.messer.mystical_index.item.ModRecipes;
import net.messer.mystical_index.item.recipe.MysticalBookRecipe;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.page.abstr.PageWithText;

public class PageBookRecipe extends PageWithText {
    private static final MysticalBookRecipe RECIPE = new MysticalBookRecipe(MysticalIndex.id("crafting_special_custom_book"));

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float pticks) {
        int recipeX = getX();
        int recipeY = getY();

        RenderSystem.setShaderTexture(0, book.craftingTexture);
        RenderSystem.enableBlend();
        DrawableHelper.drawTexture(ms, recipeX - 2, recipeY - 2, 0, 0, 100, 62, 128, 256);

        int iconX = recipeX + 62;
        int iconY = recipeY + 2;
        DrawableHelper.drawTexture(ms, iconX, iconY, 0, 64, 11, 11, 128, 256);
        if (parent.isMouseInRelativeRange(mouseX, mouseY, iconX, iconY, 11, 11)) {
            parent.setTooltip(new TranslatableText("patchouli.gui.lexicon.shapeless"));
        }

        parent.drawCenteredStringNoShadow(ms, getTitle().asOrderedText(), GuiBook.PAGE_WIDTH / 2, recipeY - 10, book.headerColor);

        var output = RECIPE.getOutput(); // TODO
        parent.renderItemStack(ms, recipeX + 79, recipeY + 22, mouseX, mouseY, output);

//        DefaultedList<Ingredient> ingredients = recipe.getIngredients();
//        int wrap = 3;
//        if (shaped) {
//            wrap = ((ShapedRecipe) recipe).getWidth();
//        }

//        for (int i = 0; i < ingredients.size(); i++) {
//            parent.renderIngredient(ms, recipeX + (i % wrap) * 19 + 3, recipeY + (i / wrap) * 19 + 3, mouseX, mouseY, ingredients.get(i));
//        }

        parent.renderItemStack(ms, recipeX + 79, recipeY + 41, mouseX, mouseY, RECIPE.createIcon());

        super.render(ms, mouseX, mouseY, pticks);
    }

    @Override
    public int getTextHeight() {
        return getY() + getRecipeHeight() - 13;
    }

    public int getRecipeHeight() {
        return 78;
    }

    public Text getTitle() {
        return new TranslatableText("item.mystical_index.mystical_book");
    }

    protected int getX() {
        return GuiBook.PAGE_WIDTH / 2 - 49;
    }

    protected int getY() {
        return 4;
    }
}
