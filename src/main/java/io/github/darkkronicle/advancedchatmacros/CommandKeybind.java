package io.github.darkkronicle.advancedchatmacros;

import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import fi.dy.masa.malilib.hotkeys.KeybindMulti;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import io.github.darkkronicle.Konstruct.NodeProcessor;
import io.github.darkkronicle.Konstruct.ParseResult;
import io.github.darkkronicle.Konstruct.functions.Variable;
import io.github.darkkronicle.Konstruct.nodes.Node;
import io.github.darkkronicle.addons.conditions.BooleanFunction;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

public class CommandKeybind implements IHotkeyCallback {

    @Getter
    private final IKeybind keybind;

    @Getter
    @Setter
    private NodeProcessor processor;

    @Getter
    private final Node node;

    public CommandKeybind(IKeybind keybind, Node node, NodeProcessor processor) {
        this.keybind = keybind;
        this.node = node;
        this.processor = processor;
    }

    @Override
    public boolean onKeyAction(KeyAction action, IKeybind key) {
        processor.parse(node);
        return true;
    }

    public static CommandKeybind fromNode(NodeProcessor processor, Node node) {
        ParseResult result = processor.parse(node);
        Optional<Variable> keys = result.getContext().getLocalVariable("keys");
        if (keys.isEmpty()) {
            // No key so no bind
            return null;
        }
        boolean cancel = BooleanFunction.stringToBool(result.getContext().getLocalVariable("cancel").map(Variable::getValue).orElse("1"));
        boolean exclusive = BooleanFunction.stringToBool(result.getContext().getLocalVariable("exclusive").map(Variable::getValue).orElse("0"));
        boolean allowExtra = BooleanFunction.stringToBool(result.getContext().getLocalVariable("allowExtra").map(Variable::getValue).orElse("0"));
        boolean orderSensitive = BooleanFunction.stringToBool(result.getContext().getLocalVariable("orderSensitive").map(Variable::getValue).orElse("1"));
        KeybindSettings settings = KeybindSettings.create(KeybindSettings.Context.INGAME, KeyAction.PRESS, allowExtra, orderSensitive, exclusive, cancel);
        IKeybind keybind = KeybindMulti.fromStorageString(keys.get().getValue().toUpperCase(), settings);
        CommandKeybind command = new CommandKeybind(keybind, node, processor);
        command.getKeybind().setCallback(command);
        return command;
    }
}
