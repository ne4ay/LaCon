package ua.nechay.lacon.core.function;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 16.05.2023
 */
public enum LaconMethodName implements MethodName {
    SIZE("size"),
    SUB_STRING("sub_string"),
    SPLIT("split"),
    PUT("put"),
    REMOVE("remove"),
    START("start"),
    END("end"),
    NEXT_GROUP("next_group")
    ;

    private final String representation;

    LaconMethodName(@Nonnull String representation) {
        this.representation = representation;
    }

    @Nonnull
    @Override
    public String getRepresentation() {
        return representation;
    }
}
