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

    private final List<FindPair> matches;
    private final Node replaceTo;

    public MatchFilter(List<FindPair> matches, String replaceTo) {
        this.matches = matches;
        this.replaceTo = new NodeBuilder(replaceTo).build();
    }

    @Override
    public Optional<String> filter(String input) {
        List<StringMatch> stringMatches = new ArrayList<>();
        for (FindPair match : matches) {
            stringMatches.addAll(SearchResult.searchOf(input, match.getString(), match.getType()).getMatches());
        }
        if (stringMatches.size() == 0) {
            return Optional.empty();
        }
        int lastIndex = 0;
        StringBuilder builder = new StringBuilder();
        for (StringMatch match : stringMatches) {
            NodeProcessor processor = new NodeProcessor();
            processor.addAll(KonstructFilter.getInstance().getProcessor());
            processor.addVariable("0", match.match);
            if (match.start - lastIndex > 0) {
                builder.append(input, lastIndex, match.start);
            }
            builder.append(replaceTo.parse(processor.createContext()));
            lastIndex = match.end;
        }
        return Optional.of(builder.append(input, lastIndex, input.length()).toString());
    }

}
