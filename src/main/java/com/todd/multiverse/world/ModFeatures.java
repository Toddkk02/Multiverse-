package com.todd.multiverse.world;

import com.todd.multiverse.structures.CrystalSpikeFeature;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.*;

import java.util.List;

public class ModFeatures {
    public static final String MOD_ID = "multiverse";

    // Define your custom feature
    public static final Feature<DefaultFeatureConfig> CRYSTAL_SPIKE =
            new CrystalSpikeFeature(DefaultFeatureConfig.CODEC);

    // Define the ConfiguredFeature
    public static final ConfiguredFeature<?, ?> CRYSTAL_SPIKE_CONFIGURED =
            new ConfiguredFeature<>(CRYSTAL_SPIKE, DefaultFeatureConfig.INSTANCE);

    // Define the PlacedFeature with RegistryEntry
    public static final PlacedFeature CRYSTAL_SPIKE_PLACED = new PlacedFeature(
            RegistryEntry.of(CRYSTAL_SPIKE_CONFIGURED),
            List.of(
                    CountPlacementModifier.of(25), // Aumentato da 15 a 25
                    RarityFilterPlacementModifier.of(2), // Ridotto da 4 a 2 per renderli più comuni
                    SquarePlacementModifier.of(),
                    HeightRangePlacementModifier.uniform(
                            YOffset.fixed(55), // Abbassato da 60 a 55
                            YOffset.fixed(70)  // Abbassato da 100 a 70
                    ),
                    BiomePlacementModifier.of()
            )
    );

    public static void register() {
        // Register the custom feature
        Registry.register(
                Registry.FEATURE,
                new Identifier(MOD_ID, "crystal_spike"),
                CRYSTAL_SPIKE
        );

        // Register the configured feature
        Registry.register(
                BuiltinRegistries.CONFIGURED_FEATURE,
                new Identifier(MOD_ID, "crystal_spike"),
                CRYSTAL_SPIKE_CONFIGURED
        );

        // Register the placed feature
        Registry.register(
                BuiltinRegistries.PLACED_FEATURE,
                new Identifier(MOD_ID, "crystal_spike_placed"),
                CRYSTAL_SPIKE_PLACED
        );

        System.out.println("Registered Crystal Spike features for " + MOD_ID);
    }
}
