package com.todd.multiverse.item;

import com.todd.multiverse.Multiverse;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {

    // Dichiarazione e registrazione degli oggetti
    public static final Item GREEN_ROCK = registerItem("green_rock",
            new Item(new FabricItemSettings().group(ItemGroup.MISC)));

    public static final Item GREEN_POWDER = registerItem("green_powder",
            new Item(new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item TUBE_OF_COPPER = registerItem("tube_of_copper",
            new Item(new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item CONTAINER = registerItem("container",
            new Item(new FabricItemSettings().group(ItemGroup.MISC)));

    // Metodo per registrare un singolo oggetto e l'oggetto associato)

    // Metodo per registrare tutti gli oggetti)
    // Metodo per registrare un singolo oggetto
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(Multiverse.MOD_ID, name), item);
    }

    // Metodo per registrare tutti gli oggetti
    public static void registerModItems() {
        Multiverse.LOGGER.debug("Registering Mod Items for " + Multiverse.MOD_ID);
        // Aggiungi qui eventuali log o registrazioni aggiuntive se necessario
    }
}