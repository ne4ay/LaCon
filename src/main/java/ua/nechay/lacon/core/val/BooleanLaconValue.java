package ua.nechay.lacon.core.val;

import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconType;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.function.FunctionLaconValue;
import ua.nechay.lacon.core.touch.SimpleTypeTouch;
import ua.nechay.lacon.core.touch.TypeTouch;
import ua.nechay.lacon.core.touch.TypeTouchBuilder;
import ua.nechay.lacon.core.touch.UnsupportedOperationTypeTouch;

import javax.annotation.Nonnull;

import static ua.nechay.lacon.core.LaconOperation.AND;
import static ua.nechay.lacon.core.LaconOperation.CAST;
import static ua.nechay.lacon.core.LaconOperation.MINUS;
import static ua.nechay.lacon.core.LaconOperation.MUL;
import static ua.nechay.lacon.core.LaconOperation.OR;
import static ua.nechay.lacon.core.LaconOperation.PLUS;
import static ua.nechay.lacon.exception.LaconUnsupportedOperationException.unsupportedOperation;

/**
 * @author anechaev
 * @since 21.04.2023
 */
public class BooleanLaconValue extends LaconValue<Boolean> {
    public BooleanLaconValue(@Nonnull Boolean value) {
        super(value, LaconBuiltInType.BOOLEAN);
    }

    @Nonnull
    @Override
    public LaconValue<?> plus(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), getDefaultTypeTouchBuilder(PLUS, value)
            .setInteger(() -> new IntLaconValue(toInt() + (long) value.getValue()))
            .setString(() -> new StringLaconValue(getValue() + (String) value.getValue()))
            .setBool(() -> new BooleanLaconValue(plus(getValue(), (boolean) value.getValue())))
            .setList(() -> ListLaconValue.addElementAtTheStart((ListLaconValue) value, this))
            .build());
    }

    public static boolean plus(boolean value1, boolean value2) {
        return !value1 || !value2;
    }

    @Nonnull
    @Override
    public LaconValue<?> minus(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), getDefaultTypeTouchBuilder(MINUS, value)
            .setInteger(() -> new IntLaconValue(toInt() - (long) value.getValue()))
            .setBool(() -> new BooleanLaconValue(minus(getValue(), (boolean) value.getValue())))
            .build());
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
        return TypeTouch.touch(value.getType(), getDefaultTypeTouchBuilder(MUL, value)
            .setInteger(() -> new IntLaconValue(toInt() * (long) value.getValue()))
            .setBool(() -> new BooleanLaconValue(getValue() && (boolean) value.getValue()))
            .build());
    }

    @Nonnull
    @Override
    public LaconValue<?> div(@Nonnull LaconValue<?> value) {
        return UnsupportedOperationTypeTouch.unsupported("/", value, getType());
    }

    @Override
    protected int compare(Boolean obj1, Boolean obj2) {
        return Boolean.compare(obj1, obj2);
    }

    @Nonnull
    @Override
    public LaconValue<?> or(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), getDefaultTypeTouchBuilder(OR, value)
            .setInteger(() -> new BooleanLaconValue(getValue() || IntLaconValue.castToBoolValue(value)))
            .setString(() -> new BooleanLaconValue(getValue() || StringLaconValue.castToBoolValue(value)))
            .setBool(() -> new BooleanLaconValue(getValue() || (boolean) value.getValue()))
            .setList(() -> new BooleanLaconValue(getValue() || ListLaconValue.castToBoolValue(value)))
            .build());
    }

    @Nonnull
    @Override
    public LaconValue<?> and(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), getDefaultTypeTouchBuilder(AND, value)
            .setInteger(() -> new BooleanLaconValue(getValue() && IntLaconValue.castToBoolValue(value)))
            .setString(() -> new BooleanLaconValue(getValue() && StringLaconValue.castToBoolValue(value)))
            .setBool(() -> new BooleanLaconValue(getValue() && (boolean) value.getValue()))
            .setList(() -> new BooleanLaconValue(getValue() && ListLaconValue.castToBoolValue(value)))
            .build());
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

    @Nonnull
    @Override
    public LaconValue<?> castTo(@Nonnull LaconType type) {
        return TypeTouch.touch(type, TypeTouchBuilder.<LaconValue<?>>create(() -> unsupported(CAST, type))
            .setInteger( () -> new IntLaconValue(BooleanLaconValue.castToIntValue(getValue())))
            .setString( () -> new StringLaconValue(String.valueOf(getValue())))
            .setBool(() -> this)
            .setList(() -> ListLaconValue.create(this))
            .setFunction(() -> FunctionLaconValue.createSupplier(this))
            .build());
    }

    public static Long castToIntValue(boolean logicValue) {
        if (logicValue) {
            return 1L;
        } else {
            return 0L;
        }
    }

    public static Long castToIntValue(@Nonnull LaconValue<?> laconValue) {
        if (laconValue.getType() != LaconBuiltInType.BOOLEAN) {
            throw new IllegalStateException("Unexpected type: " + laconValue.getType() + ". Expected: " + LaconBuiltInType.BOOLEAN);
        }
        return castToIntValue((boolean) laconValue.getValue());
    }


    private Long toInt() {
        return castToIntValue(getValue());
    }

}
