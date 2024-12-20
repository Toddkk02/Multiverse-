package com.todd.multiverse.blocks;

import com.todd.multiverse.Multiverse;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
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

    public static final Block FLUID_DISTILLER = registerBlock("fluid_distiller",
            new Block(Block.Settings.of(Material.METAL)));

    public static final Block PHOSPHORUS_PRECURSOR = registerBlock("phosphorus_precursor",
            new Block(Block.Settings.of(Material.METAL)));

    // Metodo per registrare tutti i blocchi)

    // Metodo per registrare un singolo)
    // Metodo per registrare il blocco e l'oggetto associato
    private static Block registerBlock(String name, Block block) {
        // Registrazione del blocco
        Registry.register(Registry.BLOCK, new Identifier(Multiverse.MOD_ID, name), block);

        // Registrazione dell'oggetto associato al blocco (BlockItem)
        Registry.register(Registry.ITEM, new Identifier(Multiverse.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)));

        return block;
    }

    // Metodo di registrazione di tutti i blocchi
    public static void registerModBlocks() {
        // Registrazione del blocco (in questo caso il blocco GREEN_ROCK_ORE)
        Multiverse.LOGGER.debug("Registering mod blocks for " + Multiverse.MOD_ID);
        // Qui puoi aggiungere altre registrazioni se hai altri blocchi da registrare
    }
}
