package io.github.darkkronicle.advancedchatmacros.filter;

import io.github.darkkronicle.Konstruct.NodeException;
import io.github.darkkronicle.Konstruct.NodeProcessor;
import io.github.darkkronicle.Konstruct.builder.NodeBuilder;
import io.github.darkkronicle.Konstruct.nodes.Node;
import io.github.darkkronicle.Konstruct.reader.Token;
import io.github.darkkronicle.addons.*;
import io.github.darkkronicle.advancedchatcore.interfaces.IStringFilter;
import io.github.darkkronicle.advancedchatmacros.config.MacrosConfigStorage;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;

import java.util.Optional;

public class KonstructFilter implements IStringFilter {

    @Getter
    private NodeProcessor processor;

    private Token.TokenSettings settings = new Token.TokenSettings("[[", "]]", "(", ")", ",", "{{", "}}", "\\", "''", "'''");

    private final static KonstructFilter INSTANCE = new KonstructFilter();

    public static KonstructFilter getInstance() {
        return INSTANCE;
    }

    private KonstructFilter() {
        MinecraftClient client = MinecraftClient.getInstance();
        processor = new NodeProcessor();

        processor.addFunction(new CalculatorFunction());
        processor.addFunction(new ReplaceFunction());
        processor.addFunction(new RandomFunction());
        processor.addFunction(new OwOFunction());
        processor.addFunction(new GetFunction());
        processor.addFunction(new RoundFunction());
        processor.addFunction(new RomanNumeralFunction());

        processor.addVariable("x", () -> String.valueOf(client.player.getX()));
        processor.addVariable("y", () -> String.valueOf(client.player.getY()));
        processor.addVariable("z", () -> String.valueOf(client.player.getZ()));
        processor.addVariable("blockX", () -> String.valueOf(client.player.getBlockX()));
        processor.addVariable("blockY", () -> String.valueOf(client.player.getBlockY()));
        processor.addVariable("blockZ", () -> String.valueOf(client.player.getBlockZ()));
    }

    @Override
    public Optional<String> filter(String input) {
        if (!MacrosConfigStorage.General.KONSTRUCT_ENABLED.config.getBooleanValue()) {
            return Optional.empty();
        }
        try {
            Node node = new NodeBuilder(input, settings).build();
            return Optional.of(node.parse(processor.createContext()));
        } catch (NodeException e) {
            return Optional.empty();
        }
    }
}
