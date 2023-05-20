package ua.nechay.lacon.core.var;

import ua.nechay.lacon.core.LaconType;
import ua.nechay.lacon.core.LaconValue;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 19.04.2023
 */
public class LaconVariable {
    private final LaconType type;

    public LaconVariable(@Nonnull LaconType type) {
        this.type = type;
    }

    public static LaconVariable initialized(@Nonnull LaconValue<?> value) {
        return new LaconInitializedVariable(value);
    }

    public boolean isInitialized() {
        return false;
    }

    @Nonnull
    public Object getValue() {
        throw new IllegalStateException("The variable is not initialized!!!");
    }

    @Nonnull
    public LaconValue<?> getValueObject() {
        throw new IllegalStateException("The variable is not initialized!!!");
    }

    @Nonnull
    public LaconType getType() {
        return type;
    }

    @Nonnull
    public LaconInitializedVariable setValue(LaconValue<?> value) {
        if (!getType().accepts(value.getType())) {
            throw new IllegalStateException("Type mismatch! Unable to put " + value.getType() + " into " + getType() + " var!");
        }
        return new LaconInitializedVariable(value);
    }
}
