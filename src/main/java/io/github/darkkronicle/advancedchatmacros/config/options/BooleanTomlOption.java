package io.github.darkkronicle.advancedchatmacros.config.options;

import fi.dy.masa.malilib.config.options.ConfigBoolean;

public class BooleanTomlOption extends ConfigBoolean implements TomlOption<Boolean> {

    public BooleanTomlOption(String name, boolean defaultValue, String comment) {
        super(name, defaultValue, comment);
    }

    public BooleanTomlOption(String name, boolean defaultValue, String comment, String prettyName) {
        super(name, defaultValue, comment, prettyName);
    }

    @Override
    public void setValueFromToml(Boolean value) {
        setBooleanValue(value);
    }

    @Override
    public Object getToml() {
        return getBooleanValue();
    }

}
