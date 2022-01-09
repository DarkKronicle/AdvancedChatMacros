package io.github.darkkronicle.advancedchatmacros.config;

import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.util.FileUtils;
import io.github.darkkronicle.advancedchatmacros.AdvancedChatMacros;

import java.io.File;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class MacrosConfigStorage implements IConfigHandler {

    public static final String CONFIG_FILE_NAME = AdvancedChatMacros.MOD_ID + ".json";
    private static final int CONFIG_VERSION = 1;

    public static void loadFromFile() {

        File configFile =
                FileUtils.getConfigDirectory()
                        .toPath()
                        .resolve("advancedchat")
                        .resolve(CONFIG_FILE_NAME)
                        .toFile();

        if (configFile.exists() && configFile.isFile() && configFile.canRead()) {

        }
    }

    public static void saveFromFile() {
        File dir = FileUtils.getConfigDirectory().toPath().resolve("advancedchat").toFile();

        if ((dir.exists() && dir.isDirectory()) || dir.mkdirs()) {

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
