package com.todd.multiverse.blocks.entity;

import com.todd.multiverse.Multiverse;
import com.todd.multiverse.blocks.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlockEntities {
    public static BlockEntityType<PortalBlockEntity> PORTAL_BLOCK_ENTITY;
    public static BlockEntityType<FluidDistillerBlockEntity> FLUID_DISTILLER;
    public static EntityType<com.todd.multiverse.blocks.entity.BulletEntity> PROJECTILE;

    public static void registerBlockEntities() {
        // Registrazione del blocco entità per il "Portal"
        PORTAL_BLOCK_ENTITY = Registry.register(
                Registry.BLOCK_ENTITY_TYPE,
                new Identifier(Multiverse.MOD_ID, "portal_block_entity"),
                FabricBlockEntityTypeBuilder.create(
                        PortalBlockEntity::new,
                        ModBlocks.PORTAL
                ).build(null)
        );

        // Registrazione del blocco entità per il "Fluid Distiller"
        FLUID_DISTILLER = Registry.register(
                Registry.BLOCK_ENTITY_TYPE,
                new Identifier(Multiverse.MOD_ID, "fluid_distiller"),
                FabricBlockEntityTypeBuilder.create(
                        FluidDistillerBlockEntity::new,
                        ModBlocks.FLUID_DISTILLER
                ).build(null)
        );

        // Registrazione dell'entità BulletEntity come "projectile"
        PROJECTILE = Registry.register(
                Registry.ENTITY_TYPE,
                new Identifier(Multiverse.MOD_ID, "projectile"),
                EntityType.Builder.<BulletEntity>create(BulletEntity::new, net.minecraft.entity.SpawnGroup.MISC)
                        .setDimensions(0.25F, 0.25F) // Dimensioni dell'entità
                        .maxTrackingRange(4) // Intervallo massimo di tracciamento
                        .trackingTickInterval(10) // Intervallo di tracciamento
                        .build("projectile")
        );

        // Log di conferma della registrazione delle entità e dei blocchi
        Multiverse.LOGGER.info("Registering BlockEntities for " + Multiverse.MOD_ID);
    }
}
