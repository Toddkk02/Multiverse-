package com.todd.multiverse.screen;
import com.todd.multiverse.screen.FluidDistillerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModScreenHandlers {
    public static ScreenHandlerType<FluidDistillerScreenHandler> FLUID_DISTILLER;

    public static void registerAllScreenHandlers() {
        FLUID_DISTILLER = Registry.register(
                Registry.SCREEN_HANDLER,
                new Identifier("multiverse", "fluid_distiller"),
                new ScreenHandlerType<>(FluidDistillerScreenHandler::new)
        );
    }
}