package com.todd.multiverse.world.biomes;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.sound.BiomeMoodSound;

public class CrystalHillsBiome {
    public static final RegistryKey<Biome> CRYSTAL_HILLS = RegistryKey.of(
            Registry.BIOME_KEY,
            new Identifier("multiverse", "crystal_hills")
    );

    public static Biome create() {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();

        GenerationSettings.Builder generationSettings = new GenerationSettings.Builder();

        return new Biome.Builder()
                .precipitation(Biome.Precipitation.NONE)
                .temperature(2.0f)
                .downfall(0.0f)
                .effects(new BiomeEffects.Builder()
                        .waterColor(4159204)
                        .waterFogColor(329011)
                        .fogColor(12638463)
                        .skyColor(7842047)
                        .moodSound(BiomeMoodSound.CAVE)
                        .build())
                .spawnSettings(spawnSettings.build())
                .generationSettings(generationSettings.build())
                .build();
    }
}