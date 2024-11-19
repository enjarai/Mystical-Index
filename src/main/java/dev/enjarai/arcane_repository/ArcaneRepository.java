package dev.enjarai.arcane_repository;

import dev.enjarai.arcane_repository.block.ModBlockEntities;
import dev.enjarai.arcane_repository.block.ModBlocks;
import dev.enjarai.arcane_repository.event.LootTableEvent;
import dev.enjarai.arcane_repository.event.ModEvents;
import dev.enjarai.arcane_repository.event.ServerNetworkListeners;
import dev.enjarai.arcane_repository.item.ModDataComponentTypes;
import dev.enjarai.arcane_repository.item.ModItems;
import dev.enjarai.arcane_repository.item.ModLootTables;
import dev.enjarai.arcane_repository.item.ModRecipes;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArcaneRepository implements ModInitializer {

	public static final Logger LOGGER = LogManager.getLogger("arcane_repository");
	public static final String MOD_ID = "arcane_repository";

    @Override
	public void onInitialize() {
		ModDataComponentTypes.registerDataComponentTypes();
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerBlockEntities();
		ModLootTables.registerLootTables();
		LootTableEvent.registerLootTable();
		ModRecipes.registerModRecipes();

		ModEvents.register();
		ServerNetworkListeners.registerListeners();
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

	public static void playUISound(PlayerEntity player, SoundEvent sound, SoundCategory category, Vec3d pos) {
		playUISound(player, sound, category, pos, 0.8f);
	}

	public static void playUISound(PlayerEntity player, SoundEvent sound, SoundCategory category, Vec3d pos, float volume) {
		player.playSoundToPlayer(sound, category, volume, 0.8f + player.getWorld().getRandom().nextFloat() * 0.4f);
	}
}
