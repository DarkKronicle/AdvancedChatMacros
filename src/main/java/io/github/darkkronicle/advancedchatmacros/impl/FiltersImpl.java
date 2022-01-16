package io.github.darkkronicle.advancedchatmacros.impl;

import io.github.darkkronicle.advancedchatfilters.registry.MatchProcessorRegistry;

public class FiltersImpl {

    private final static FiltersImpl INSTANCE = new FiltersImpl();

    public static FiltersImpl getInstance() {
        return INSTANCE;
    }

    private FiltersImpl() {}

    public void init() {
        MatchProcessorRegistry processors = MatchProcessorRegistry.getInstance();
        processors.register(MacroMatchProcessor::new, "macro", "advancedchatmacros.processor", "advancedchatmacros.info.processor");
    }

}
