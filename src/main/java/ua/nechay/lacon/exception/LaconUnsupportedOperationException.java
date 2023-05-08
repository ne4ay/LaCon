package ua.nechay.lacon.exception;

import ua.nechay.lacon.core.LaconOperation;
import ua.nechay.lacon.core.LaconType;
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

    public static <T> LaconValue<T> unsupportedOperation(String operation, LaconType type1, LaconType type2) {
        return unsupportedOperation(operation, type1.getRepresentation(), type2.getRepresentation());
    }

    public static <T> LaconValue<T> unsupportedOperation(String operation, String type1, String type2) {
        throw new IllegalStateException("Unable to perform " + operation + " operation between types " + type1 + " and " + type2);
    }

    public static <T> LaconValue<T> unsupportedOperation(LaconOperation operation, LaconType type) {
        return unsupportedOperation(operation.getRepresentation(), type);
    }

    public static <T> LaconValue<T> unsupportedOperation(String operation, LaconType type) {
        return unsupportedOperation(operation, type.getRepresentation());
    }

    public static <T> LaconValue<T> unsupportedOperation(String operation, String type) {
        throw new IllegalStateException("Unable to perform " + operation + " operation on type " + type);
    }
}
