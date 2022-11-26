package net.messer.mystical_index.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.messer.mystical_index.MysticalIndex;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.Registry;

public class ClientNetworkListeners {

    public static void registerListeners() {
        ClientPlayNetworking.registerGlobalReceiver(MysticalIndex.BLOCK_PARTICLES_CHANNEL, (client, handler, buf, responseSender) -> {
            var pos = buf.readBlockPos();
            var effect = Registry.PARTICLE_TYPE.get(buf.readIdentifier());

            client.execute(() -> {
                Particles.spawnParticlesCoveringBlock(client.world, pos, (ParticleEffect) effect,
                        UniformIntProvider.create(3, 5), 0.0);
            });
        });
    }
}
