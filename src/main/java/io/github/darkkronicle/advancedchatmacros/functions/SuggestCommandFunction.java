package io.github.darkkronicle.advancedchatmacros.functions;

import fi.dy.masa.malilib.gui.GuiBase;
import io.github.darkkronicle.Konstruct.IntRange;
import io.github.darkkronicle.Konstruct.ParseContext;
import io.github.darkkronicle.Konstruct.Result;
import io.github.darkkronicle.Konstruct.functions.Function;
import io.github.darkkronicle.Konstruct.functions.NamedFunction;
import io.github.darkkronicle.Konstruct.nodes.Node;
import io.github.darkkronicle.advancedchatmacros.config.KeybindManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;

import java.util.List;

public class SuggestCommandFunction implements NamedFunction {

    @Override
    public String getName() {
        return "suggestCommand";
    }

    @Override
    public Result parse(ParseContext context, List<Node> input) {
        Result res = Function.parseArgument(context, input, 0);
        if (Function.shouldReturn(res)) return res;
        if (!KeybindManager.SETTING_UP && MinecraftClient.getInstance().player != null) {
            GuiBase.openGui(new ChatScreen(res.getContent()));
        }
        return Result.success("");
    }

    @Override
    public IntRange getArgumentCount() {
        return IntRange.of(1);
    }
}
