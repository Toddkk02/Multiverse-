package com.todd.multiverse.world.dimension;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;

public class CrystalHillsDimension {
    public static final String MOD_ID = "multiverse";
    public static final Identifier DIMENSION_ID = new Identifier(MOD_ID, "crystal_hills");
    public static final RegistryKey<World> DIMENSION_KEY = RegistryKey.of(Registry.WORLD_KEY, DIMENSION_ID);
    public static final RegistryKey<DimensionType> DIMENSION_TYPE_KEY = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, DIMENSION_ID);
}