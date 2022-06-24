package net.messer.mystical_index.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.messer.mystical_index.util.ChatInterception;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

public class InterceptionWidget extends DrawableHelper {
    private static final int WIDTH = 200;
    private static final int HEIGHT = 40;

    public static void render(MatrixStack matrices, int screenWidth, int screenHeight, float delta, ChatInterception interceptionMode) {
        var rightOffset = -2;
        var bottomOffset = -(2 + 14);
        var left = screenWidth - WIDTH + rightOffset;
        var right = screenWidth + rightOffset;
        var top = screenHeight - HEIGHT + bottomOffset;
        var bottom = screenHeight + bottomOffset;

        fill(
                matrices,
                left, top, right, bottom,
                MinecraftClient.getInstance().options.getTextBackgroundColor(Integer.MIN_VALUE)
        );

        RenderSystem.setShaderTexture(0, interceptionMode.texture);
        drawTexture(
                matrices,
                left + 4, top + 4,
                0, 0, 32, 32,
                32, 32
        );

        for (int i = 0; i < 3; i++) {
            MinecraftClient.getInstance().textRenderer.draw(
                    matrices,
                    new TranslatableText("gui.mystical_index.intercepting." + interceptionMode.name + "[" + i + "]"),
                    left + 40, top + 4 + i * 12,
                    0xffffffff
            );
        }
    }
}
