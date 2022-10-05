package net.messer.mystical_index.client.tooltip;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.messer.mystical_index.client.render.ItemCirclesRenderer2D;
import net.messer.mystical_index.util.BigStack;
import net.messer.mystical_index.util.MathUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;

import static net.messer.mystical_index.client.render.ItemCirclesRenderer.SECONDARY_CIRCLE_ITEM_COUNT;
import static net.messer.mystical_index.client.render.ItemCirclesRenderer.TERNARY_CIRCLE_ITEM_COUNT;

@Environment(EnvType.CLIENT)
public class ItemStorageTooltipComponent extends DrawableHelper implements TooltipComponent {

    private final ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
    private final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
    private final ItemCirclesRenderer2D<BigStack> circleRenderer = new ItemCirclesRenderer2D<>() {
        @Override
        protected void drawItem(MatrixStack matrices, double x, double y, double z, BigStack stack) {
            ItemStorageTooltipComponent.this.drawItem(matrices, (int) x, (int) y, stack);
        }
    };
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
    public void drawItems(TextRenderer textRenderer, int x, int y, MatrixStack matrices, ItemRenderer itemRenderer, int z) {
        if (data.contents.getAll().isEmpty()) return;

        var centerX = x + getWidth(textRenderer) / 2;
        var centerY = y + getHeight() / 2;

        var stacks = data.contents.getAll();

        circleRenderer.render(centerX, centerY, z, matrices, stacks);
    }

    private void drawItem(MatrixStack matrices, int x, int y, BigStack stack) {
        var count = stack.getAmount();

        itemRenderer.renderInGuiWithOverrides(stack.getItemStack(), x - 8, y - 8);
        itemRenderer.renderGuiItemOverlay(textRenderer, stack.getItemStack(), x - 8, y - 8,
                count > 1 ? MathUtil.shortNumberFormat(count) : "");
    }
}
