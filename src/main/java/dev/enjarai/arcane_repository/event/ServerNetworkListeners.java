package dev.enjarai.arcane_repository.event;

import dev.enjarai.arcane_repository.ArcaneRepository;
import dev.enjarai.arcane_repository.item.custom.book.MysticalBookItem;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class ServerNetworkListeners {
    public static final Identifier SCROLL_ITEM = ArcaneRepository.id("scroll_item");

    public static void registerListeners() {
        ServerPlayNetworking.registerGlobalReceiver(SCROLL_ITEM, (server, player, handler, buf, responseSender) -> {
            var syncId = buf.readByte();
            var slotId = buf.readShort();
            var scroll = (byte) (buf.readByte() > 0 ? 1 : -1);

            server.execute(() -> {
                var screen = player.currentScreenHandler;

                if (screen.syncId == syncId && screen.isValid(slotId)) {
                    var slot = screen.getSlot(slotId);
                    var stack = slot.getStack();

                    if (stack.getItem() instanceof MysticalBookItem book) {
                        book.onInventoryScroll(stack, player, scroll);
                    }
                }
            });
        });
    }
}
