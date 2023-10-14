package dev.enjarai.arcane_repository.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import dev.enjarai.arcane_repository.ArcaneRepository;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class ClientNetworkListeners {

    public static void registerListeners() {
        ClientPlayNetworking.registerGlobalReceiver(ArcaneRepository.BLOCK_PARTICLES_CHANNEL, (client, handler, buf, responseSender) -> {
            var pos = buf.readBlockPos();
            var effect = Registries.PARTICLE_TYPE.get(buf.readIdentifier());

            client.execute(() -> {
                Particles.spawnParticlesCoveringBlock(client.world, pos, (ParticleEffect) effect,
                        UniformIntProvider.create(3, 5), 0.0);
            });
        });
    }
}
