package dev.enjarai.arcane_repository.client.render;

import dev.enjarai.arcane_repository.util.ChatInterception;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class InterceptionWidget {
    private static final int WIDTH = 200;
    private static final int HEIGHT = 40;

    public static void render(DrawContext context, int screenWidth, int screenHeight, float delta, ChatInterception interceptionMode) {
        var rightOffset = -2;
        var bottomOffset = -(2 + 14);
        var left = screenWidth - WIDTH + rightOffset;
        var right = screenWidth + rightOffset;
        var top = screenHeight - HEIGHT + bottomOffset;
        var bottom = screenHeight + bottomOffset;

        context.fill(
                left, top, right, bottom,
                MinecraftClient.getInstance().options.getTextBackgroundColor(Integer.MIN_VALUE)
        );

        context.drawTexture(
                interceptionMode.texture,
                left + 4, top + 4,
                0, 0, 32, 32,
                32, 32
        );

        for (int i = 0; i < 3; i++) {
            context.drawText(
                    MinecraftClient.getInstance().textRenderer,
                    Text.translatable("gui.arcane_repository.intercepting." + interceptionMode.name + "[" + i + "]"),
                    left + 40, top + 4 + i * 12,
                    0xffffffff, true
            );
        }
    }
}
