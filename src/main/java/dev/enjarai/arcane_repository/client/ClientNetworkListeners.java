package dev.enjarai.arcane_repository.client;

import dev.enjarai.arcane_repository.network.BlockParticlesPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import dev.enjarai.arcane_repository.ArcaneRepository;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class ClientNetworkListeners {

    public static void registerListeners() {
        ClientPlayNetworking.registerGlobalReceiver(BlockParticlesPacket.ID, (payload, context) -> {
            Particles.spawnParticlesCoveringBlock(context.client().world, payload.pos(), (ParticleEffect) payload.effect(), UniformIntProvider.create(3, 5), 0.0);
        });
    }
}
