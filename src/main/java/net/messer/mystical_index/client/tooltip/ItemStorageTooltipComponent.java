package net.messer.mystical_index.client.tooltip;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.messer.mystical_index.client.render.ItemCirclesRenderer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;

import static net.messer.mystical_index.client.render.ItemCirclesRenderer.SECONDARY_CIRCLE_ITEM_COUNT;
import static net.messer.mystical_index.client.render.ItemCirclesRenderer.TERNARY_CIRCLE_ITEM_COUNT;

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
        if (data.size <= SECONDARY_CIRCLE_ITEM_COUNT) return 64;
        if (data.size <= TERNARY_CIRCLE_ITEM_COUNT) return 112;
        return 112;
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
