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
    INT("int", 0L),
    REAL("real", 0.0)
    ;
    private static final Map<String, LaconType> ACCESS_MAP = Arrays.stream(values())
        .collect(Collectors.toMap(
            LaconType::getRepresentation,
            type -> type
        ));

    private final String representation;
    private final Object noneObject;

    LaconType(@Nonnull String representation, @Nonnull Object noneObject) {
        this.representation = representation;
        this.noneObject = noneObject;
    }

    @Nullable
    public static LaconType getForRepresentation(@Nonnull String representation) {
        return ACCESS_MAP.get(representation);
    }

    @Nonnull
    public String getRepresentation() {
        return representation;
    }

    @Nonnull
    public Object getNoneObject() {
        return noneObject;
    }
}
