package io.github.darkkronicle.advancedchatmacros.executes;

import com.electronwill.nightconfig.core.Config;

public interface IExecute {

    void execute();

    static IExecute fromConfig(Config config) {
        String type = (String) config.getOptional("type").orElse("command");
        if (type.equals("command")) {
            return new CommandExecute(config);
        }
        return null;
    }

}
