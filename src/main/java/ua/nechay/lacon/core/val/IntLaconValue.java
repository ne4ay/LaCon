package ua.nechay.lacon.core.val;

import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconType;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.function.FunctionLaconValue;
import ua.nechay.lacon.core.touch.TypeTouch;
import ua.nechay.lacon.core.touch.TypeTouchBuilder;

import javax.annotation.Nonnull;

import static ua.nechay.lacon.core.LaconOperation.CAST;
import static ua.nechay.lacon.core.LaconOperation.DIV;
import static ua.nechay.lacon.core.LaconOperation.MINUS;
import static ua.nechay.lacon.core.LaconOperation.MODULUS;
import static ua.nechay.lacon.core.LaconOperation.MUL;
import static ua.nechay.lacon.core.LaconOperation.PLUS;
import static ua.nechay.lacon.core.LaconValueUtils.multipleStrings;

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
        return TypeTouch.touch(value.getType(), getDefaultTypeTouchBuilder(PLUS, value)
            .setInteger(() -> new IntLaconValue(getValue() + (long) value.getValue()))
            .setReal(() -> new RealLaconValue((double) getValue() + (double) value.getValue()))
            .setString(() -> new StringLaconValue(getValue() + (String) value.getValue()))
            .setBool(() -> new IntLaconValue(getValue() + BooleanLaconValue.castToIntValue(value)))
            .setList(() -> ListLaconValue.addElementAtTheStart((ListLaconValue) value, this))
            .build());
    }

    @Nonnull
    @Override
    public LaconValue<?> minus(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), getDefaultTypeTouchBuilder(MINUS, value)
            .setInteger(() -> new IntLaconValue(getValue() - (long) value.getValue()))
            .setReal(() -> new RealLaconValue((double) getValue() - (double) value.getValue()))
            .setBool(() -> new IntLaconValue(getValue() - BooleanLaconValue.castToIntValue((boolean) value.getValue())))
            .build());
    }

    @Nonnull
    @Override
    public LaconValue<?> mul(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), getDefaultTypeTouchBuilder(MUL, value)
            .setInteger(() -> new IntLaconValue(getValue() * (long) value.getValue()))
            .setReal(() -> new RealLaconValue((double) getValue() * (double) value.getValue()))
            .setString(() -> new StringLaconValue(multipleStrings((String) value.getValue(), getValue())))
            .setBool(() -> new IntLaconValue(getValue() * BooleanLaconValue.castToIntValue((boolean) value.getValue())))
            .setList(() -> ListLaconValue.multiplyList((ListLaconValue)value, getValue()))
            .setFunction(() -> FunctionLaconValue.multiplyFunction((FunctionLaconValue)value, getValue()))
            .build());
    }

    @Nonnull
    @Override
    public LaconValue<?> div(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), getDefaultTypeTouchBuilder(DIV, value)
            .setInteger(() -> new IntLaconValue(getValue() / (long) value.getValue()))
            .setReal(() -> new RealLaconValue((double) getValue() / (double) value.getValue()))
            .build());
    }

    @Nonnull
    @Override
    public LaconValue<?> modulus(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), getDefaultTypeTouchBuilder(MODULUS, value)
            .setInteger(() -> new IntLaconValue(getValue() % (long) value.getValue()))
            .setReal(() -> new RealLaconValue((double) getValue() % (double) value.getValue()))
            .build());
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

    @Nonnull
    @Override
    public LaconValue<?> castTo(@Nonnull LaconType type) {
        return TypeTouch.touch(type, TypeTouchBuilder.<LaconValue<?>>create(() -> unsupported(CAST, type))
            .setInteger(() -> this)
            .setReal(() -> new RealLaconValue(IntLaconValue.castToReal(getValue())))
            .setString(() -> new StringLaconValue(String.valueOf(getValue())))
            .setBool(() -> new BooleanLaconValue(IntLaconValue.castToBoolValue(getValue())))
            .setList(() -> ListLaconValue.create(this))
            .build());
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
