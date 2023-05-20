package ua.nechay.lacon.core.val;

import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconType;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.function.FunctionLaconValue;
import ua.nechay.lacon.core.function.LaconMethodName;
import ua.nechay.lacon.core.function.built.LaconBuiltInSizeMethod;
import ua.nechay.lacon.core.function.built.string.LaconBuiltInSplitMethod;
import ua.nechay.lacon.core.function.built.string.LaconBuiltInSubstringMethod;
import ua.nechay.lacon.core.touch.TypeTouch;
import ua.nechay.lacon.core.touch.TypeTouchBuilder;
import ua.nechay.lacon.core.touch.UnsupportedOperationTypeTouch;
import ua.nechay.lacon.exception.LaconOutOfBoundsException;
import ua.nechay.lacon.utils.Pair;

import javax.annotation.Nonnull;
import java.util.Map;

import static ua.nechay.lacon.core.LaconOperation.CAST;
import static ua.nechay.lacon.core.LaconOperation.GET_BY_INDEX;
import static ua.nechay.lacon.core.LaconOperation.MINUS;
import static ua.nechay.lacon.core.LaconOperation.MUL;
import static ua.nechay.lacon.core.LaconOperation.PLUS;
import static ua.nechay.lacon.core.LaconValueUtils.multipleStrings;
import static ua.nechay.lacon.core.function.MethodName.toMap;

/**
 * @author anechaev
 * @since 21.04.2023
 */
public class StringLaconValue extends LaconValue<String> {
    private static final Map<String, FunctionLaconValue> METHODS = toMap(
        new Pair<>(LaconMethodName.SIZE, LaconBuiltInSizeMethod.getInstance()),
        new Pair<>(LaconMethodName.SUB_STRING, LaconBuiltInSubstringMethod.getInstance()),
        new Pair<>(LaconMethodName.SPLIT, LaconBuiltInSplitMethod.getInstance())
    );

    public StringLaconValue(@Nonnull String value) {
        super(value, LaconBuiltInType.STRING);
    }

    @Nonnull
    @Override
    public LaconValue<?> plus(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), getDefaultTypeTouchBuilder(PLUS, value)
            .setInteger(() -> new StringLaconValue(getValue() + value.getValue()))
            .setReal(() -> new StringLaconValue(getValue() + value.getValue()))
            .setString(() -> new StringLaconValue(getValue() + value.getValue()))
            .setBool(() -> new StringLaconValue(getValue() + value.getValue()))
            .setList(() -> ListLaconValue.addElementAtTheStart((ListLaconValue) value, this))
            .build());
    }

    @Nonnull
    @Override
    public LaconValue<?> minus(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), getDefaultTypeTouchBuilder(MINUS, value)
            .setString(() -> new StringLaconValue(subtractStrings(getValue(), (String) value.getValue())))
            .build());
    }

    @Nonnull
    public String subtractStrings(@Nonnull String text1, @Nonnull String text2) {
        return text1.replace(text2, "");
    }

    @Nonnull
    @Override
    public LaconValue<?> mul(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), getDefaultTypeTouchBuilder(MUL, value)
            .setInteger(() -> new StringLaconValue(multipleStrings(getValue(), (long) value.getValue())))
            .build());
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
        return TypeTouch.touch(value.getType(), getDefaultTypeTouchBuilder(GET_BY_INDEX, value)
            .setInteger(() -> getByIndex((int)(long)value.getValue()))
            .build());
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

    @Override
    public Map<String, FunctionLaconValue> getMethods() {
        return METHODS;
    }

    @Nonnull
    @Override
    public LaconValue<?> contains(@Nonnull LaconValue<?> value) {
        if (!value.getType().equals(LaconBuiltInType.STRING)) {
            return new BooleanLaconValue(false);
        }
        String seq = ((StringLaconValue)value).getValue();
        return new BooleanLaconValue(getValue().contains(seq));
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

    @Nonnull
    @Override
    public LaconValue<?> castTo(@Nonnull LaconType type) {
        return TypeTouch.touch(type, TypeTouchBuilder.<LaconValue<?>>create(() -> unsupported(CAST, type))
            .setInteger(() -> new IntLaconValue(Long.parseLong(getValue())))
            .setReal(() -> new RealLaconValue(Double.parseDouble(getValue())))
            .setString(() -> this)
            .setBool(() -> new BooleanLaconValue(castToBoolValue(getValue())))
            .setList(() -> ListLaconValue.create(this))
            .setFunction(() -> FunctionLaconValue.createSupplier(this))
            .build());
    }

    public static boolean castToBoolValue(@Nonnull LaconValue<?> laconValue) {
        if (laconValue.getType() != LaconBuiltInType.STRING) {
            throw new IllegalStateException("Unexpected type: " + laconValue.getType() + ". Expected: " + LaconBuiltInType.STRING);
        }
        return castToBoolValue((String) laconValue.getValue());
    }

    public static boolean castToBoolValue(@Nonnull String value) {
        if ("false".equals(value)) {
            return false;
        }
        return !value.isBlank();
    }
}
