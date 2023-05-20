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
public enum LaconBuiltInType implements LaconType {
    INT("int"),
    REAL("real"),
    STRING("str"),
    BOOLEAN("bool"),
    LIST("list"),
    FUNCTION("function"),
    DICT("dict"),
    VOID("void"),
    RANGE("range"),
    ANY("any") {
        @Override
        public boolean accepts(@Nonnull LaconType type) {
            return true;
        }
    }
    ;
    private static final Map<String, LaconBuiltInType> ACCESS_MAP = Arrays.stream(values())
        .collect(Collectors.toMap(
            LaconBuiltInType::getRepresentation,
            type -> type
        ));

    private final String representation;

    LaconBuiltInType(@Nonnull String representation) {
        this.representation = representation;
    }

    @Override
    public boolean isBuiltIn() {
        return true;
    }

    @Nullable
    public static LaconBuiltInType getForRepresentation(@Nonnull String representation) {
        return ACCESS_MAP.get(representation);
    }

    @Nonnull
    public String getRepresentation() {
        return representation;
    }

    @Override
    public boolean accepts(@Nonnull LaconType type) {
        return equals(type);
    }

    @Override
    public String toString() {
        return getRepresentation();
    }
}
