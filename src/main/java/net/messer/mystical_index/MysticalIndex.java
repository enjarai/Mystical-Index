package net.messer.mystical_index;

import net.fabricmc.api.ModInitializer;
import net.messer.mystical_index.block.ModBlockEntities;
import net.messer.mystical_index.block.ModBlocks;
import net.messer.mystical_index.event.LootTableEvent;
import net.messer.mystical_index.event.ModEvents;
import net.messer.mystical_index.event.ServerNetworkListeners;
import net.messer.mystical_index.item.ModItems;
import net.messer.mystical_index.item.ModRecipes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MysticalIndex implements ModInitializer {

	public static final Logger LOGGER = LogManager.getLogger("mystical_index");
	public static final String MOD_ID = "mystical_index";

    public static final Identifier BLOCK_PARTICLES_CHANNEL = id("block_particles");

    @Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerBlockEntities();
		ModRecipes.registerModRecipes();
		LootTableEvent.registerLootTable();

		ModEvents.register();
		ServerNetworkListeners.registerListeners();
	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}

	public static void playUISound(PlayerEntity player, SoundEvent sound, SoundCategory category, Vec3d pos) {
		playUISound(player, sound, category, pos, 0.8f);
	}

	public static void playUISound(PlayerEntity player, SoundEvent sound, SoundCategory category, Vec3d pos, float volume) {
		player.playSound(sound, category, volume, 0.8f + player.getWorld().getRandom().nextFloat() * 0.4f);
	}
}
