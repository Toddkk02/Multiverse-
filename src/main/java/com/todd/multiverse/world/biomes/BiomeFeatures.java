package com.todd.multiverse.world.biomes;

import com.todd.multiverse.world.ModFeatures;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.GenerationStep;

public class BiomeFeatures {
    public static void addCrystalSpikes(GenerationSettings.Builder builder) {
        builder.feature(
                GenerationStep.Feature.SURFACE_STRUCTURES,
                RegistryEntry.of(ModFeatures.CRYSTAL_SPIKE_PLACED)
        );
    }
}