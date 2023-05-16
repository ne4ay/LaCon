package ua.nechay.lacon.core.function;

import ua.nechay.lacon.utils.Pair;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author anechaev
 * @since 16.05.2023
 */
public enum LaconMethodName {
    SIZE("size"),
    SUB_STRING("sub_string"),
    SPLIT("split")
    ;

    private final String representation;

    LaconMethodName(@Nonnull String representation) {
        this.representation = representation;
    }

    @SafeVarargs
    public static Map<String, FunctionLaconValue> toMap(@Nonnull Pair<LaconMethodName, FunctionLaconValue> ... pairs) {
        return Arrays.stream(pairs)
            .map(pair -> pair.mapFirst(LaconMethodName::getRepresentation))
            .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
    }

    public String getRepresentation() {
        return representation;
    }
}
