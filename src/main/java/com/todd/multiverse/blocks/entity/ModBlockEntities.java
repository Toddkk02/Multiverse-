package com.todd.multiverse.blocks.entity;

import com.todd.multiverse.Multiverse;
import com.todd.multiverse.blocks.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlockEntities {
    public static BlockEntityType<PortalBlockEntity> PORTAL_BLOCK_ENTITY;
    public static BlockEntityType<FluidDistillerBlockEntity> FLUID_DISTILLER;

    public static void registerBlockEntities() {
        PORTAL_BLOCK_ENTITY = Registry.register(
                Registry.BLOCK_ENTITY_TYPE,
                new Identifier(Multiverse.MOD_ID, "portal_block_entity"),
                FabricBlockEntityTypeBuilder.create(
                        PortalBlockEntity::new,
                        ModBlocks.PORTAL
                ).build()
        );

        FLUID_DISTILLER = Registry.register(
                Registry.BLOCK_ENTITY_TYPE,
                new Identifier(Multiverse.MOD_ID, "fluid_distiller"),
                FabricBlockEntityTypeBuilder.create(
                        FluidDistillerBlockEntity::new,
                        ModBlocks.FLUID_DISTILLER
                ).build()
        );

        Multiverse.LOGGER.info("Registering BlockEntities for " + Multiverse.MOD_ID);
    }
}