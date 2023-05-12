package ua.nechay.lacon.core.touch;

import ua.nechay.lacon.core.LaconBuiltInType;

import javax.annotation.Nonnull;

/**
 * Just to decrease amount of switch statements ))
 *
 * @author anechaev
 * @since 15.04.2023
 */
public interface TypeTouch<R> {

    R integer();
    R real();
    R string();
    R bool();
    R list();
    R function();

    default R switcher(@Nonnull LaconBuiltInType type) {
        switch (type) {
        case INT:
            return integer();
        case REAL:
            return real();
        case STRING:
            return string();
        case BOOLEAN:
            return bool();
        case LIST:
            return list();
        case FUNCTION:
            return function();
        default:
            throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    static <R> R touch(@Nonnull LaconBuiltInType type, TypeTouch<R> touch) {
        return touch.switcher(type);
    }
}
