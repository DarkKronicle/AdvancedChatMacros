package io.github.darkkronicle.advancedchatmacros;

import com.electronwill.nightconfig.core.Config;
import com.google.gson.JsonElement;
import fi.dy.masa.malilib.config.ConfigType;
import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import fi.dy.masa.malilib.hotkeys.KeybindMulti;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import io.github.darkkronicle.advancedchatmacros.executes.IExecute;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class CommandKeybind implements IHotkeyCallback {

    @Getter
    private final IKeybind keybind;

    @Getter
    private final List<IExecute> executes;

    public CommandKeybind(IKeybind keybind, List<IExecute> executes) {
        this.keybind = keybind;
        this.executes = executes;
    }

    @Override
    public boolean onKeyAction(KeyAction action, IKeybind key) {
        for (IExecute execute : executes) {
            execute.execute();
        }
        return true;
    }

    public static CommandKeybind fromToml(Config config) {
        KeybindSettings settings = KeybindSettings.MODIFIER_INGAME;
        IKeybind keybind = KeybindMulti.fromStorageString(config.get("keys"), settings);
        List<IExecute> executes = new ArrayList<>();
        List<Config> executeConfig = config.get("executes");
        for (Config execute : executeConfig) {
            IExecute e = IExecute.fromConfig(execute);
            if (e != null) {
                executes.add(e);
            }
        }
        CommandKeybind command = new CommandKeybind(keybind, executes);
        command.getKeybind().setCallback(command);
        return command;
    }
}
