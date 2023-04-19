package ua.nechay.lacon.core.var;

import ua.nechay.lacon.core.LaconType;
import ua.nechay.lacon.core.LaconValue;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 11.04.2023
 */
public class LaconInitializedVariable extends LaconVariable {
    private LaconValue<?> value;

    public LaconInitializedVariable(@Nonnull LaconType type, @Nonnull Object value) {
        super(type);
        this.value = LaconValue.create(value, type);
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

    @Nonnull
    @Override
    public LaconInitializedVariable setValue(Object value, LaconType type) {
        if (type != getType()) {
            throw new IllegalStateException("Type mismatch! Unable to put " + type + " into " + getType() + " var!");
        }
        this.value = LaconValue.create(value, type);
        return this;
    }
}
