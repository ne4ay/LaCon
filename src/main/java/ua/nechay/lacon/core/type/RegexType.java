package ua.nechay.lacon.core.type;

import ua.nechay.lacon.core.LaconType;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 19.05.2023
 */
public enum RegexType implements LaconType {
    MATCH("match");

    private final String representation;

    RegexType(String representation) {
        this.representation = representation;
    }

    @Nonnull
    @Override
    public String getRepresentation() {
        return null;
    }

    @Override
    public boolean accepts(@Nonnull LaconType type) {
        return equals(type);
    }
}
