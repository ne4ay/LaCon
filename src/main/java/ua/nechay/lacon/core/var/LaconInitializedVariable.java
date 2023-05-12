package ua.nechay.lacon.core.var;

import ua.nechay.lacon.core.LaconValue;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 11.04.2023
 */
public class LaconInitializedVariable extends LaconVariable {
    private LaconValue<?> value;

    public LaconInitializedVariable(@Nonnull LaconValue<?> value) {
        super(value.getType());
        this.value = value;
    }

    public boolean isInitialized() {
        return true;
    }

    @Nonnull
    @Override
    public LaconValue<?> getValueObject() {
        return value;
    }

    @Nonnull
    @Override
    public Object getValue() {
        return getValueObject().getValue();
    }

    @Nonnull
    @Override
    public LaconInitializedVariable setValue(LaconValue<?> value) {
        if (value.getType() != getType()) {
            throw new IllegalStateException("Type mismatch! Unable to put " + value.getType() + " into " + getType() + " var!");
        }
        this.value = value;
        return this;
    }
}
