package ua.nechay.lacon.core.val;

import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.touch.SimpleTypeTouch;
import ua.nechay.lacon.core.touch.TypeTouch;
import ua.nechay.lacon.core.touch.UnsupportedOperationTypeTouch;

import javax.annotation.Nonnull;

import static ua.nechay.lacon.core.val.LaconValueUtils.multipleStrings;
import static ua.nechay.lacon.exception.LaconUnsupportedOperationException.unsupportedOperation;

/**
 * @author anechaev
 * @since 21.04.2023
 */
public class StringLaconValue extends LaconValue<String> {
    public StringLaconValue(@Nonnull String value) {
        super(value, LaconBuiltInType.STRING);
    }

    @Nonnull
    @Override
    public LaconValue<?> plus(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> new StringLaconValue(getValue() + value.getValue()),
            () -> new StringLaconValue(getValue() + value.getValue()),
            () -> new StringLaconValue(getValue() + value.getValue()),
            () -> new StringLaconValue(getValue() + value.getValue()),
            () -> ListLaconValue.addElement((ListLaconValue) value, getValue())
        ));
    }

    @Nonnull
    @Override
    public LaconValue<?> minus(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> unsupportedOperation("-", LaconBuiltInType.STRING, LaconBuiltInType.INT),
            () -> unsupportedOperation("-", LaconBuiltInType.STRING, LaconBuiltInType.REAL),
            () -> new StringLaconValue(subtractStrings(getValue(), (String) value.getValue())),
            () -> unsupportedOperation("-", LaconBuiltInType.STRING, LaconBuiltInType.BOOLEAN),
            () -> ListLaconValue.removeElement((ListLaconValue) value, getValue())
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
            () -> new StringLaconValue(multipleStrings(getValue(), (long) value.getValue())),
            () -> unsupportedOperation("*", LaconBuiltInType.STRING, LaconBuiltInType.REAL),
            () -> unsupportedOperation("*", LaconBuiltInType.STRING, LaconBuiltInType.STRING),
            () -> unsupportedOperation("*", LaconBuiltInType.STRING, LaconBuiltInType.BOOLEAN),
            () -> unsupportedOperation("*", LaconBuiltInType.STRING, LaconBuiltInType.LIST)
        ));
    }

    @Nonnull
    @Override
    public LaconValue<?> div(@Nonnull LaconValue<?> value) {
        return UnsupportedOperationTypeTouch.unsupported("/", value, getType());
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

    @Nonnull
    @Override
    public LaconValue<?> unaryNot() {
        return new BooleanLaconValue(castToBoolValue(getValue()));
    }

    public static boolean castToBoolValue(@Nonnull LaconValue<?> laconValue) {
        if (laconValue.getType() != LaconBuiltInType.STRING) {
            throw new IllegalStateException("Unexpected type: " + laconValue.getType() + ". Expected: " + LaconBuiltInType.STRING);
        }
        return castToBoolValue((String) laconValue.getValue());
    }

    public static boolean castToBoolValue(@Nonnull String value) {
        return !value.isBlank();
    }
}
