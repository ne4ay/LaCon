package ua.nechay.lacon.core;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 23.04.2023
 */
public interface LaconType {
    @Nonnull
    String getRepresentation();

    boolean accepts(@Nonnull LaconType type);
}
