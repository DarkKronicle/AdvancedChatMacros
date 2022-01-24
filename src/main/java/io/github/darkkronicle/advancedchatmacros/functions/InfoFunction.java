package io.github.darkkronicle.advancedchatmacros.functions;

import io.github.darkkronicle.Konstruct.functions.Function;
import io.github.darkkronicle.Konstruct.functions.NamedFunction;
import io.github.darkkronicle.Konstruct.nodes.Node;
import io.github.darkkronicle.Konstruct.parser.IntRange;
import io.github.darkkronicle.Konstruct.parser.ParseContext;
import io.github.darkkronicle.Konstruct.parser.Result;
import io.github.darkkronicle.advancedchatmacros.config.KeybindManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;

import java.util.List;

public class InfoFunction implements NamedFunction {

    @Override
    public String getName() {
        return "sendInfo";
    }

    @Override
    public Result parse(ParseContext context, List<Node> input) {
        Result res = Function.parseArgument(context, input, 0);
        if (Function.shouldReturn(res)) return res;
        if (!KeybindManager.SETTING_UP && MinecraftClient.getInstance().player != null) {
            MinecraftClient.getInstance().player.sendMessage(new LiteralText(res.getContent().getString()), false);
        }
        return Result.success("");
    }

    @Override
    public IntRange getArgumentCount() {
        return IntRange.of(1);
    }
}
