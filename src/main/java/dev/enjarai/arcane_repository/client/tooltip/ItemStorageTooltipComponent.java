package dev.enjarai.arcane_repository.client.tooltip;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import dev.enjarai.arcane_repository.client.render.ItemCirclesRenderer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;

import static dev.enjarai.arcane_repository.client.render.ItemCirclesRenderer.*;

@Environment(EnvType.CLIENT)
public class ItemStorageTooltipComponent implements TooltipComponent {
    private final ItemCirclesRenderer circleRenderer = new ItemCirclesRenderer(false);
    private final ItemStorageTooltipData data;

    public ItemStorageTooltipComponent(ItemStorageTooltipData data) {
        this.data = data;
    }

    public int getSize() {
        if (data.size <= 0) return 0;
        if (data.size <= 1) return 22;
        if (data.size <= SECONDARY_CIRCLE_ITEM_COUNT) return (24 + 8) * 2;
        if (data.size <= TERNARY_CIRCLE_ITEM_COUNT) return (48 + 8) * 2;
        if (data.size <= QUATERNARY_CIRCLE_ITEM_COUNT) return (72 + 8) * 2;
        return (72 + 8) * 2;
    }

    @Override
    public int getHeight() {
        return getSize();
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return getSize();
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        if (data.contents.getAll().isEmpty()) return;

        var centerX = x + getWidth(textRenderer) / 2;
        var centerY = y + getHeight() / 2;

        var stacks = data.contents.getAll();

        circleRenderer.render(context, centerX, centerY, stacks);
    }
}
