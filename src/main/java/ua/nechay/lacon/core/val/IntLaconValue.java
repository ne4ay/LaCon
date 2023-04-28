package ua.nechay.lacon.core.val;

import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.touch.SimpleTypeTouch;
import ua.nechay.lacon.core.touch.TypeTouch;

import javax.annotation.Nonnull;

import static ua.nechay.lacon.core.LaconValueUtils.multipleStrings;
import static ua.nechay.lacon.exception.LaconUnsupportedOperationException.unsupportedOperation;

/**
 * @author anechaev
 * @since 15.04.2023
 */
public class IntLaconValue extends LaconValue<Long> {
    public IntLaconValue(long value) {
        super(value, LaconBuiltInType.INT);
    }

    @Nonnull
    @Override
    public LaconValue<?> plus(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> new IntLaconValue(getValue() + (long) value.getValue()),
            () -> new RealLaconValue((double) getValue() + (double) value.getValue()),
            () -> new StringLaconValue(getValue() + (String) value.getValue()),
            () -> new IntLaconValue(getValue() + BooleanLaconValue.castToIntValue(value)),
            () -> ListLaconValue.addElementAtTheStart((ListLaconValue) value, this)
        ));
    }

    @Nonnull
    @Override
    public LaconValue<?> minus(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> new IntLaconValue(getValue() - (long) value.getValue()),
            () -> new RealLaconValue((double) getValue() - (double) value.getValue()),
            () -> unsupportedOperation("-", LaconBuiltInType.INT.getRepresentation(), LaconBuiltInType.STRING.getRepresentation()),
            () -> new IntLaconValue(getValue() - BooleanLaconValue.castToIntValue((boolean) value.getValue())),
            () -> ListLaconValue.removeElement((ListLaconValue) value, this)
        ));
    }

    @Nonnull
    @Override
    public LaconValue<?> mul(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> new IntLaconValue(getValue() * (long) value.getValue()),
            () -> new RealLaconValue((double) getValue() * (double) value.getValue()),
            () -> new StringLaconValue(multipleStrings((String) value.getValue(), getValue())),
            () -> new IntLaconValue(getValue() * BooleanLaconValue.castToIntValue((boolean) value.getValue())),
            () -> unsupportedOperation("*", LaconBuiltInType.INT, LaconBuiltInType.LIST)
        ));
    }

    @Nonnull
    @Override
    public LaconValue<?> div(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> new IntLaconValue(getValue() / (long) value.getValue()),
            () -> new RealLaconValue((double) getValue() / (double) value.getValue()),
            () -> unsupportedOperation("/", LaconBuiltInType.INT, LaconBuiltInType.STRING),
            () -> unsupportedOperation("/", LaconBuiltInType.INT, LaconBuiltInType.BOOLEAN),
            () -> unsupportedOperation("/", LaconBuiltInType.INT, LaconBuiltInType.LIST)
        ));
    }

    @Nonnull
    @Override
    public LaconValue<?> modulus(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> new IntLaconValue(getValue() % (long) value.getValue()),
            () -> new RealLaconValue((double) getValue() % (double) value.getValue()),
            () -> unsupportedOperation("%", LaconBuiltInType.INT, LaconBuiltInType.STRING),
            () -> unsupportedOperation("%", LaconBuiltInType.INT, LaconBuiltInType.BOOLEAN),
            () -> unsupportedOperation("%", LaconBuiltInType.INT, LaconBuiltInType.LIST)
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
        return new BooleanLaconValue(!castToBoolValue(getValue()));
    }

    public static double castToReal(long value) {
        return (double) value;
    }

    public static boolean castToBoolValue(@Nonnull LaconValue<?> laconValue) {
        if (laconValue.getType() != LaconBuiltInType.INT) {
            throw new IllegalStateException("Unexpected type: " + laconValue.getType() + ". Expected: " + LaconBuiltInType.INT);
        }
        return castToBoolValue((long) laconValue.getValue());
    }

    public static boolean castToBoolValue(long value) {
        return value != 0;
    }
}
