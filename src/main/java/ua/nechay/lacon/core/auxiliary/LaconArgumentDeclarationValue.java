package ua.nechay.lacon.core.auxiliary;

import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconValue;

import javax.annotation.Nonnull;

import static ua.nechay.lacon.exception.LaconUnsupportedOperationException.unsupportedOperation;

/**
 * @author anechaev
 * @since 03.05.2023
 */
public class LaconArgumentDeclarationValue extends LaconValue<String> {
    public LaconArgumentDeclarationValue(@Nonnull String value, @Nonnull LaconBuiltInType type) {
        super(value, type);
    }

    @Nonnull
    @Override
    public LaconValue<?> plus(@Nonnull LaconValue<?> value) {
        return unsupported("+", value);
    }

    @Nonnull
    @Override
    public LaconValue<?> minus(@Nonnull LaconValue<?> value) {
        return unsupported("-", value);
    }

    @Nonnull
    @Override
    public LaconValue<?> mul(@Nonnull LaconValue<?> value) {
        return unsupported("*", value);
    }

    @Nonnull
    @Override
    public LaconValue<?> div(@Nonnull LaconValue<?> value) {
        return unsupported("/", value);
    }

    @Nonnull
    @Override
    public LaconValue<?> unaryPlus() {
        return unsupportedOperation("+", getType());
    }

    @Nonnull
    @Override
    public LaconValue<?> unaryMinus() {
        return unsupportedOperation("-", getType());
    }

    @Nonnull
    @Override
    public LaconValue<?> unaryNot() {
        return unsupportedOperation("!", getType());
    }
}
