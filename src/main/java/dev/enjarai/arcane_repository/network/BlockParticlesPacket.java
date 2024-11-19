package dev.enjarai.arcane_repository.network;

import dev.enjarai.arcane_repository.ArcaneRepository;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.BlockPos;

public record BlockParticlesPacket(BlockPos pos, ParticleType<?> effect) implements CustomPayload {
    public static final Id<BlockParticlesPacket> ID = new Id<>(ArcaneRepository.id("scroll_item"));
    public static final PacketCodec<RegistryByteBuf, BlockParticlesPacket> PACKET_CODEC = CustomPayload.codecOf(BlockParticlesPacket::writePacket, BlockParticlesPacket::readPacket);

    public void writePacket(RegistryByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeRegistryKey(Registries.PARTICLE_TYPE.getKey(effect).orElseThrow());
    }

    public static BlockParticlesPacket readPacket(RegistryByteBuf buf) {
        return new BlockParticlesPacket(buf.readBlockPos(), Registries.PARTICLE_TYPE.getOrThrow(buf.readRegistryKey(RegistryKeys.PARTICLE_TYPE)));
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
