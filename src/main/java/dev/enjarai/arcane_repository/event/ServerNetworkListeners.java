package dev.enjarai.arcane_repository.event;

import dev.enjarai.arcane_repository.network.BlockParticlesPacket;
import dev.enjarai.arcane_repository.network.ScrollItemPacket;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ServerNetworkListeners {
    public static void registerListeners() {
        PayloadTypeRegistry.playC2S().register(ScrollItemPacket.ID, ScrollItemPacket.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(BlockParticlesPacket.ID, BlockParticlesPacket.PACKET_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(ScrollItemPacket.ID, ScrollItemPacket::handle);
    }
}
