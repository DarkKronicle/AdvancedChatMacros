package io.github.darkkronicle.advancedchatmacros.functions;

import com.electronwill.nightconfig.core.Config;
import io.github.darkkronicle.Konstruct.IntRange;
import io.github.darkkronicle.Konstruct.NodeProcessor;
import io.github.darkkronicle.Konstruct.ParseContext;
import io.github.darkkronicle.Konstruct.Result;
import io.github.darkkronicle.Konstruct.builder.NodeBuilder;
import io.github.darkkronicle.Konstruct.functions.Function;
import io.github.darkkronicle.Konstruct.functions.NamedFunction;
import io.github.darkkronicle.Konstruct.nodes.Node;
import io.github.darkkronicle.advancedchatmacros.config.KeybindManager;
import io.github.darkkronicle.advancedchatmacros.filter.KonstructFilter;
import net.minecraft.client.MinecraftClient;

import java.util.List;

public class CommandFunction implements NamedFunction {

    @Override
    public String getName() {
        return "executeCommand";
    }

    @Override
    public Result parse(ParseContext context, List<Node> input) {
        Result res = Function.parseArgument(context, input, 0);
        if (Function.shouldReturn(res)) return res;
        if (!KeybindManager.SETTING_UP && MinecraftClient.getInstance().player != null) {
            MinecraftClient.getInstance().player.sendChatMessage(res.getContent());
        }
        return Result.success("");
    }

    @Override
    public IntRange getArgumentCount() {
        return IntRange.of(1);
    }
}
