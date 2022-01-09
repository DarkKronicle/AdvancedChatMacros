package io.github.darkkronicle.advancedchatmacros.executes;

import com.electronwill.nightconfig.core.Config;
import io.github.darkkronicle.Konstruct.NodeProcessor;
import io.github.darkkronicle.Konstruct.builder.NodeBuilder;
import io.github.darkkronicle.Konstruct.nodes.Node;
import io.github.darkkronicle.advancedchatmacros.KonstructFilter;
import net.minecraft.client.MinecraftClient;

public class CommandExecute implements IExecute {

    private final Node node;

    public CommandExecute(Config config) {
        this((String) config.get("value"));
    }

    public CommandExecute(String command) {
        node = new NodeBuilder(command).build();
    }

    @Override
    public void execute() {
        NodeProcessor processor = new NodeProcessor();
        processor.addAll(KonstructFilter.getInstance().getProcessor());
        MinecraftClient.getInstance().player.sendChatMessage(processor.parse(node));
    }
}
