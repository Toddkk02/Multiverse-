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
/*
    public static final Block PINK_SAND = registerBlock("pink_sand",
            new Block(FabricBlockSettings.of(Material.STONE).strength(0.5F)));  // Manually specify Material.SAND behavior

    public static final Block PINK_SANDSTONE = registerBlock("pink_sandstone",
            new Block(FabricBlockSettings.of(Material.STONE).strength(1.5F)));
*/
    public static final Block BLUE_STONE = registerBlock("blue_stone",
            new Block(FabricBlockSettings.of(Material.STONE).strength(3.0F)));



    public static final Block BLUE_CRYSTAL = registerBlock("blue_crystal",
            new Block(FabricBlockSettings.of(Material.GLASS).strength(2.0F).luminance(15)));

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
