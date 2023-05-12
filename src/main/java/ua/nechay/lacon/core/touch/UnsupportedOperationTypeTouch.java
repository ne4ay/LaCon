package ua.nechay.lacon.core.touch;

import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconType;
import ua.nechay.lacon.core.LaconValue;

import javax.annotation.Nonnull;

import static ua.nechay.lacon.exception.LaconUnsupportedOperationException.unsupportedOperation;

/**
 * @author anechaev
 * @since 23.04.2023
 */
public class UnsupportedOperationTypeTouch implements TypeTouch<LaconValue<?>> {

    private final String operation;
    private final LaconType fromType;

    protected UnsupportedOperationTypeTouch(@Nonnull String operation, @Nonnull LaconType fromType) {
        this.operation = operation;
        this.fromType = fromType;
    }

    @Nonnull
    public static UnsupportedOperationTypeTouch create(@Nonnull String operation, @Nonnull LaconType fromType) {
        return new UnsupportedOperationTypeTouch(operation, fromType);
    }

    public static LaconValue<?> unsupported(@Nonnull String operation, @Nonnull LaconValue<?> value, @Nonnull LaconType fromType) {
        return TypeTouch.touch(value.getType(), create(operation, fromType));
    }

    @Override
    public LaconValue<?> integer() {
        return unsupportedOperation(operation, fromType, LaconBuiltInType.INT);
    }

    @Override
    public LaconValue<?> real() {
        return unsupportedOperation(operation, fromType, LaconBuiltInType.REAL);
    }

    @Override
    public LaconValue<?> string() {
        return unsupportedOperation(operation, fromType, LaconBuiltInType.STRING);
    }

    @Override
    public LaconValue<?> bool() {
        return unsupportedOperation(operation, fromType, LaconBuiltInType.BOOLEAN);
    }

    @Override
    public LaconValue<?> list() {
        return unsupportedOperation(operation, fromType, LaconBuiltInType.LIST);
    }

    @Override
    public LaconValue<?> function() {
        return unsupportedOperation(operation, fromType, LaconBuiltInType.FUNCTION);
    }
}
