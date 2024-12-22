package com.todd.multiverse.blocks;

import com.todd.multiverse.Multiverse;
import com.todd.multiverse.blocks.custom.FluidDistillerBlock;
import com.todd.multiverse.blocks.custom.PortalBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {
    // Dichiarazione dei blocchi
    public static final Block GREEN_ROCK_ORE = registerBlock("green_rock_ore",
            new Block(Block.Settings.of(Material.STONE)));

    public static final Block PORTAL = registerBlock("portal",
            new PortalBlock(FabricBlockSettings.of(Material.PORTAL)
                    .strength(-1.0f)
                    .nonOpaque()
                    .luminance(state -> 15)
                    .noCollision())); // Il portale non deve avere collisione

    public static final Block FLUID_DISTILLER = registerBlock("fluid_distiller",
            new FluidDistillerBlock(FabricBlockSettings.of(Material.METAL)
                    .strength(4.0f)
                    .requiresTool()));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registry.BLOCK, new Identifier(Multiverse.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(
                Registry.ITEM,
                new Identifier(Multiverse.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS))
        );
    }

    public static void registerModBlocks() {
        Multiverse.LOGGER.info("Registering ModBlocks for " + Multiverse.MOD_ID);
        // La registrazione avviene automaticamente grazie ai campi statici sopra
    }
}
