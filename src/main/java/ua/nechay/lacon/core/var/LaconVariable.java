package ua.nechay.lacon.core.var;

import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconValue;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 19.04.2023
 */
public class LaconVariable {
    private final LaconBuiltInType type;

    public LaconVariable(@Nonnull LaconBuiltInType type) {
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
    public LaconBuiltInType getType() {
        return type;
    }

    @Nonnull
    public LaconInitializedVariable setValue(LaconValue<?> value) {
        if (value.getType() != getType()) {
            throw new IllegalStateException("Type mismatch! Unable to put " + value.getType() + " into " + getType() + " var!");
        }
        return new LaconInitializedVariable(value);
    }
}
