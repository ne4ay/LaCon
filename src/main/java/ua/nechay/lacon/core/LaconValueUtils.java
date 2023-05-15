package ua.nechay.lacon.core;

import ua.nechay.lacon.LaconToken;
import ua.nechay.lacon.core.function.FunctionLaconValue;
import ua.nechay.lacon.core.touch.SimpleTypeTouch;
import ua.nechay.lacon.core.touch.TypeTouch;
import ua.nechay.lacon.core.val.BooleanLaconValue;
import ua.nechay.lacon.core.val.IntLaconValue;
import ua.nechay.lacon.core.val.ListLaconValue;
import ua.nechay.lacon.core.val.RealLaconValue;
import ua.nechay.lacon.core.val.StringLaconValue;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ua.nechay.lacon.core.LaconOperation.CAST;
import static ua.nechay.lacon.exception.LaconUnsupportedOperationException.unsupportedOperation;

/**
 * @author anechaev
 * @since 21.04.2023
 */
public final class LaconValueUtils {

    private LaconValueUtils() { }

    @Nonnull
    public static String multipleStrings(@Nonnull String text, long times) {
        if (times == 0L) {
            return "";
        }
        StringBuilder builderToMultiply = new StringBuilder(text);
        if (times < 0) {
            builderToMultiply.reverse();
        }
        String textToMultiply = builderToMultiply.toString();
        long to = Math.abs(times) - 1;
        for (int i = 0; i < to; i++) {
            builderToMultiply.append(textToMultiply);
        }
        return builderToMultiply.toString();
    }

    @Nonnull
    public static List<LaconValue<?>> revert(@Nonnull List<LaconValue<?>> list) {
        Collections.reverse(list);
        return list;
    }

    @Nonnull
    public static List<LaconValue<?>> multiply(@Nonnull List<LaconValue<?>> list, long multiplier) {
        if (multiplier == 0L) {
            return new ArrayList<>();
        }
        List<LaconValue<?>> part;
        if (multiplier < 0) {
            part = revert(list);
        } else {
            part = list;
        }
        List<LaconValue<?>> result = new ArrayList<>();
        long to = Math.abs(multiplier);
        for (int i = 0; i < to; i++) {
            result.addAll(part);
        }
        return result;
    }

    @Nonnull
    public static LaconValue<?> less(LaconValue<?> val1, LaconValue<?> val2) {
        return new BooleanLaconValue(val1.compareTo(val2) < 0);
    }

    @Nonnull
    public static LaconValue<?> lessOrEqual(LaconValue<?> val1, LaconValue<?> val2) {
        return new BooleanLaconValue(val1.compareTo(val2) < 1);
    }

    @Nonnull
    public static LaconValue<?> greater(LaconValue<?> val1, LaconValue<?> val2) {
        return new BooleanLaconValue(val1.compareTo(val2) > 0);
    }

    @Nonnull
    public static LaconValue<?> greaterOrEqual(LaconValue<?> val1, LaconValue<?> val2) {
        return new BooleanLaconValue(val1.compareTo(val2) > -1);
    }

    public static LaconBuiltInType determineType(@Nonnull LaconToken typeToken) {
        String typeRepresentation = typeToken.getText();
        LaconBuiltInType type = LaconBuiltInType.getForRepresentation(typeRepresentation);
        if (type == null) {
            throw new IllegalStateException("Unknown type: " + typeRepresentation);
        }
        return type;
    }

    public static LaconValue<?> castToStr(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> new StringLaconValue(String.valueOf((long)value.getValue())),
            () -> new StringLaconValue(String.valueOf((double)value.getValue())),
            () -> value,
            () -> new StringLaconValue(String.valueOf((boolean) value.getValue())),
            () -> new StringLaconValue(ListLaconValue.castToStrValue(value)),
            () -> FunctionLaconValue.castToStringValue((FunctionLaconValue) value)
        ));
    }

    @Nonnull
    public static Function<Double, LaconValue<?>> determineMapper(@Nonnull LaconBuiltInType type) {
        switch (type) {
        case INT:
            return IntLaconValue::fromReal;
        case REAL:
            return RealLaconValue::new;
        default:
            throw new IllegalStateException("Unknown range type " + type + " for creating the list from it!");
        }
    }

    @Nonnull
    public static LaconValue<?> fromObject(Object obj) {
        if (obj instanceof Long) {
            return new IntLaconValue((Long) obj);
        } else if (obj instanceof Integer) {
            return new IntLaconValue((Integer) obj);
        } else if (obj instanceof Double) {
            return new RealLaconValue((Double) obj);
        } else if (obj instanceof Float) {
            return new RealLaconValue((Float) obj);
        } else if (obj instanceof String) {
            return new StringLaconValue((String) obj);
        } else if (obj instanceof Boolean) {
            return new BooleanLaconValue((Boolean) obj);
        } else if (obj instanceof List) {
            return new ListLaconValue((List<LaconValue<?>>) obj);
        } else {
            throw new IllegalStateException();
        }
    }
}
