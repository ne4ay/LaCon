package ua.nechay.lacon.exception;

import ua.nechay.lacon.core.LaconValue;

/**
 * @author anechaev
 * @since 21.04.2023
 */
public class LaconException extends RuntimeException {

    public LaconException(String message) {
        super(message);
    }

    public LaconException(String message, Exception ex) {
        super(message, ex);
    }
}
