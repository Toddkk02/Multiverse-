package com.todd.multiverse.screen;

import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModScreenHandlers {
    public static final ScreenHandlerType<FluidDistillerScreenHandler> FLUID_DISTILLER;
    public static final ScreenHandlerType<DestinationScreenHandler> DESTINATION_SCREEN_HANDLER;

    static {
        FLUID_DISTILLER = Registry.register(
                Registry.SCREEN_HANDLER,
                new Identifier("multiverse", "fluid_distiller"),
                new ScreenHandlerType<>(FluidDistillerScreenHandler::new)
        );

        DESTINATION_SCREEN_HANDLER = Registry.register(
                Registry.SCREEN_HANDLER,
                new Identifier("multiverse", "destination_screen"),
                new ScreenHandlerType<>(DestinationScreenHandler::new)
        );
    }
}