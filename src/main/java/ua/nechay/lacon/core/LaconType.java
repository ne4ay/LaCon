package ua.nechay.lacon.core;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author anechaev
 * @since 15.04.2023
 */
public enum LaconType {
    INT("int"),
    REAL("real"),
    STRING("str")
    ;
    private static final Map<String, LaconType> ACCESS_MAP = Arrays.stream(values())
        .collect(Collectors.toMap(
            LaconType::getRepresentation,
            type -> type
        ));

    private final String representation;

    LaconType(@Nonnull String representation) {
        this.representation = representation;
    }

    @Nullable
    public static LaconType getForRepresentation(@Nonnull String representation) {
        return ACCESS_MAP.get(representation);
    }

    @Nonnull
    public String getRepresentation() {
        return representation;
    }
}
