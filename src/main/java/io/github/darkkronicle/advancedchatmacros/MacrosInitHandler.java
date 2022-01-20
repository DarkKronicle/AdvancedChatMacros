package io.github.darkkronicle.advancedchatmacros;

import com.mojang.brigadier.tree.CommandNode;
import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;
import io.github.darkkronicle.advancedchatcore.ModuleHandler;
import io.github.darkkronicle.advancedchatcore.chat.MessageSender;
import io.github.darkkronicle.advancedchatcore.config.CommandsHandler;
import io.github.darkkronicle.advancedchatmacros.config.KeybindManager;
import io.github.darkkronicle.advancedchatmacros.config.MacrosConfigStorage;
import io.github.darkkronicle.advancedchatmacros.filter.KonstructFilter;
import io.github.darkkronicle.advancedchatmacros.filter.MatchFilterHandler;
import io.github.darkkronicle.advancedchatmacros.impl.FiltersImpl;
import io.github.darkkronicle.kommandlib.CommandManager;
import io.github.darkkronicle.kommandlib.command.ClientCommand;
import io.github.darkkronicle.kommandlib.util.CommandUtil;
import io.github.darkkronicle.kommandlib.util.InfoUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.Level;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class MacrosInitHandler implements IInitializationHandler {

    @Override
    public void registerModHandlers() {
        ConfigManager.getInstance().registerConfigHandler(AdvancedChatMacros.MOD_ID, new MacrosConfigStorage());
        MessageSender.getInstance().addFilter(input -> {
            if (input.equals("[[reloadMacros]]")) {
                AdvancedChatMacros.reloadFilters(true);
                return Optional.of("");
            }
            if (input.equals("[[reloadKeybinds]]")) {
                AdvancedChatMacros.reloadKeybinds(true);
                return Optional.of("");
            }
            return Optional.empty();
        });
        MessageSender.getInstance().addFilter(KonstructFilter.getInstance());

        try {
            CommandNode<ServerCommandSource> node = CommandsHandler.getInstance().getOrCreateSubs("macros");
            node.addChild(CommandUtil.literal("reloadToml").executes(ClientCommand.of((context) -> AdvancedChatMacros.reloadFilters(true))).build());
        } catch (Exception e) {
            AdvancedChatMacros.LOGGER.log(Level.WARN, "Could not set up command!");
        }

        MatchFilterHandler.getInstance().load();
        MessageSender.getInstance().addFilter(MatchFilterHandler.getInstance());

        KeybindManager.getInstance().load();

        InputEventHandler.getKeybindManager().registerKeybindProvider(KeybindManager.getInstance());
        InputEventHandler.getInputManager().registerMouseInputHandler(KeybindManager.getInstance());
        InputEventHandler.getInputManager().registerKeyboardInputHandler(KeybindManager.getInstance());

        if (ModuleHandler.getInstance().fromId("advancedchatfilters").isPresent()) {
            FiltersImpl.getInstance().init();
        }
    }
}
