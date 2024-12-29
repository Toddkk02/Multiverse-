package com.todd.multiverse.world.dimension;

import com.todd.multiverse.Multiverse;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class CrystalHillsDimension {
    public static final RegistryKey<World> DIMENSION_KEY = RegistryKey.of(
            Registry.WORLD_KEY,
            new Identifier(Multiverse.MOD_ID, "crystal_hills")
    );

    public static final RegistryKey<DimensionType> DIMENSION_TYPE_KEY = RegistryKey.of(
            Registry.DIMENSION_TYPE_KEY,
            new Identifier(Multiverse.MOD_ID, "crystal_hills")
    );

    public static void register() {
        Multiverse.LOGGER.info("Registering Crystal Hills dimension for " + Multiverse.MOD_ID);
    }
}
