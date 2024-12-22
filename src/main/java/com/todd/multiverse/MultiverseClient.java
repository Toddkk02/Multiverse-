package com.todd.multiverse;

import com.todd.multiverse.registry.ModEntities;
import com.todd.multiverse.render.ProjectileEntityRenderer;
import com.todd.multiverse.screen.FluidDistillerScreen;
import com.todd.multiverse.screen.ModScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class MultiverseClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Registrazione delle schermate per il FluidDistiller
        HandledScreens.register(ModScreenHandlers.FLUID_DISTILLER, FluidDistillerScreen::new);
        EntityRendererRegistry.register(ModEntities.BULLET, ProjectileEntityRenderer::new);
        // Registrazione del renderer per l'entità BulletEntity
        // Assicurati che "ProjectileEntityRenderer" sia implementato correttamente
    }
}
