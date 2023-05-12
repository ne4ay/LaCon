package ua.nechay.lacon.core.val;

import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.touch.SimpleTypeTouch;
import ua.nechay.lacon.core.touch.TypeTouch;
import ua.nechay.lacon.core.touch.UnsupportedOperationTypeTouch;
import ua.nechay.lacon.exception.LaconOutOfBoundsException;

import javax.annotation.Nonnull;

import static ua.nechay.lacon.core.LaconOperation.GET_BY_INDEX;
import static ua.nechay.lacon.core.LaconOperation.MINUS;
import static ua.nechay.lacon.core.LaconOperation.MUL;
import static ua.nechay.lacon.core.LaconOperation.PLUS;
import static ua.nechay.lacon.core.LaconValueUtils.multipleStrings;
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
            () -> ListLaconValue.addElementAtTheStart((ListLaconValue) value, this),
            () -> unsupported(PLUS, LaconBuiltInType.FUNCTION)
        ));
    }

    @Nonnull
    @Override
    public LaconValue<?> minus(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> unsupported(MINUS, LaconBuiltInType.INT),
            () -> unsupported(MINUS, LaconBuiltInType.REAL),
            () -> new StringLaconValue(subtractStrings(getValue(), (String) value.getValue())),
            () -> unsupported(MINUS, LaconBuiltInType.BOOLEAN),
            () -> ListLaconValue.removeElement((ListLaconValue) value, this),
            () -> unsupported(MINUS, LaconBuiltInType.FUNCTION)
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
            () -> unsupported(MUL, LaconBuiltInType.REAL),
            () -> unsupported(MUL, LaconBuiltInType.STRING),
            () -> unsupported(MUL, LaconBuiltInType.BOOLEAN),
            () -> unsupported(MUL, LaconBuiltInType.LIST),
            () -> unsupported(MUL, LaconBuiltInType.FUNCTION)
        ));
    }

    @Nonnull
    @Override
    public LaconValue<?> div(@Nonnull LaconValue<?> value) {
        return UnsupportedOperationTypeTouch.unsupported("/", value, getType());
    }

    @Override
    protected int compare(String obj1, String obj2) {
        return obj1.compareTo(obj2);
    }

    @Nonnull
    @Override
    public LaconValue<?> getByIndex(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> getByIndex((int)(long)value.getValue()),
            () -> unsupported(GET_BY_INDEX, LaconBuiltInType.REAL),
            () -> unsupported(GET_BY_INDEX, LaconBuiltInType.STRING),
            () -> unsupported(GET_BY_INDEX, LaconBuiltInType.BOOLEAN),
            () -> unsupported(GET_BY_INDEX, LaconBuiltInType.LIST),
            () -> unsupported(GET_BY_INDEX, LaconBuiltInType.FUNCTION)
        ));
    }

    private LaconValue<?> getByIndex(int index) {
        String text = getValue();
        if (Math.abs(index) >= text.length()) {
            throw new LaconOutOfBoundsException("Unable to get element at index " + index + " in the '" + getValue() + "'");
        }
        int ind = index;
        if (index < 0) {
            ind = text.length() + index;
        }
        return new StringLaconValue(String.valueOf(text.charAt(ind)));
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
