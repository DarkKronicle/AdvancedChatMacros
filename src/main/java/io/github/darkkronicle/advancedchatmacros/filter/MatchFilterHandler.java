package io.github.darkkronicle.advancedchatmacros.filter;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.FileConfig;
import fi.dy.masa.malilib.util.FileUtils;
import io.github.darkkronicle.advancedchatcore.AdvancedChatCore;
import io.github.darkkronicle.advancedchatcore.interfaces.IStringFilter;
import io.github.darkkronicle.advancedchatcore.util.FindPair;
import io.github.darkkronicle.advancedchatcore.util.FindType;
import io.github.darkkronicle.advancedchatmacros.AdvancedChatMacros;
import io.github.darkkronicle.advancedchatmacros.util.TomlUtils;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MatchFilterHandler implements IStringFilter {

    private final static MatchFilterHandler INSTANCE = new MatchFilterHandler();

    private List<MatchFilter> filters = new ArrayList<>();
    private File filtersFile = FileUtils.getConfigDirectory().toPath().resolve("advancedchat").resolve(AdvancedChatMacros.MOD_ID).resolve("filters.toml").toFile();

    public static MatchFilterHandler getInstance() {
        return INSTANCE;
    }

    private MatchFilterHandler() {}

    public void load() {
        filters.clear();
        if (!filtersFile.exists() && !FileUtils.getConfigDirectory().toPath().resolve("advancedchat").resolve(AdvancedChatMacros.MOD_ID).resolve("example_filters.toml").toFile().exists()) {
            // Copy examples if filters and the example filters don't exist
            try {
                org.apache.commons.io.FileUtils.copyInputStreamToFile(AdvancedChatCore.getResource("example_filters.toml"), filtersFile);
            } catch (IOException | URISyntaxException e) {
                AdvancedChatMacros.LOGGER.log(Level.WARN, "Example filters failed to copy!", e);
                return;
            }
            AdvancedChatMacros.LOGGER.log(Level.INFO, "example_filters.toml was successfully created!");
            return;
        }
        FileConfig config = TomlUtils.loadFile(filtersFile);
        Optional<List<Config>> filters = config.getOptional("filter");
        if (filters.isPresent()) {
            for (Config filter : filters.get()) {
                loadFilter(filter);
            }
        }
        config.close();
    }

    private void loadFilter(Config config) {
        filters.add(new MatchFilter(config.get("replace")));
    }

    @Override
    public Optional<String> filter(String input) {
        String unfiltered = input;
        for (MatchFilter filter : filters) {
            Optional<String> changed = filter.filter(input);
            if (changed.isPresent()) {
                input = changed.get();
            }
        }
        if (input.equals(unfiltered)) {
            return Optional.empty();
        }
        return Optional.of(input);
    }
}
