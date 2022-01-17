package io.github.darkkronicle.advancedchatmacros.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.options.ConfigInteger;
import fi.dy.masa.malilib.config.options.ConfigString;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import io.github.darkkronicle.Konstruct.NodeException;
import io.github.darkkronicle.Konstruct.NodeProcessor;
import io.github.darkkronicle.Konstruct.builder.NodeBuilder;
import io.github.darkkronicle.Konstruct.nodes.Node;
import io.github.darkkronicle.advancedchatcore.config.SaveableConfig;
import io.github.darkkronicle.advancedchatcore.finder.PatternFinder;
import io.github.darkkronicle.advancedchatcore.finder.RegexFinder;
import io.github.darkkronicle.advancedchatcore.interfaces.IJsonApplier;
import io.github.darkkronicle.advancedchatcore.interfaces.IMatchProcessor;
import io.github.darkkronicle.advancedchatcore.interfaces.IScreenSupplier;
import io.github.darkkronicle.advancedchatcore.util.FluidText;
import io.github.darkkronicle.advancedchatcore.util.SearchResult;
import io.github.darkkronicle.advancedchatcore.util.SyncTaskQueue;
import io.github.darkkronicle.advancedchatmacros.AdvancedChatMacros;
import io.github.darkkronicle.advancedchatmacros.config.MacrosConfigStorage;
import io.github.darkkronicle.advancedchatmacros.filter.KonstructFilter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class MacroMatchProcessor implements IMatchProcessor, IScreenSupplier, IJsonApplier {

    private static String translate(String key) {
        return "advancedchatmacros.matchprocessor." + key;
    }

    private final SaveableConfig<ConfigString> command = SaveableConfig.fromConfig("command",
            new ConfigString(translate("command"), "", translate("info.command")));

    private final SaveableConfig<ConfigInteger> delay = SaveableConfig.fromConfig("delay",
            new ConfigInteger(translate("delay"), 0, 0, 6000, translate("info.delay")));

    private String old = null;

    private Node node;

    public MacroMatchProcessor() {
        reloadNode();
    }

    @Override
    public Result processMatches(FluidText text, @Nullable FluidText unfiltered, @Nullable SearchResult search) {
        reloadNode();
        NodeProcessor processor = KonstructFilter.getInstance().getProcessor().copy();
        processor.addVariable("input", text.getString());
        String message = processor.parse(node);
        if (MacrosConfigStorage.General.PREVENT_MACRO_RECURSION.config.getBooleanValue() && search.getFinder() != null && search.getFinder().isMatch(message, search.getSearch())) {
            AdvancedChatMacros.LOGGER.log(Level.WARN, "Auto message stopped to prevent recursion!");
        }
        if (delay.config.getIntegerValue() == 0) {
            MinecraftClient.getInstance().player.sendChatMessage(message);
            return Result.getFromBool(true);
        }
        SyncTaskQueue.getInstance().add(delay.config.getIntegerValue(), () -> {
            MinecraftClient.getInstance().player.sendChatMessage(message);
        });
        return Result.getFromBool(true);
    }

    private void reloadNode() {
        if (old != null && old.equals(command.config.getStringValue())) {
            return;
        }
        try {
            node = new NodeBuilder(command.config.getStringValue()).build();
        } catch (NodeException e) {
            AdvancedChatMacros.LOGGER.log(Level.WARN, "Error setting up macro processor!", e);
            node = new NodeBuilder("").build();
        }
        old = command.config.getStringValue();
    }

    @Override
    public JsonObject save() {
        JsonObject obj = new JsonObject();
        obj.add("command", command.config.getAsJsonElement());
        obj.add("delay", delay.config.getAsJsonElement());
        return obj;
    }

    @Override
    public void load(JsonElement element) {
        if (!element.isJsonObject()) {
            return;
        }
        JsonObject obj = element.getAsJsonObject();
        if (obj.has("command")) {
            command.config.setValueFromJsonElement(obj.get("command"));
        }
        if (obj.has("delay")) {
            command.config.setValueFromJsonElement(obj.get("delay"));
        }
        reloadNode();
    }

    @Override
    public Supplier<Screen> getScreen(@Nullable Screen parent) {
        return () -> new MacroScreen(this, parent);
    }

    public static class MacroScreen extends GuiConfigsBase {

        private final MacroMatchProcessor processor;

        public MacroScreen(MacroMatchProcessor processor, Screen parent) {
            super(10, 60, AdvancedChatMacros.MOD_ID, parent, "advancedchatmacros.screen.processor");
            this.processor = processor;
        }

        @Override
        public List<ConfigOptionWrapper> getConfigs() {
            return ConfigOptionWrapper.createFor(Arrays.asList(processor.command.config, processor.delay.config));
        }

    }
}
