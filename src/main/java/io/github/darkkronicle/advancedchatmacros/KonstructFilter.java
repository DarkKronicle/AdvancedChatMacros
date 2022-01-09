package io.github.darkkronicle.advancedchatmacros;

import io.github.darkkronicle.Konstruct.NodeException;
import io.github.darkkronicle.Konstruct.NodeProcessor;
import io.github.darkkronicle.Konstruct.builder.NodeBuilder;
import io.github.darkkronicle.Konstruct.nodes.Node;
import io.github.darkkronicle.addons.CalculatorFunction;
import io.github.darkkronicle.addons.GetFunction;
import io.github.darkkronicle.addons.RoundFunction;
import io.github.darkkronicle.advancedchatcore.interfaces.IStringFilter;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;

import java.util.Optional;

public class KonstructFilter implements IStringFilter {

    @Getter
    private NodeProcessor processor;

    public KonstructFilter() {
        MinecraftClient client = MinecraftClient.getInstance();
        processor = new NodeProcessor();

        GetFunction get = new GetFunction();
        CalculatorFunction calc = new CalculatorFunction();
        RoundFunction round = new RoundFunction();

        processor.addFunction(get.getName(), get);
        processor.addFunction(calc.getName(), calc);
        processor.addFunction(round.getName(), round);

        processor.addVariable("x", () -> String.valueOf(client.player.getX()));
        processor.addVariable("y", () -> String.valueOf(client.player.getY()));
        processor.addVariable("z", () -> String.valueOf(client.player.getZ()));
        processor.addVariable("blockX", () -> String.valueOf(client.player.getBlockX()));
        processor.addVariable("blockY", () -> String.valueOf(client.player.getBlockY()));
        processor.addVariable("blockZ", () -> String.valueOf(client.player.getBlockZ()));
    }

    @Override
    public Optional<String> filter(String input) {
        try {
            Node node = new NodeBuilder(input).build();
            return Optional.of(node.parse(processor.createContext()));
        } catch (NodeException e) {
            return Optional.empty();
        }
    }
}
