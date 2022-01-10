package io.github.darkkronicle.advancedchatmacros;

import io.github.darkkronicle.advancedchatcore.util.FindType;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Match {

    FindType findType;
    String findString;

}
