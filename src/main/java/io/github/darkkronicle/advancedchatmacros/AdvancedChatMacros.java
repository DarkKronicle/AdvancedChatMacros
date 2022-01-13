package io.github.darkkronicle.advancedchatmacros;

import fi.dy.masa.malilib.event.InitializationHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public class AdvancedChatMacros implements ClientModInitializer {

    public static final String MOD_ID = "advancedchatmacros";

    @Override
    public void onInitializeClient() {
        // This will run after AdvancedChatCore's because of load order
        InitializationHandler.getInstance().registerInitializationHandler(new MacrosInitHandler());
    }
}
