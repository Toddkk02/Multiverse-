package com.todd.multiverse;

import com.todd.multiverse.blocks.ModBlocks;
import com.todd.multiverse.item.ModItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Multiverse implements ModInitializer {
	public static final String MOD_ID = "multiverse"; // ID del mod
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// Inizializza e registra gli oggetti
		LOGGER.info("Initializing Multiverse Mod!");
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
	}
}
