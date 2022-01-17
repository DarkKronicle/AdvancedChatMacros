package io.github.darkkronicle.advancedchatmacros.config;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.util.FileUtils;
import io.github.darkkronicle.advancedchatcore.config.ConfigStorage;
import io.github.darkkronicle.advancedchatcore.config.SaveableConfig;
import io.github.darkkronicle.advancedchatmacros.AdvancedChatMacros;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;

import io.github.darkkronicle.advancedchatmacros.config.options.BooleanTomlOption;
import io.github.darkkronicle.advancedchatmacros.config.options.TomlOption;
import io.github.darkkronicle.advancedchatmacros.util.TomlUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.logging.log4j.Level;

@Environment(EnvType.CLIENT)
public class MacrosConfigStorage implements IConfigHandler {

    public static final String CONFIG_FILE_NAME = AdvancedChatMacros.MOD_ID + ".toml";
    private static final int CONFIG_VERSION = 1;

    public static class General {

        public static String NAME = "general";

        private static String translate(String key) {
            return "advancedchatmacros.config.general." + key;
        }

        public static SaveableConfig<BooleanTomlOption> KONSTRUCT_ENABLED = SaveableConfig.fromConfig("konstructEnabled",
                new BooleanTomlOption(translate("konstructenabled"), true, translate("info.konstructenabled")));

        public static SaveableConfig<BooleanTomlOption> PREVENT_MACRO_RECURSION = SaveableConfig.fromConfig("preventRecursion",
                new BooleanTomlOption(translate("preventrecursion"), true, translate("info.preventrecursion")));

        public static ImmutableList<SaveableConfig<? extends TomlOption<?>>> OPTIONS = ImmutableList.of(KONSTRUCT_ENABLED, PREVENT_MACRO_RECURSION);
    }

    public static void loadFromFile() {

        File configFile = FileUtils.getConfigDirectory().toPath().resolve("advancedchat").resolve(CONFIG_FILE_NAME).toFile();

        if (configFile.exists() && configFile.isFile() && configFile.canRead()) {
            FileConfig config = TomlUtils.loadFile(configFile);
            config.load();
            Optional<Config> general = config.getOptional(ConfigStorage.General.NAME);
            if (general.isEmpty()) {
                config.close();
                return;
            }
            for (SaveableConfig<? extends TomlOption<?>> option : General.OPTIONS) {
                try {
                    option.config.setValueFromToml(config.getOptional(Arrays.asList(General.NAME, option.key)));
                } catch (ClassCastException e) {
                    AdvancedChatMacros.LOGGER.log(Level.WARN, "Error getting value " + option.key, e);
                }
            }
            config.close();
        }
    }

    public static void saveFromFile() {
        File dir = FileUtils.getConfigDirectory().toPath().resolve("advancedchat").toFile();

        if ((dir.exists() && dir.isDirectory()) || dir.mkdirs()) {
            File file = dir.toPath().resolve(CONFIG_FILE_NAME).toFile();
            FileConfig config = TomlUtils.loadFile(file);
            config.load();
            for (SaveableConfig<? extends TomlOption<?>> option : General.OPTIONS) {
                config.set(Arrays.asList(General.NAME, option.key), option.config.getToml());
            }
            config.save();
            config.close();
        }
    }

    @Override
    public void load() {
        loadFromFile();
    }

    @Override
    public void save() {
        saveFromFile();
    }
}
