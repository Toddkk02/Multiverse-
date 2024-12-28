package com.todd.multiverse.world.dimension;

import com.todd.multiverse.Multiverse;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.block.Block;
import net.minecraft.block.Material;

public class ModDimension {
    // Custom Blocks

    // Dimensions
    public static final RegistryKey<World> CRYSTAL_DESERT = registerDimension("crystal_desert");
    public static final RegistryKey<World> RAINBOW_FOREST = registerDimension("rainbow_forest");
    public static final RegistryKey<World> SHADOW_PEAKS = registerDimension("shadow_peaks");
    public static final RegistryKey<World> FLOATING_ISLANDS = registerDimension("floating_islands");
    public static final RegistryKey<World> MUSHROOM_CAVES = registerDimension("mushroom_caves");
    public static final RegistryKey<World> ICE_SPIKES = registerDimension("ice_spikes");
    public static final RegistryKey<World> LAVA_LAKES = registerDimension("lava_lakes");
    public static final RegistryKey<World> CLOUD_REALM = registerDimension("cloud_realm");
    public static final RegistryKey<World> VOID_PLAINS = registerDimension("void_plains");
    public static final RegistryKey<World> CORAL_HEIGHTS = registerDimension("coral_heights");

    private static RegistryKey<World> registerDimension(String name) {
        return RegistryKey.of(Registry.WORLD_KEY, new Identifier(Multiverse.MOD_ID, name));
    }

    public static void register(){
        Multiverse.LOGGER.debug("Register dimension");
    }
}