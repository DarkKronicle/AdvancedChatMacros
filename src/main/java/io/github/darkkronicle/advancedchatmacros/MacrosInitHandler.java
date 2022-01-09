package io.github.darkkronicle.advancedchatmacros;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;
import io.github.darkkronicle.advancedchatcore.chat.MessageSender;
import io.github.darkkronicle.advancedchatmacros.config.KeybindManager;
import io.github.darkkronicle.advancedchatmacros.config.MacrosConfigStorage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class MacrosInitHandler implements IInitializationHandler {

    @Override
    public void registerModHandlers() {
        ConfigManager.getInstance()
                .registerConfigHandler(AdvancedChatMacros.MOD_ID, new MacrosConfigStorage());
        MessageSender.getInstance().addFilter(KonstructFilter.getInstance());

        KeybindManager.getInstance().load();

        InputEventHandler.getKeybindManager().registerKeybindProvider(KeybindManager.getInstance());
        InputEventHandler.getInputManager().registerMouseInputHandler(KeybindManager.getInstance());
        InputEventHandler.getInputManager().registerKeyboardInputHandler(KeybindManager.getInstance());
    }
}
