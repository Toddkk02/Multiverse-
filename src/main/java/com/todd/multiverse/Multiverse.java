package com.todd.multiverse;

import com.todd.multiverse.blocks.ModBlocks;
import com.todd.multiverse.blocks.entity.BulletEntity;
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
	public static final String MOD_ID = "multiverse"; // ID del mod
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final EntityType<BulletEntity> BULLET_ENTITY = Registry.register(
			Registry.ENTITY_TYPE,
			new Identifier(MOD_ID, "bullet"),
			FabricEntityTypeBuilder.<BulletEntity>create(SpawnGroup.MISC, BulletEntity::new)
					.dimensions(EntityDimensions.fixed(0.25F, 0.25F)) // Imposta le dimensioni dell'entità
					.trackRangeBlocks(64) // Raggio di tracciamento
					.trackedUpdateRate(1) // Frequenza di aggiornamento
					.build()
	);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Multiverse Mod!");
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerBlockEntities();
		ModScreenHandlers.registerAllScreenHandlers();

		// Registra il tipo di ricetta e il serializer
		registerRecipes();
	}

	private void registerRecipes() {
		Registry.register(Registry.RECIPE_TYPE, new Identifier(MOD_ID, FluidDistillerRecipe.Type.ID), FluidDistillerRecipe.Type.INSTANCE);
		Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MOD_ID, FluidDistillerRecipe.Serializer.ID), FluidDistillerRecipe.Serializer.INSTANCE);
		LOGGER.info("Registered custom recipes for Multiverse!");
	}
}

