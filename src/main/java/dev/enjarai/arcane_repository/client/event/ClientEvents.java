package dev.enjarai.arcane_repository.client.event;

import dev.enjarai.arcane_repository.item.custom.book.MysticalBookItem;
import dev.enjarai.arcane_repository.network.ScrollItemPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import dev.enjarai.arcane_repository.mixin.accessor.HandledScreenAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;

@Environment(EnvType.CLIENT)
public class ClientEvents {
    public static void register() {
        ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
            if (screen instanceof HandledScreen<?>) {
                ScreenMouseEvents.afterMouseScroll(screen).register(ClientEvents::scrollOnBook);
            }
        });
    }

    private static void scrollOnBook(Screen screen, double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        var handledScreen = (HandledScreenAccessor<?>) screen;
        var focusedSlot = handledScreen.getFocusedSlot();

        if (verticalAmount != 0 && focusedSlot != null) {
            var stack = focusedSlot.getStack();
            var scroll = (byte) (verticalAmount > 0 ? 1 : -1);

            if (stack.getItem() instanceof MysticalBookItem book) {
                book.onInventoryScroll(stack, MinecraftClient.getInstance().player, scroll);
            }

            ClientPlayNetworking.send(new ScrollItemPacket(handledScreen.getHandler().syncId, focusedSlot.id, scroll));
        }
    }


}
