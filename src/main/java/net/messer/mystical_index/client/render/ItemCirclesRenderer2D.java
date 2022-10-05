package net.messer.mystical_index.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.util.math.MatrixStack;

public abstract class ItemCirclesRenderer2D<T> extends ItemCirclesRenderer<T> {

    protected void drawItemCircle(MatrixStack matrices, double x, double y, double z, boolean isPrimary) {
        RenderSystem.setShaderTexture(0, CIRCLES_TEXTURE);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();

        matrices.push();
        matrices.translate(0, 0, z);
        drawTexture(matrices, (int) x - 12, (int) y - 12, isPrimary ? 24 : 0, 0, 24, 24);
        matrices.pop();
    }
}
