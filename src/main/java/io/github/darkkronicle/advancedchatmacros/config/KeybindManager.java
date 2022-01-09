package io.github.darkkronicle.advancedchatmacros.config;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.FileConfig;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.hotkeys.IKeybindManager;
import fi.dy.masa.malilib.hotkeys.IKeybindProvider;
import fi.dy.masa.malilib.hotkeys.IKeyboardInputHandler;
import fi.dy.masa.malilib.hotkeys.IMouseInputHandler;
import fi.dy.masa.malilib.util.FileUtils;
import io.github.darkkronicle.advancedchatmacros.AdvancedChatMacros;
import io.github.darkkronicle.advancedchatmacros.CommandKeybind;
import io.github.darkkronicle.advancedchatmacros.util.TomlUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KeybindManager implements IKeybindProvider, IKeyboardInputHandler, IMouseInputHandler {

    private final static KeybindManager INSTANCE = new KeybindManager();

    public static KeybindManager getInstance() {
        return INSTANCE;
    }

    private List<CommandKeybind> keybinds = new ArrayList<>();
    private File configDirectory = FileUtils.getConfigDirectory().toPath().resolve(AdvancedChatMacros.MOD_ID).toFile();
    private File keyBindFile = FileUtils.getConfigDirectory().toPath().resolve(AdvancedChatMacros.MOD_ID).resolve("keybinds.toml").toFile();

    public void load() {
        keybinds.clear();
        configDirectory.mkdirs();

        if (keyBindFile.exists()) {
            loadFile();
        }

        // Clear the old ones and redo
        InputEventHandler.getKeybindManager().updateUsedKeys();
    }

    private void loadFile() {
        FileConfig fileConfig = TomlUtils.loadFile(keyBindFile);
        fileConfig.load();

        Optional<List<Config>> keybindConfigs = fileConfig.getOptional("keybind");
        if (keybindConfigs.isPresent()) {
            for (Config config : keybindConfigs.get()) {
                loadKeybind(config);
            }
        }

        fileConfig.clear();
    }

    private void loadKeybind(Config config) {
        keybinds.add(CommandKeybind.fromToml(config));
    }

    @Override
    public void addKeysToMap(IKeybindManager manager) {
        for (CommandKeybind keybind : keybinds) {
            manager.addKeybindToMap(keybind.getKeybind());
        }
    }

    @Override
    public void addHotkeys(IKeybindManager manager) {

    }
}
