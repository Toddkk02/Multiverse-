package com.todd.multiverse;

import com.todd.multiverse.blocks.ModBlocks;
import com.todd.multiverse.blocks.entity.ModBlockEntities;
import com.todd.multiverse.item.ModItems;
import com.todd.multiverse.location.LocationManager;
import com.todd.multiverse.screen.DestinationScreenHandler;
import com.todd.multiverse.recipe.FluidDistillerRecipe;
import com.todd.multiverse.registry.ModEntities;
import com.todd.multiverse.screen.ModScreenHandlers;
import com.todd.multiverse.world.biomes.CrystalHillsBiome;
import com.todd.multiverse.world.biomes.ModBiomes;
import com.todd.multiverse.world.dimension.CrystalHillsDimension;
import com.todd.multiverse.world.dimension.ModDimension;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.util.registry.BuiltinRegistries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Multiverse implements ModInitializer {
	public static final String MOD_ID = "multiverse";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static LocationManager locationManager;

	public static final Identifier CRYSTAL_HILLS_ID = new Identifier(MOD_ID, "crystal_hills");
	public static final RegistryKey<World> CRYSTAL_HILLS_WORLD_KEY = RegistryKey.of(Registry.WORLD_KEY, CRYSTAL_HILLS_ID);
	public static final RegistryKey<DimensionType> CRYSTAL_HILLS_DIMENSION_TYPE_KEY =
			RegistryKey.of(Registry.DIMENSION_TYPE_KEY, CRYSTAL_HILLS_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Multiverse Mod!");

		// Ordine corretto di inizializzazione
		ModBiomes.registerBiomes();
		ModDimension.register();  // Registra prima le dimensioni
		initializeCore();
		registerRecipes();
		initializeLocations();

		LOGGER.info("Multiverse Mod Initialized Successfully!");
	}

	private void initializeCore() {
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerBlockEntities();
		ModItems.registerModItems();
		DestinationScreenHandler.registerNetworking();
		ModEntities.registerEntities();
		LOGGER.info("Core Systems Initialized!");
	}

	private void initializeLocations() {
		String name = "example_location";
		double x = 100.0;
		double y = 64.0;
		double z = 200.0;
		String dimension = "minecraft:overworld";

		locationManager = new LocationManager(name, x, y, z, dimension);
		LOGGER.info("Location System Initialized Successfully!");
	}

	private void registerRecipes() {
		Registry.register(
				Registry.RECIPE_TYPE,
				new Identifier(MOD_ID, FluidDistillerRecipe.Type.ID),
				FluidDistillerRecipe.Type.INSTANCE
		);

		Registry.register(
				Registry.RECIPE_SERIALIZER,
				new Identifier(MOD_ID, FluidDistillerRecipe.Serializer.ID),
				FluidDistillerRecipe.Serializer.INSTANCE
		);

		LOGGER.info("Recipes Registered Successfully!");
	}

	public static LocationManager getLocationManager() {
		return locationManager;
	}
}