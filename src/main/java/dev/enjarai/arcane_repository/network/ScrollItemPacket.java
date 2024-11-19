package dev.enjarai.arcane_repository.network;

import dev.enjarai.arcane_repository.ArcaneRepository;
import dev.enjarai.arcane_repository.item.custom.book.MysticalBookItem;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

public record ScrollItemPacket(int syncId, int slotId, byte scroll) implements CustomPayload {
    public static final Id<ScrollItemPacket> ID = new CustomPayload.Id<>(ArcaneRepository.id("scroll_item"));
    public static final PacketCodec<RegistryByteBuf, ScrollItemPacket> PACKET_CODEC = CustomPayload.codecOf(ScrollItemPacket::writePacket, ScrollItemPacket::readPacket);

    public void writePacket(RegistryByteBuf buf) {
        buf.writeByte(syncId);
        buf.writeShort(slotId);
        buf.writeByte(scroll);
    }

    public static ScrollItemPacket readPacket(RegistryByteBuf buf) {
        return new ScrollItemPacket(buf.readByte(), buf.readShort(), (byte) (buf.readByte() > 0 ? 1 : -1));
    }

    public void handle(ServerPlayNetworking.Context context) {
        ServerPlayerEntity player = context.player();

        var screen = player.currentScreenHandler;

        if (screen.syncId == syncId && screen.isValid(slotId)) {
            var slot = screen.getSlot(slotId);
            var stack = slot.getStack();

            if (stack.getItem() instanceof MysticalBookItem book) {
                book.onInventoryScroll(stack, player, scroll);
            }
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
