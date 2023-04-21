package ua.nechay.lacon.core.val;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 21.04.2023
 */
public final class LaconValueUtils {

    private LaconValueUtils() { }

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
}
