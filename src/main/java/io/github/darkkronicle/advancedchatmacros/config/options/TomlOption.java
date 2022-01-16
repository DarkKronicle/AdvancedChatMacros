package io.github.darkkronicle.advancedchatmacros.config.options;

import java.util.Optional;

public interface TomlOption<T> {

    void setValueFromToml(T value);

    default void setValueFromToml(Optional<T> value) {
        if (value.isPresent()) {
            setValueFromToml(value.get());
        }
    }

    Object getToml();

}
