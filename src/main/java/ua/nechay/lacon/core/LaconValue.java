package ua.nechay.lacon.core;

import ua.nechay.lacon.core.touch.SimpleTypeTouch;
import ua.nechay.lacon.core.touch.TypeTouch;
import ua.nechay.lacon.core.val.IntLaconValue;
import ua.nechay.lacon.core.val.RealLaconValue;
import ua.nechay.lacon.core.val.StringLaconValue;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * @author anechaev
 * @since 15.04.2023
 */
public abstract class LaconValue<T> {

    private final T value;
    private final LaconType type;

    public LaconValue(@Nonnull T value, @Nonnull LaconType type) {
        this.value = value;
        this.type = type;
    }

    public static LaconValue<?> create(Object value, @Nonnull LaconType type) {
        return TypeTouch.touch(type, SimpleTypeTouch.create(
            () -> new IntLaconValue((long) value),
            () -> new RealLaconValue((double) value),
            () -> new StringLaconValue((String) value)
        ));
    }

    @Nonnull
    public T getValue() {
        return value;
    }

    @Nonnull
    public LaconType getType() {
        return type;
    }

    @Nonnull public abstract LaconValue<?> plus(@Nonnull LaconValue<?> value);
    @Nonnull public abstract LaconValue<?> minus(@Nonnull LaconValue<?> value);
    @Nonnull public abstract LaconValue<?> mul(@Nonnull LaconValue<?> value);
    @Nonnull public abstract LaconValue<?> div(@Nonnull LaconValue<?> value);
    @Nonnull public abstract LaconValue<?> unaryPlus();
    @Nonnull public abstract LaconValue<?> unaryMinus();


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof LaconValue))
            return false;
        LaconValue<?> that = (LaconValue<?>) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
