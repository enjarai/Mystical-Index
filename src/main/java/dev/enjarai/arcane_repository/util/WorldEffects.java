package dev.enjarai.arcane_repository.util;

import dev.enjarai.arcane_repository.ArcaneRepository;
import dev.enjarai.arcane_repository.util.request.IndexInteractable;
import dev.enjarai.arcane_repository.util.request.Request;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WorldEffects {
    private static final double ITEMS_PER_PARTICLE = 16;
    private static final double PARTICLE_SPACING = 0.4;

    public static void extractionParticles(Request request, BlockEntity source) {
        Vec3d sourcePos = request.getSourcePosition();

        if (sourcePos != null) {
            movingEnchantParticles(
                    source.getWorld(),
                    Vec3d.ofCenter(source.getPos()),
                    sourcePos,
                    (int) Math.ceil(request.getAmountAffected() / ITEMS_PER_PARTICLE)
            );
        }
    }

    public static void insertionParticles(Request request, BlockEntity source) { // TODO oopify
        Vec3d sourcePos = request.getSourcePosition();

        if (sourcePos != null) {
            movingEnchantParticles(
                    source.getWorld(),
                    sourcePos,
                    Vec3d.ofCenter(source.getPos()),
                    (int) Math.ceil(request.getAmountAffected() / ITEMS_PER_PARTICLE)
            );
        }
    }

    private static void movingEnchantParticles(World world, Vec3d source, Vec3d destination, int amount) {
        Vec3d velocity = source.subtract(destination);
        double length = velocity.length();
        for (int i = 0; i < amount; i++) {
            for (double d = 0; d < length; d += PARTICLE_SPACING) {
                Vec3d modVelocity = velocity.multiply(d / length);
                ((ServerWorld) world).spawnParticles(ParticleTypes.ENCHANT,
                        destination.getX(), destination.getY() + 1, destination.getZ(),
                        0,
                        modVelocity.getX(), modVelocity.getY() - 1, modVelocity.getZ(),
                        1
                );
            }
        }
    }

    public static void registrationParticles(IndexInteractable interactable) { // TODO add subtle sound
        if (interactable instanceof BlockEntity blockEntity) {
            blockParticles(blockEntity.getWorld(), blockEntity.getPos(), ParticleTypes.SOUL_FIRE_FLAME);
        }
    }

    public static void blockParticles(World world, BlockPos pos, ParticleEffect effect) {
        if (world != null && !world.isClient()) {
            var buf = PacketByteBufs.create();

            buf.writeBlockPos(pos);
            buf.writeIdentifier(Registries.PARTICLE_TYPE.getId(effect.getType()));

            for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, pos)) {
                ServerPlayNetworking.send(player, ArcaneRepository.BLOCK_PARTICLES_CHANNEL, buf);
            }
        }
    }

    // If ever in need of epic sounds from vanilla, use these:
    // https://discord.com/channels/507304429255393322/511324119728521285/996159997325090897
    public static void lecternPlonk(World world, Vec3d pos, float pitch, boolean particles) {
        world.playSound(null, pos.getX(), pos.getY(), pos.getZ(),
                SoundEvents.BLOCK_AMETHYST_BLOCK_STEP, SoundCategory.BLOCKS,
                0.5f, pitch + world.getRandom().nextFloat() * 0.4f);
        if (particles) ((ServerWorld) world).spawnParticles(
                ParticleTypes.SOUL_FIRE_FLAME, pos.getX(), pos.getY(), pos.getZ(),
                5, 0, 0, 0, 0.1);
    }

    public static void drawParticleCircle(int tick, World world, Vec3d pos, int cycleTicks, int cycleOffset, double radius) {
        double animationPos = (tick + cycleOffset) % cycleTicks / ((double) cycleTicks) * (2 * Math.PI);
        Vec3d particlePos = pos.add(radius * Math.cos(animationPos), 0, radius * Math.sin(animationPos));
        ((ServerWorld) world).spawnParticles(
                ParticleTypes.ENCHANT, particlePos.getX(), particlePos.getY(), particlePos.getZ(),
                0, 0, 0, 0, 0
        );
    }
}
