package com.todd.multiverse.world.biomes;

import com.todd.multiverse.Multiverse;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class ModBiomes {
    public static final RegistryKey<Biome> CRYSTAL_HILLS_KEY = RegistryKey.of(
            Registry.BIOME_KEY,
            new Identifier(Multiverse.MOD_ID, "crystal_hills")
    );

    public static void registerBiomes() {
        Registry.register(BuiltinRegistries.BIOME, CRYSTAL_HILLS_KEY.getValue(), CrystalHillsBiome.createBiome());
        Multiverse.LOGGER.info("Biomes Registered Successfully!");
    }
}
