package ua.nechay.lacon.core;

import ua.nechay.lacon.core.val.BooleanLaconValue;
import ua.nechay.lacon.core.val.IntLaconValue;
import ua.nechay.lacon.core.val.ListLaconValue;
import ua.nechay.lacon.core.val.RealLaconValue;
import ua.nechay.lacon.core.val.StringLaconValue;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
