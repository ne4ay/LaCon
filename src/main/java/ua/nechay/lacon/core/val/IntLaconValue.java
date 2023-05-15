package ua.nechay.lacon.core.val;

import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.function.FunctionLaconValue;
import ua.nechay.lacon.core.touch.SimpleTypeTouch;
import ua.nechay.lacon.core.touch.TypeTouch;

import javax.annotation.Nonnull;

import static ua.nechay.lacon.core.LaconOperation.DIV;
import static ua.nechay.lacon.core.LaconOperation.MINUS;
import static ua.nechay.lacon.core.LaconOperation.MODULUS;
import static ua.nechay.lacon.core.LaconOperation.MUL;
import static ua.nechay.lacon.core.LaconOperation.PLUS;
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
            () -> ListLaconValue.addElementAtTheStart((ListLaconValue) value, this),
            () -> unsupported(PLUS, LaconBuiltInType.FUNCTION)
        ));
    }

    @Nonnull
    @Override
    public LaconValue<?> minus(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> new IntLaconValue(getValue() - (long) value.getValue()),
            () -> new RealLaconValue((double) getValue() - (double) value.getValue()),
            () -> unsupported(MINUS, LaconBuiltInType.STRING),
            () -> new IntLaconValue(getValue() - BooleanLaconValue.castToIntValue((boolean) value.getValue())),
            () -> ListLaconValue.removeElement((ListLaconValue) value, this),
            () -> unsupported(MINUS, LaconBuiltInType.FUNCTION)
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
            () -> ListLaconValue.multiplyList((ListLaconValue)value, getValue()),
            () -> FunctionLaconValue.multiplyFunction((FunctionLaconValue)value, getValue())
        ));
    }

    @Nonnull
    @Override
    public LaconValue<?> div(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> new IntLaconValue(getValue() / (long) value.getValue()),
            () -> new RealLaconValue((double) getValue() / (double) value.getValue()),
            () -> unsupported(DIV, LaconBuiltInType.STRING),
            () -> unsupported(DIV, LaconBuiltInType.BOOLEAN),
            () -> unsupported(DIV, LaconBuiltInType.LIST),
            () -> unsupported(DIV, LaconBuiltInType.FUNCTION)
        ));
    }

    @Nonnull
    @Override
    public LaconValue<?> modulus(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> new IntLaconValue(getValue() % (long) value.getValue()),
            () -> new RealLaconValue((double) getValue() % (double) value.getValue()),
            () -> unsupported(MODULUS, LaconBuiltInType.STRING),
            () -> unsupported(MODULUS, LaconBuiltInType.BOOLEAN),
            () -> unsupported(MODULUS, LaconBuiltInType.LIST),
            () -> unsupported(MODULUS, LaconBuiltInType.FUNCTION)
        ));
    }

    @Override
    protected int compare(Long obj1, Long obj2) {
        return Long.compare(obj1, obj2);
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

    public static IntLaconValue fromReal(double value) {
        return new IntLaconValue(RealLaconValue.castToInt(value));
    }
}
