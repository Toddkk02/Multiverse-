package com.todd.multiverse.world.surface;

import com.todd.multiverse.Multiverse;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.minecraft.block.Blocks;

public class ModSurfaceBuilders {
    private static final MaterialRules.MaterialRule CRYSTAL_HILLS = MaterialRules.sequence(
            // Bedrock floor
            MaterialRules.condition(
                    MaterialRules.surface(),
                    MaterialRules.block(Blocks.PINK_CONCRETE_POWDER.getDefaultState())
            ),
            // Surface layer - Pink Concrete (5 blocks)
            MaterialRules.condition(
                    MaterialRules.surface(),
                    MaterialRules.sequence(
                            MaterialRules.condition(
                                    MaterialRules.STONE_DEPTH_FLOOR,
                                    MaterialRules.block(Blocks.PINK_CONCRETE.getDefaultState())
                            )
                    )
            ),
            // Under layer - Pink Concrete Powder
            MaterialRules.condition(
                    MaterialRules.STONE_DEPTH_FLOOR_WITH_SURFACE_DEPTH,
                    MaterialRules.block(Blocks.PINK_CONCRETE_POWDER.getDefaultState())
            ),
            // Base layer - Blue Stone
            MaterialRules.block(Registry.BLOCK.get(new Identifier(Multiverse.MOD_ID, "blue_stone")).getDefaultState())
    );

    public static void registerSurfaceBuilders() {
        Multiverse.LOGGER.info("Registering Surface Builders for " + Multiverse.MOD_ID);
    }

    public static MaterialRules.MaterialRule createSurfaceRule() {
        return CRYSTAL_HILLS;
    }
}