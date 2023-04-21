package ua.nechay.lacon.core.val;

import ua.nechay.lacon.core.LaconType;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.touch.SimpleTypeTouch;
import ua.nechay.lacon.core.touch.TypeTouch;

import javax.annotation.Nonnull;

import static ua.nechay.lacon.exception.LaconUnsupportedOperationException.unsupportedOperation;

/**
 * @author anechaev
 * @since 15.04.2023
 */
public class RealLaconValue extends LaconValue<Double> {
    public RealLaconValue(@Nonnull Double value) {
        super(value, LaconType.REAL);
    }

    @Nonnull @Override
    public LaconValue<?> plus(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> new RealLaconValue(getValue() + (double) value.getValue()),
            () -> new RealLaconValue(getValue() + (double) value.getValue()),
            () -> new StringLaconValue(getValue() + (String) value.getValue())
        ));
    }

    @Nonnull @Override
    public LaconValue<?> minus(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> new RealLaconValue(getValue() - (double) value.getValue()),
            () -> new RealLaconValue(getValue() - (double) value.getValue()),
            () -> unsupportedOperation("-", "real", "string")
        ));
    }

    @Nonnull @Override
    public LaconValue<?> mul(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> new RealLaconValue(getValue() * (double) value.getValue()),
            () -> new RealLaconValue(getValue() * (double) value.getValue()),
            () -> unsupportedOperation("*", "real", "string")
        ));
    }

    @Nonnull @Override
    public LaconValue<?> div(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> new RealLaconValue(getValue() / (double) value.getValue()),
            () -> new RealLaconValue(getValue() / (double) value.getValue()),
            () -> unsupportedOperation("/", "real", "string")
        ));
    }

    @Nonnull @Override
    public LaconValue<?> unaryPlus() {
        return new RealLaconValue(+getValue());
    }

    @Nonnull @Override
    public LaconValue<?> unaryMinus() {
        return new RealLaconValue(-getValue());
    }
}
