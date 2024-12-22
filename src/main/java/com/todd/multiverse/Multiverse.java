package com.todd.multiverse;

import com.todd.multiverse.blocks.ModBlocks;
import com.todd.multiverse.blocks.entity.ModBlockEntities;
import com.todd.multiverse.item.ModItems;
import com.todd.multiverse.item.custom.PortalGun;
import com.todd.multiverse.recipe.FluidDistillerRecipe;
import com.todd.multiverse.screen.ModScreenHandlers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Multiverse implements ModInitializer {
	public static final String MOD_ID = "multiverse";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Multiverse Mod!");

		// Prima registriamo i blocchi, poi le block entities
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerBlockEntities();

		// Poi registriamo gli items
		ModItems.registerModItems();


		// Screen handlers e ricette
		ModScreenHandlers.registerAllScreenHandlers();
		registerRecipes();

		LOGGER.info("Multiverse Mod Initialized Successfully!");
	}

	private void registerPortalGun() {
		// Registra il PortalGun con le sue impostazioni
		Registry.register(
				Registry.ITEM,
				new Identifier(MOD_ID, "portal_gun"),
				new PortalGun(new Item.Settings()
						.group(ItemGroup.MISC)
						.maxCount(1) // Solo uno per slot
						.maxDamage(100)) // Durabilità opzionale
		);
		LOGGER.info("Portal Gun Registered Successfully!");
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
}