package com.todd.multiverse.world.dimension;

import com.todd.multiverse.Multiverse;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;

public class ModDimension {
    public static void register() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            ServerWorld crystalHills = server.getWorld(CrystalHillsDimension.DIMENSION_KEY);
            if (crystalHills != null) {
                Multiverse.LOGGER.info("Crystal Hills dimension registered successfully!");
            }
        });
    }
}