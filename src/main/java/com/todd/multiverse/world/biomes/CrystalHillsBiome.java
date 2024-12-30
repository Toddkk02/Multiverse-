package com.todd.multiverse.world.biomes;

import com.todd.multiverse.world.ModFeatures;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStep;

public class CrystalHillsBiome {
    public static Biome createBiome() {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addFarmAnimals(spawnSettings);
        DefaultBiomeFeatures.addMonsters(spawnSettings, 95, 5, 100, false);

        GenerationSettings.Builder generationSettings = new GenerationSettings.Builder();

        // Aggiungi le feature base del terreno
        DefaultBiomeFeatures.addLandCarvers(generationSettings);
        DefaultBiomeFeatures.addPlainsTallGrass(generationSettings);
        DefaultBiomeFeatures.addSprings(generationSettings);
        DefaultBiomeFeatures.addFrozenTopLayer(generationSettings);

        // Aggiungi i Crystal Spikes
        generationSettings.feature(
                GenerationStep.Feature.SURFACE_STRUCTURES,
                RegistryEntry.of(ModFeatures.CRYSTAL_SPIKE_PLACED)
        );

        return new Biome.Builder()
                .precipitation(Biome.Precipitation.RAIN)
                .temperature(0.8f)
                .downfall(0.4f)
                .effects(new BiomeEffects.Builder()
                        .waterColor(4159204)
                        .waterFogColor(329011)
                        .fogColor(12638463)
                        .skyColor(calculateSkyColor(0.8f))
                        .grassColor(9470285)
                        .foliageColor(9470285)
                        .build())
                .spawnSettings(spawnSettings.build())
                .generationSettings(generationSettings.build())
                .build();
    }

    private static int calculateSkyColor(float temperature) {
        float colorValue = temperature / 3.0F;
        colorValue = MathHelper.clamp(colorValue, -1.0F, 1.0F);
        return MathHelper.hsvToRgb(0.62222224F - colorValue * 0.05F, 0.5F + colorValue * 0.1F, 1.0F);
    }
}