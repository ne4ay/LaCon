package ua.nechay.lacon.core;

import ua.nechay.lacon.core.function.FunctionLaconValue;
import ua.nechay.lacon.core.touch.SimpleTypeTouch;
import ua.nechay.lacon.core.touch.TypeTouch;
import ua.nechay.lacon.core.touch.TypeTouchBuilder;
import ua.nechay.lacon.core.val.BooleanLaconValue;
import ua.nechay.lacon.core.val.IntLaconValue;
import ua.nechay.lacon.core.val.RealLaconValue;
import ua.nechay.lacon.core.val.StringLaconValue;
import ua.nechay.lacon.exception.LaconUnsupportedOperationException;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import static ua.nechay.lacon.core.LaconOperation.PLUS;
import static ua.nechay.lacon.exception.LaconUnsupportedOperationException.unsupportedOperation;

/**
 * @author anechaev
 * @since 15.04.2023
 */
public abstract class LaconValue<T> implements Comparable<LaconValue<?>>, Iterable<LaconValue<?>> {

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
            () -> new StringLaconValue((String) value),
            () -> new BooleanLaconValue((boolean) value),
            () -> { throw new IllegalStateException("Unable to create list from plain value: " + value); },
            () -> { throw new IllegalStateException("Unable to create function from plain value: " + value); },
            () -> { throw new IllegalStateException("Unable to create dict from plain value: " + value); },
            () -> { throw new IllegalStateException("Unable to create " + type.getRepresentation() + " from plain value: " + value); }
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

    @Nonnull
    public LaconValue<?> plus(@Nonnull LaconValue<?> value) {
        return unsupported(LaconOperation.PLUS, value);
    }

    @Nonnull
    public LaconValue<?> minus(@Nonnull LaconValue<?> value) {
        return unsupported(LaconOperation.MINUS, value);
    }

    @Nonnull
    public LaconValue<?> mul(@Nonnull LaconValue<?> value) {
        return unsupported(LaconOperation.MUL, value);
    }

    @Nonnull
    public LaconValue<?> div(@Nonnull LaconValue<?> value) {
        return unsupported(LaconOperation.DIV, value);
    }

    @Nonnull
    public LaconValue<?> unaryPlus() {
        return unsupportedOperation(LaconOperation.PLUS, getType());
    }

    @Nonnull
    public LaconValue<?> unaryMinus() {
        return unsupportedOperation(LaconOperation.MINUS, getType());
    }

    @Nonnull
    public LaconValue<?> unaryNot() {
        return unsupportedOperation(LaconOperation.NOT, getType());
    }

    @Override
    public int compareTo(@Nonnull LaconValue<?> o) {
        var type = o.getType();
        var uncastedValue = o.getValue();
        if (!type.equals(getType())) {
            throw new IllegalStateException("Unable to compare objects of different types: '" + type + "' and '" + getType() + "'!");
        }
        return Objects.compare(getValue(), (T) uncastedValue, this::compare);
    }

    protected int compare(T obj1, T obj2) {
        throw new IllegalStateException("Type " + getType() + " is incomparable!");
    }

    @Nonnull
    public LaconValue<?> modulus(@Nonnull LaconValue<?> value) {
        return unsupported(LaconOperation.MODULUS, value);
    }

    @Nonnull
    public LaconValue<?> or(@Nonnull LaconValue<?> value) {
        return unsupported(LaconOperation.OR, value);
    }

    @Nonnull
    public LaconValue<?> and(@Nonnull LaconValue<?> value) {
        return unsupported(LaconOperation.AND, value);
    }

    @Nonnull
    public LaconValue<?> getByIndex(@Nonnull LaconValue<?> value) {
        return unsupported(LaconOperation.GET_BY_INDEX, value);
    }

    @Override
    public Iterator<LaconValue<?>> iterator() {
        throw new IllegalStateException("Iterator is not supported by type: " + getType());
    }

    @Nonnull
    public LaconValue<?> call(@Nonnull LaconProgramState currentState, @Nonnull LaconValue<?> args) {
        return unsupported(LaconOperation.CALL, args);
    }

    @Nonnull
    protected LaconValue<?> methodCall(@Nonnull LaconProgramState currentState, @Nonnull LaconValue<?> args, @Nonnull LaconValue<?> ref) {
        return unsupported(LaconOperation.CALL, args);
    }

    @Nonnull
    public LaconValue<?> contains(@Nonnull LaconValue<?> value) {
        return unsupported(LaconOperation.IN, value);
    }

    @Nonnull
    public LaconValue<?> castTo(@Nonnull LaconType type) {
        return unsupported(LaconOperation.CAST, type);
    }

    public Map<String, FunctionLaconValue> getMethods() {
        return Collections.emptyMap();
    }

    public LaconValue<?> callMethod(@Nonnull String methodName, @Nonnull LaconProgramState currentState, @Nonnull LaconValue<?> args) {
        LaconValue<?> method = getMethods().get(methodName);
        if (method == null) {
            throw new IllegalStateException("There is no method " + methodName + " inside type " + getType());
        }
        return method.methodCall(currentState, args, this);
    }


    protected <T> LaconValue<T> unsupported(@Nonnull LaconOperation operation, @Nonnull LaconType type) {
        return LaconUnsupportedOperationException.unsupportedOperation(operation, getType(), type);
    }

    protected LaconValue<?> unsupported(@Nonnull LaconOperation operation, @Nonnull LaconValue<?> value) {
        return unsupported(operation.getRepresentation(), value);
    }

    protected LaconValue<?> unsupported(@Nonnull String operation, @Nonnull LaconValue<?> value) {
        return LaconUnsupportedOperationException.unsupportedOperation(operation, getType(), value.getType());
    }

    protected TypeTouchBuilder<LaconValue<?>> getDefaultTypeTouchBuilder(@Nonnull LaconOperation operation, @Nonnull LaconValue<?> value) {
        return TypeTouchBuilder.<LaconValue<?>>create(() -> unsupported(operation, value));
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

    @Override
    public String toString() {
        return getValue().toString();
    }
}
