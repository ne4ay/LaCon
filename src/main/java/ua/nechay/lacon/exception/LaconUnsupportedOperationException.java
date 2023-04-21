package ua.nechay.lacon.exception;

import ua.nechay.lacon.core.LaconValue;

/**
 * @author anechaev
 * @since 21.04.2023
 */
public class LaconUnsupportedOperationException extends LaconException {


    public LaconUnsupportedOperationException(String message) {
        super(message);
    }

    public LaconUnsupportedOperationException(String message, Exception ex) {
        super(message, ex);
    }

    public static LaconValue<?> unsupportedOperation(String operation, String type1, String type2) {
        throw new IllegalStateException("Unable to perform " + operation + " operation between types " + type1 + " and " + type2);
    }
}
