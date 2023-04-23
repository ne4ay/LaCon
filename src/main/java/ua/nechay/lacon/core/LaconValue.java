package ua.nechay.lacon.core;

import ua.nechay.lacon.core.touch.SimpleTypeTouch;
import ua.nechay.lacon.core.touch.TypeTouch;
import ua.nechay.lacon.core.val.BooleanLaconValue;
import ua.nechay.lacon.core.val.IntLaconValue;
import ua.nechay.lacon.core.val.ListLaconValue;
import ua.nechay.lacon.core.val.RealLaconValue;
import ua.nechay.lacon.core.val.StringLaconValue;
import ua.nechay.lacon.exception.LaconUnsupportedOperationException;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static ua.nechay.lacon.exception.LaconUnsupportedOperationException.unsupportedOperation;

/**
 * @author anechaev
 * @since 15.04.2023
 */
public abstract class LaconValue<T> {

    private final T value;
    private final LaconBuiltInType type;

    public LaconValue(@Nonnull T value, @Nonnull LaconBuiltInType type) {
        this.value = value;
        this.type = type;
    }

    public static LaconValue<?> create(Object value, @Nonnull LaconBuiltInType type) {
        return TypeTouch.touch(type, SimpleTypeTouch.create(
            () -> new IntLaconValue((long) value),
            () -> new RealLaconValue((double) value),
            () -> new StringLaconValue((String) value),
            () -> new BooleanLaconValue((boolean) value),
            () -> new ListLaconValue((List<Object>) value)
        ));
    }

    @Nonnull
    public T getValue() {
        return value;
    }

    @Nonnull
    public LaconBuiltInType getType() {
        return type;
    }

    @Nonnull public abstract LaconValue<?> plus(@Nonnull LaconValue<?> value);
    @Nonnull public abstract LaconValue<?> minus(@Nonnull LaconValue<?> value);
    @Nonnull public abstract LaconValue<?> mul(@Nonnull LaconValue<?> value);
    @Nonnull public abstract LaconValue<?> div(@Nonnull LaconValue<?> value);
    @Nonnull public abstract LaconValue<?> unaryPlus();
    @Nonnull public abstract LaconValue<?> unaryMinus();
    @Nonnull public abstract LaconValue<?> unaryNot();

    @Nonnull
    public LaconValue<?> modulus(@Nonnull LaconValue<?> value) {
        return unsupported("%", value);
    }

    @Nonnull
    public LaconValue<?> or(@Nonnull LaconValue<?> value) {
        return unsupported("'or'", value);
    }

    @Nonnull
    public LaconValue<?> and(@Nonnull LaconValue<?> value) {
        return unsupported("'and'", value);
    }

    protected LaconValue<?> unsupported(@Nonnull String operation, @Nonnull LaconValue<?> value) {
        return LaconUnsupportedOperationException.unsupportedOperation(operation, getType().getRepresentation(), value.getType().getRepresentation());
    }

    @Nonnull
    public LaconValue<?> equal(@Nonnull LaconValue<?> value) {
        return new BooleanLaconValue(equals(value));
    }

    @Nonnull
    public LaconValue<?> notEqual(@Nonnull LaconValue<?> value) {
        return new BooleanLaconValue(!equals(value));
    }
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
