package ua.nechay.lacon.core.touch;

import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconType;

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
    R dict();
    R notBuiltIn();

    default R switcher(@Nonnull LaconType type) {
        if (!type.isBuiltIn()) {
            return notBuiltIn();
        }
        switch ((LaconBuiltInType)type) {
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
        case DICT:
            return dict();
        default:
            throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    static <R> R touch(@Nonnull LaconType type, TypeTouch<R> touch) {
        return touch.switcher(type);
    }
}
