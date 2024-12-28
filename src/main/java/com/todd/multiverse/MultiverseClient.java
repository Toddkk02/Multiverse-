package com.todd.multiverse;

import com.todd.multiverse.blocks.entity.ModBlockEntities;
import com.todd.multiverse.location.LocationManager;
import com.todd.multiverse.registry.ModEntities;
import com.todd.multiverse.render.PortalBlockEntityRenderer;
import com.todd.multiverse.render.ProjectileEntityRenderer;
import com.todd.multiverse.screen.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class MultiverseClient implements ClientModInitializer {
    private static final Identifier UPDATE_LOCATIONS_PACKET = new Identifier("multiverse", "update_locations");
    private static final Identifier UPDATE_DIMENSIONS_PACKET = new Identifier("multiverse", "update_dimensions");

    @Override
    public void onInitializeClient() {
        HandledScreens.<FluidDistillerScreenHandler, FluidDistillerScreen>register(
                ModScreenHandlers.FLUID_DISTILLER,
                FluidDistillerScreen::new
        );

        HandledScreens.<DestinationScreenHandler, DestinationScreen>register(
                ModScreenHandlers.DESTINATION_SCREEN_HANDLER,
                DestinationScreen::new
        );

        EntityRendererRegistry.register(ModEntities.BULLET, ProjectileEntityRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.PORTAL_BLOCK_ENTITY,
                (context) -> new PortalBlockEntityRenderer());

        // Register packet handlers for location and dimension updates
        ClientPlayNetworking.registerGlobalReceiver(UPDATE_LOCATIONS_PACKET, (client, handler, buf, responseSender) -> {
            int size = buf.readInt();
            List<LocationManager> locations = new ArrayList<>();

            for (int i = 0; i < size; i++) {
                String name = buf.readString();
                double x = buf.readDouble();
                double y = buf.readDouble();
                double z = buf.readDouble();
                String dimensionId = buf.readString();
                locations.add(new LocationManager(name, x, y, z, dimensionId));
            }

            client.execute(() -> {
                if (client.currentScreen instanceof DestinationScreen screen) {
                    screen.updateLocations(locations);
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(UPDATE_DIMENSIONS_PACKET, (client, handler, buf, responseSender) -> {
            int size = buf.readInt();
            List<String> dimensions = new ArrayList<>();

            for (int i = 0; i < size; i++) {
                dimensions.add(buf.readString());
            }

            client.execute(() -> {
                if (client.currentScreen instanceof DestinationScreen screen) {
                    screen.updateDimensions(dimensions);
                }
            });
        });
    }
}