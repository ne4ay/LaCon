package ua.nechay.lacon;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 05.03.2023
 */
public class LaconUtils {

    public static boolean isSpace(char character) {
        return character == ' ' || character == '\t' || character == '\n';
    }
}
