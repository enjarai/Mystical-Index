package net.messer.mystical_index.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public abstract class ItemCirclesRenderer2D<T> extends ItemCirclesRenderer<T> {

    protected void drawItemCircle(DrawContext context, double x, double y, double z, boolean isPrimary) {
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();

        var matrices = context.getMatrices();

        matrices.push();
        matrices.translate(0, 0, z);
        context.drawTexture(CIRCLES_TEXTURE, (int) x - 12, (int) y - 12, isPrimary ? 24 : 0, 0, 24, 24);
        matrices.pop();
    }
}
