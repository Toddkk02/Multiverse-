package com.todd.multiverse.world.biomes;

import com.todd.multiverse.Multiverse;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.sound.BiomeMoodSound;

public class ModBiomes {
    public static final RegistryKey<Biome> CRYSTAL_HILLS_KEY = RegistryKey.of(
            Registry.BIOME_KEY,
            new Identifier(Multiverse.MOD_ID, "crystal_hills")
    );

    public static Biome createCrystalHillsBiome() {
        // Spawn settings builder with default mob spawns
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        spawnSettings.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.ZOMBIE, 100, 4, 4));
        spawnSettings.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.SKELETON, 100, 4, 4));

        // Generation settings builder
        GenerationSettings.Builder generationSettings = new GenerationSettings.Builder();

        return new Biome.Builder()
                .precipitation(Biome.Precipitation.NONE)
                .temperature(0.8f)
                .downfall(0.4f)
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

    public static void register() {
        Multiverse.LOGGER.info("Registering ModBiomes for " + Multiverse.MOD_ID);
    }
}
