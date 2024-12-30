package com.todd.multiverse.structures;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

public class CrystalSpikeFeature extends Feature<DefaultFeatureConfig> {

    public CrystalSpikeFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        BlockPos pos = context.getOrigin();
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();

        // Trova la superficie
        BlockPos surfacePos = world.getTopPosition(
                net.minecraft.world.Heightmap.Type.WORLD_SURFACE_WG,
                pos
        );

        // Se siamo nell'acqua, non generare
        if (world.getBlockState(surfacePos).getFluidState().isStill()) {
            return false;
        }

        // Verifica che ci sia un blocco solido sotto
        if (!world.getBlockState(surfacePos.down()).isSolidBlock(world, surfacePos)) {
            return false;
        }

        // Verifica che non ci siano altri spike nelle vicinanze
        for (int x = -10; x <= 10; x++) {
            for (int z = -10; z <= 10; z++) {
                if (x == 0 && z == 0) continue;
                BlockPos checkPos = surfacePos.add(x, 0, z);
                }
            }

        // Genera l'altezza casuale dello spike
        int height = random.nextInt(8) + 5;
        int baseRadius = random.nextInt(2) + 2;

        // Genera lo spike principale
        generateSpike(world, surfacePos, height, baseRadius, getDefaultCrystalBlock(), random);

        // Aggiungi spike secondari casuali ma più distanziati
        int secondarySpikes = random.nextInt(2) + 1; // Ridotto il numero di spike secondari
        for (int i = 0; i < secondarySpikes; i++) {
            BlockPos offset = surfacePos.add(
                    random.nextInt(15) - 7, // Aumentato il range di spawn
                    0,
                    random.nextInt(15) - 7
            );

            // Verifica che la posizione sia valida

        }
        return true;
    }

    // Metodo per verificare se c'è già uno spike nelle vicinanze

    private void generateSpike(StructureWorldAccess world, BlockPos start, int height,
                               int baseRadius, BlockState block, Random random) {
        for (int y = 0; y < height; y++) {
            float radiusMultiplier = 1.0f - ((float) y / height);
            int radius = Math.max(1, Math.round(baseRadius * radiusMultiplier));

            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    double distance = Math.sqrt(x * x + z * z);
                    if (distance <= radius + random.nextFloat() * 0.5) {
                        BlockPos currentPos = start.add(x, y, z);
                        if (world.isAir(currentPos)) {
                            world.setBlockState(currentPos, block, 3);

                            if (random.nextFloat() < 0.1f) {
                                for (Direction direction : Direction.values()) {
                                    BlockPos offsetPos = currentPos.offset(direction);
                                    if (world.isAir(offsetPos) && random.nextFloat() < 0.3f) {
                                        world.setBlockState(offsetPos, block, 3);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private BlockState getDefaultCrystalBlock() {
        return Registry.BLOCK.get(new Identifier("multiverse", "blue_crystal")).getDefaultState();
    }
}