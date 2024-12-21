package com.todd.multiverse.blocks.entity;

import com.todd.multiverse.blocks.ModBlocks;
import com.todd.multiverse.blocks.entity.FluidDistillerBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlockEntities {
    public static final BlockEntityType<FluidDistillerBlockEntity> FLUID_DISTILLER =
            BlockEntityType.Builder.create(
                    FluidDistillerBlockEntity::new,
                    ModBlocks.FLUID_DISTILLER // il tuo blocco
            ).build(null);

    public static void registerBlockEntities() {
        Registry.register(
                Registry.BLOCK_ENTITY_TYPE,
                new Identifier("multiverse", "fluid_distiller"),
                FLUID_DISTILLER
        );
    }
}