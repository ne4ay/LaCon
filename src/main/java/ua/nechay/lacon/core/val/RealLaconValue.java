package ua.nechay.lacon.core.val;

import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconOperation;
import ua.nechay.lacon.core.LaconType;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.function.FunctionLaconValue;
import ua.nechay.lacon.core.touch.SimpleTypeTouch;
import ua.nechay.lacon.core.touch.TypeTouch;
import ua.nechay.lacon.core.touch.TypeTouchBuilder;

import javax.annotation.Nonnull;

import static ua.nechay.lacon.core.LaconOperation.CAST;
import static ua.nechay.lacon.core.LaconOperation.DIV;
import static ua.nechay.lacon.core.LaconOperation.MINUS;
import static ua.nechay.lacon.core.LaconOperation.MODULUS;
import static ua.nechay.lacon.core.LaconOperation.MUL;
import static ua.nechay.lacon.core.LaconOperation.PLUS;
import static ua.nechay.lacon.exception.LaconUnsupportedOperationException.unsupportedOperation;

/**
 * @author anechaev
 * @since 15.04.2023
 */
public class RealLaconValue extends LaconValue<Double> {
    public RealLaconValue(double value) {
        super(value, LaconBuiltInType.REAL);
    }

    @Nonnull @Override
    public LaconValue<?> plus(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), getDefaultTypeTouchBuilder(PLUS, value)
            .setInteger(() -> new RealLaconValue(getValue() + IntLaconValue.castToReal((long) value.getValue())))
            .setReal(() -> new RealLaconValue(getValue() + (double) value.getValue()))
            .setString(() -> new StringLaconValue(getValue() + (String) value.getValue()))
            .setList(() -> ListLaconValue.addElementAtTheStart((ListLaconValue)value.getValue(),this))
            .build());
    }

    @Nonnull @Override
    public LaconValue<?> minus(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), getDefaultTypeTouchBuilder(MINUS, value)
            .setInteger(() -> new RealLaconValue(getValue() - IntLaconValue.castToReal((long)value.getValue())))
            .setReal(() -> new RealLaconValue(getValue() - (double) value.getValue()))
            .setList(() -> ListLaconValue.addElement((ListLaconValue)value.getValue(), this))
            .build());
    }

    @Nonnull @Override
    public LaconValue<?> mul(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), getDefaultTypeTouchBuilder(MUL, value)
            .setInteger(() -> new RealLaconValue(getValue() * IntLaconValue.castToReal((long)value.getValue())))
            .setReal(() -> new RealLaconValue(getValue() * (double) value.getValue()))
            .build());
    }

    @Nonnull @Override
    public LaconValue<?> div(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), getDefaultTypeTouchBuilder(DIV, value)
            .setInteger(() -> new RealLaconValue(getValue() / IntLaconValue.castToReal((long)value.getValue())))
            .setReal(() -> new RealLaconValue(getValue() / (double) value.getValue()))
            .build());
    }

    @Nonnull
    @Override
    public LaconValue<?> modulus(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), getDefaultTypeTouchBuilder(MODULUS, value)
            .setInteger(() -> new RealLaconValue(getValue() % IntLaconValue.castToReal((long)value.getValue())))
            .setReal(() -> new RealLaconValue(getValue() % (double) value.getValue()))
            .build());
    }

    @Nonnull
    @Override
    public LaconValue<?> castTo(@Nonnull LaconType type) {
        return TypeTouch.touch(type, TypeTouchBuilder.<LaconValue<?>>create(() -> unsupported(CAST, type))
            .setInteger(() -> new IntLaconValue(RealLaconValue.castToInt(getValue())))
            .setReal(() -> this)
            .setString(() -> new StringLaconValue(String.valueOf(getValue())))
            .setBool(() -> unsupportedOperation(CAST, LaconBuiltInType.REAL, LaconBuiltInType.BOOLEAN))
            .setList(() -> ListLaconValue.create(this))
            .setFunction(() -> FunctionLaconValue.createSupplier(this))
            .build());
    }

    @Override
    protected int compare(Double obj1, Double obj2) {
        return Double.compare(obj1, obj2);
    }

    @Nonnull @Override
    public LaconValue<?> unaryPlus() {
        return new RealLaconValue(+getValue());
    }

    @Nonnull @Override
    public LaconValue<?> unaryMinus() {
        return new RealLaconValue(-getValue());
    }

    @Nonnull
    @Override
    public LaconValue<?> unaryNot() {
        return unsupportedOperation("!", getType().getRepresentation());
    }

    public static long castToInt(double value) {
        return (long) value;
    }
}
