package ua.nechay.lacon.exception;

/**
 * @author anechaev
 * @since 27.04.2023
 */
public class LaconOutOfBoundsException extends LaconException {
    public LaconOutOfBoundsException(String message) {
        super(message);
    }

    public LaconOutOfBoundsException(String message, Exception ex) {
        super(message, ex);
    }
}
