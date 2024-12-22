package com.todd.multiverse;

import com.todd.multiverse.blocks.ModBlocks;
import com.todd.multiverse.blocks.entity.ModBlockEntities;
import com.todd.multiverse.item.ModItems;
import com.todd.multiverse.item.custom.PortalGun;
import com.todd.multiverse.recipe.FluidDistillerRecipe;
import com.todd.multiverse.screen.ModScreenHandlers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Multiverse implements ModInitializer {
	public static final String MOD_ID = "multiverse"; // Mod ID
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// Register BulletEntity with a unique entity type
	

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Multiverse Mod!");

		// Register mod items, blocks, entities, etc.
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerBlockEntities();
		ModScreenHandlers.registerAllScreenHandlers();

		// Register recipes
		registerRecipes();

		// Register portal gun as an item
		registerPortalGunItem();

		LOGGER.info("Multiverse Mod Initialized!");
	}

	private void registerRecipes() {
		Registry.register(Registry.RECIPE_TYPE, new Identifier(MOD_ID, FluidDistillerRecipe.Type.ID), FluidDistillerRecipe.Type.INSTANCE);
		Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MOD_ID, FluidDistillerRecipe.Serializer.ID), FluidDistillerRecipe.Serializer.INSTANCE);
		LOGGER.info("Registered custom recipes for Multiverse!");
	}

	private void registerPortalGunItem() {
		// Register the PortalGun item
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "portalgun"), new PortalGun(new Item.Settings().group(ItemGroup.MISC)));
		LOGGER.info("PortalGun Item Registered!");
	}
}
