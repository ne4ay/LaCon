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
 * @since 21.04.2023
 */
public class StringLaconValue extends LaconValue<String> {
    public StringLaconValue(@Nonnull String value) {
        super(value, LaconType.STRING);
    }

    @Nonnull
    @Override
    public LaconValue<?> plus(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> new StringLaconValue(getValue() + value.getValue()),
            () -> new StringLaconValue(getValue() + value.getValue()),
            () -> new StringLaconValue(getValue() + value.getValue())
        ));
    }

    @Nonnull
    @Override
    public LaconValue<?> minus(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> unsupportedOperation("-", "string", "int"),
            () -> unsupportedOperation("-", "string", "real"),
            () -> new StringLaconValue(subtractStrings(getValue(), (String) value.getValue()))
        ));
    }

    @Nonnull
    public String subtractStrings(@Nonnull String text1, @Nonnull String text2) {
        return text1.replace(text2, "");
    }

    @Nonnull
    @Override
    public LaconValue<?> mul(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> new StringLaconValue(multipleStrings(getValue(), (long)value.getValue())),
            () -> unsupportedOperation("*", "string", "real"),
            () -> unsupportedOperation("*", "string", "string")
        ));
    }

    @Nonnull
    @Override
    public LaconValue<?> div(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> unsupportedOperation("/", "string", "int"),
            () -> unsupportedOperation("/", "string", "real"),
            () -> unsupportedOperation("/", "string", "string")
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
        return new StringLaconValue(new StringBuilder(getValue()).reverse().toString());
    }
}
