package io.github.darkkronicle.advancedchatmacros.filter;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.FileConfig;
import fi.dy.masa.malilib.util.FileUtils;
import io.github.darkkronicle.advancedchatcore.interfaces.IStringFilter;
import io.github.darkkronicle.advancedchatcore.util.FindType;
import io.github.darkkronicle.advancedchatmacros.AdvancedChatMacros;
import io.github.darkkronicle.advancedchatmacros.Match;
import io.github.darkkronicle.advancedchatmacros.util.TomlUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MatchFilterHandler implements IStringFilter {

    private final static MatchFilterHandler INSTANCE = new MatchFilterHandler();

    private List<MatchFilter> filters = new ArrayList<>();
    private File filtersFile = FileUtils.getConfigDirectory().toPath().resolve(AdvancedChatMacros.MOD_ID).resolve("filters.toml").toFile();

    public static MatchFilterHandler getInstance() {
        return INSTANCE;
    }

    private MatchFilterHandler() {}

    public void load() {
        filters.clear();
        if (!filtersFile.exists()) {
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
        Optional<List<Config>> matchesConfigs = config.getOptional("matches");
        if (matchesConfigs.isEmpty()) {
            // TODO flag
            return;
        }
        List<Match> matches = new ArrayList<>();
        for (Config c : matchesConfigs.get()) {
            FindType type = FindType.fromFindType(c.get("type"));
            matches.add(new Match(type, c.get("find")));
        }
        filters.add(new MatchFilter(matches, config.get("replace")));
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
