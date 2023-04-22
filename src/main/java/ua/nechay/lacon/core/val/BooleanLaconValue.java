package ua.nechay.lacon.core.val;

import ua.nechay.lacon.core.LaconType;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.touch.SimpleTypeTouch;
import ua.nechay.lacon.core.touch.TypeTouch;

import javax.annotation.Nonnull;

import static ua.nechay.lacon.exception.LaconUnsupportedOperationException.unsupportedOperation;

/**
 * @author anechaev
 * @since 21.04.2023
 */
public class BooleanLaconValue extends LaconValue<Boolean> {
    public BooleanLaconValue(@Nonnull Boolean value) {
        super(value, LaconType.BOOLEAN);
    }

    @Nonnull
    @Override
    public LaconValue<?> plus(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> new IntLaconValue(toInt() + (long) value.getValue()),
            () -> unsupportedOperation("+", LaconType.BOOLEAN.getRepresentation(), LaconType.REAL.getRepresentation()),
            () -> new StringLaconValue(getValue() + (String) value.getValue()),
            () -> new BooleanLaconValue(plus(getValue(), (boolean) value.getValue()))
        ));
    }

    public static boolean plus(boolean value1, boolean value2) {
        return !value1 || !value2;
    }

    @Nonnull
    @Override
    public LaconValue<?> minus(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> new IntLaconValue(toInt() - (long) value.getValue()),
            () -> unsupportedOperation("-", LaconType.BOOLEAN.getRepresentation(), LaconType.REAL.getRepresentation()),
            () -> unsupportedOperation("-", LaconType.BOOLEAN.getRepresentation(), LaconType.STRING.getRepresentation()),
            () -> new BooleanLaconValue(minus(getValue(), (boolean) value.getValue()))
        ));
    }

    public static boolean minus(boolean value1, boolean value2) {
        if (value1 && value2) {
            return false;
        } else
            return value1;
    }

    @Nonnull
    @Override
    public LaconValue<?> mul(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> new IntLaconValue(toInt() * (long) value.getValue()),
            () -> unsupportedOperation("*", LaconType.BOOLEAN.getRepresentation(), LaconType.REAL.getRepresentation()),
            () -> unsupportedOperation("*", LaconType.BOOLEAN.getRepresentation(), LaconType.STRING.getRepresentation()),
            () -> new BooleanLaconValue(getValue() && (boolean) value.getValue())
        ));
    }

    @Nonnull
    @Override
    public LaconValue<?> div(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> unsupportedOperation("/", LaconType.BOOLEAN.getRepresentation(), LaconType.INT.getRepresentation()),
            () -> unsupportedOperation("/", LaconType.BOOLEAN.getRepresentation(), LaconType.REAL.getRepresentation()),
            () -> unsupportedOperation("/", LaconType.BOOLEAN.getRepresentation(), LaconType.STRING.getRepresentation()),
            () -> unsupportedOperation("/", LaconType.BOOLEAN.getRepresentation(), LaconType.BOOLEAN.getRepresentation())
        ));
    }

    @Nonnull
    @Override
    public LaconValue<?> or(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> unsupportedOperation("'or'", LaconType.BOOLEAN.getRepresentation(), LaconType.INT.getRepresentation()),
            () -> unsupportedOperation("'or'", LaconType.BOOLEAN.getRepresentation(), LaconType.REAL.getRepresentation()),
            () -> unsupportedOperation("'or'", LaconType.BOOLEAN.getRepresentation(), LaconType.STRING.getRepresentation()),
            () -> new BooleanLaconValue(getValue() || (boolean) value.getValue())
        ));
    }

    @Nonnull
    @Override
    public LaconValue<?> and(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> unsupportedOperation("'and'", LaconType.BOOLEAN.getRepresentation(), LaconType.INT.getRepresentation()),
            () -> unsupportedOperation("'and'", LaconType.BOOLEAN.getRepresentation(), LaconType.REAL.getRepresentation()),
            () -> unsupportedOperation("'and'", LaconType.BOOLEAN.getRepresentation(), LaconType.STRING.getRepresentation()),
            () -> new BooleanLaconValue(getValue() && (boolean) value.getValue())
        ));
    }

    @Nonnull
    @Override
    public LaconValue<?> unaryPlus() {
        return this;
    }

    @Nonnull
    @Override
    public LaconValue<?> unaryMinus() {
        return unaryNot();
    }

    @Nonnull
    @Override
    public LaconValue<?> unaryNot() {
        return new BooleanLaconValue(!getValue());
    }

    public static Long castToInt(boolean logicValue) {
        if (logicValue) {
            return 1L;
        } else {
            return 0L;
        }
    }


    private Long toInt() {
        return castToInt(getValue());
    }

}
