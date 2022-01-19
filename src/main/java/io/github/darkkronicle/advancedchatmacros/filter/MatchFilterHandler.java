package io.github.darkkronicle.advancedchatmacros.filter;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.FileConfig;
import fi.dy.masa.malilib.util.FileUtils;
import io.github.darkkronicle.advancedchatcore.AdvancedChatCore;
import io.github.darkkronicle.advancedchatcore.interfaces.IStringFilter;
import io.github.darkkronicle.advancedchatmacros.AdvancedChatMacros;
import io.github.darkkronicle.advancedchatmacros.util.TomlUtils;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MatchFilterHandler implements IStringFilter {

    private final static MatchFilterHandler INSTANCE = new MatchFilterHandler();

    private List<MatchFilter> filters = new ArrayList<>();
    private File filtersFile = FileUtils.getConfigDirectory().toPath().resolve("advancedchat").resolve(AdvancedChatMacros.MOD_ID).resolve("filters.knst").toFile();

    public static MatchFilterHandler getInstance() {
        return INSTANCE;
    }

    private MatchFilterHandler() {}

    public void load() {
        filters.clear();
        File exampleFile = FileUtils.getConfigDirectory().toPath().resolve("advancedchat").resolve(AdvancedChatMacros.MOD_ID).resolve("example_filters.knst").toFile();
        if (!filtersFile.exists() && !exampleFile.exists()) {
            // Copy examples if filters and the example filters don't exist
            try {
                org.apache.commons.io.FileUtils.copyInputStreamToFile(AdvancedChatCore.getResource("example_filters.knst"), exampleFile);
            } catch (IOException | URISyntaxException e) {
                AdvancedChatMacros.LOGGER.log(Level.WARN, "Example filters failed to copy!", e);
                return;
            }
            AdvancedChatMacros.LOGGER.log(Level.INFO, "example_filters.knst was successfully created!");
            return;
        }
        if (!filtersFile.exists()) {
            return;
        }
        List<String> lines;
        try {
            lines = Files.readAllLines(filtersFile.toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            AdvancedChatMacros.LOGGER.log(Level.WARN, "Error loading filters.knst", e);
            return;
        }
        if (lines.size() == 0) {
            return;
        }
        StringBuilder raw = new StringBuilder();
        for (String line : lines) {
            // We don't want newlines since this is all within chat box
            if (!line.startsWith("#") && line.trim().length() > 0) {
                // Not a comment
                raw.append(line).append(' ');
            }
        }
        if (raw.length() == 0) {
            return;
        }
        // Grab everything but the last space
        String text = raw.substring(0, raw.length() - 1);
        // 5 or more dashes
        String[] filters = text.split("-{5,}");
        for (String filter : filters) {
            loadFilter(filter);
        }
    }

    private void loadFilter(String filter) {
        filter = filter.strip();
        if (filter.length() == 0) {
            // Blank
            return;
        }
        filters.add(new MatchFilter(filter));
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
