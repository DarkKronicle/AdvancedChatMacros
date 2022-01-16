package io.github.darkkronicle.advancedchatmacros.filter;

import io.github.darkkronicle.Konstruct.NodeProcessor;
import io.github.darkkronicle.Konstruct.builder.NodeBuilder;
import io.github.darkkronicle.Konstruct.nodes.Node;
import io.github.darkkronicle.advancedchatcore.interfaces.IStringFilter;
import io.github.darkkronicle.advancedchatcore.util.FindPair;
import io.github.darkkronicle.advancedchatcore.util.SearchResult;
import io.github.darkkronicle.advancedchatcore.util.StringMatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MatchFilter implements IStringFilter {

    private final Node replaceTo;

    public MatchFilter(String replaceTo) {
        this.replaceTo = new NodeBuilder(replaceTo).build();
    }

    @Override
    public Optional<String> filter(String input) {
        NodeProcessor processor = KonstructFilter.getInstance().getProcessor().copy();
        processor.addVariable("input", input);
        return Optional.of(replaceTo.parse(processor.createContext()));
    }

}
