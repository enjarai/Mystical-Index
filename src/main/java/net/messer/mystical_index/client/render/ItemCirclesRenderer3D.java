package net.messer.mystical_index.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.util.math.MatrixStack;

public abstract class ItemCirclesRenderer3D<T> extends ItemCirclesRenderer<T> {

    protected void drawItemCircle(MatrixStack matrices, double x, double y, double z, boolean isPrimary) {
        // TODO: 3D circles
    }
}
