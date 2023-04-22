package ua.nechay.lacon.core.val;

import ua.nechay.lacon.core.LaconType;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.touch.SimpleTypeTouch;
import ua.nechay.lacon.core.touch.TypeTouch;

import javax.annotation.Nonnull;

import static ua.nechay.lacon.core.val.LaconValueUtils.multipleStrings;
import static ua.nechay.lacon.exception.LaconUnsupportedOperationException.unsupportedOperation;

/**
 * @author anechaev
 * @since 15.04.2023
 */
public class IntLaconValue extends LaconValue<Long> {
    public IntLaconValue(long value) {
        super(value, LaconType.INT);
    }

    @Nonnull @Override
    public LaconValue<?> plus(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> new IntLaconValue(getValue() + (long) value.getValue()),
            () -> new RealLaconValue((double) getValue() + (double) value.getValue()),
            () -> new StringLaconValue(getValue() + (String) value.getValue()),
            () -> new IntLaconValue(getValue() + BooleanLaconValue.castToInt((boolean) value.getValue()))
        ));
    }

    @Nonnull @Override
    public LaconValue<?> minus(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> new IntLaconValue(getValue() - (long) value.getValue()),
            () -> new RealLaconValue((double) getValue() - (double) value.getValue()),
            () -> unsupportedOperation("-", LaconType.INT.getRepresentation(), LaconType.STRING.getRepresentation()),
            () -> new IntLaconValue(getValue() - BooleanLaconValue.castToInt((boolean) value.getValue()))
        ));
    }

    @Nonnull @Override
    public LaconValue<?> mul(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> new IntLaconValue(getValue() * (long) value.getValue()),
            () -> new RealLaconValue((double) getValue() * (double) value.getValue()),
            () -> new StringLaconValue(multipleStrings((String)value.getValue(), getValue())),
            () -> new IntLaconValue(getValue() * BooleanLaconValue.castToInt((boolean) value.getValue()))
        ));
    }

    @Nonnull @Override
    public LaconValue<?> div(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> new IntLaconValue(getValue() / (long) value.getValue()),
            () -> new RealLaconValue((double) getValue() / (double) value.getValue()),
            () -> unsupportedOperation("/", LaconType.INT.getRepresentation(), LaconType.STRING.getRepresentation()),
            () -> unsupportedOperation("/", LaconType.INT.getRepresentation(), LaconType.BOOLEAN.getRepresentation())
        ));
    }

    @Nonnull
    @Override
    public LaconValue<?> unaryPlus() {
        return new IntLaconValue(+getValue());
    }

    @Nonnull
    @Override
    public LaconValue<?> unaryMinus() {
        return new IntLaconValue(-getValue());
    }

    @Nonnull
    @Override
    public LaconValue<?> unaryNot() {
        return new BooleanLaconValue(!castToBool(getValue()));
    }

    public static double castToReal(long value) {
        return (double) value;
    }

    public static boolean castToBool(long value) {
        return value != 0;
    }
}
