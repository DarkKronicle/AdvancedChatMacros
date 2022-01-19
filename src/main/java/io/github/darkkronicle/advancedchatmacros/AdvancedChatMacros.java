package io.github.darkkronicle.advancedchatmacros;

import fi.dy.masa.malilib.event.InitializationHandler;
import io.github.darkkronicle.advancedchatmacros.config.KeybindManager;
import io.github.darkkronicle.advancedchatmacros.filter.MatchFilterHandler;
import io.github.darkkronicle.kommandlib.util.InfoUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Environment(EnvType.CLIENT)
public class AdvancedChatMacros implements ClientModInitializer {

    public static final String MOD_ID = "advancedchatmacros";

    public static Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        // This will run after AdvancedChatCore's because of load order
        InitializationHandler.getInstance().registerInitializationHandler(new MacrosInitHandler());
    }

    public static void reloadFilters() {
        reloadFilters(MinecraftClient.getInstance().player != null);
    }

    public static void reloadFilters(boolean sendMessage) {
        if (sendMessage) {
            InfoUtil.sendChatMessage("Reloading macro filters...");
        }
        MatchFilterHandler.getInstance().load();
        KeybindManager.getInstance().load();
        LOGGER.log(Level.INFO, "Filters loaded");
        if (sendMessage) {
            InfoUtil.sendChatMessage("Done!", Formatting.GREEN);
        }
    }
}
