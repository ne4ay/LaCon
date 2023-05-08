package ua.nechay.lacon.core.val;

import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconOperation;
import ua.nechay.lacon.core.LaconValue;

import javax.annotation.Nonnull;
import java.util.Map;

import static ua.nechay.lacon.exception.LaconUnsupportedOperationException.unsupportedOperation;

/**
 * @author anechaev
 * @since 07.05.2023
 */
public class MapLaconValue extends LaconValue<Map<LaconValue<?>, LaconValue<?>>> {
    public MapLaconValue(@Nonnull Map<LaconValue<?>, LaconValue<?>> value) {
        super(value, LaconBuiltInType.DICT);
    }

    @Nonnull
    @Override
    public LaconValue<?> plus(@Nonnull LaconValue<?> value) {
        return unsupported(LaconOperation.PLUS, value);
    }

    @Nonnull
    @Override
    public LaconValue<?> minus(@Nonnull LaconValue<?> value) {
        return unsupported(LaconOperation.MINUS, value);
    }

    @Nonnull
    @Override
    public LaconValue<?> mul(@Nonnull LaconValue<?> value) {
        return unsupported(LaconOperation.MUL, value);
    }

    @Nonnull
    @Override
    public LaconValue<?> div(@Nonnull LaconValue<?> value) {
        return unsupported(LaconOperation.DIV, value);
    }

    @Nonnull
    @Override
    public LaconValue<?> unaryPlus() {
        return unsupportedOperation(LaconOperation.PLUS, getType());
    }

    @Nonnull
    @Override
    public LaconValue<?> unaryMinus() {
        return unsupportedOperation(LaconOperation.MINUS, getType());
    }

    @Nonnull
    @Override
    public LaconValue<?> unaryNot() {
        return unsupportedOperation(LaconOperation.NOT, getType());
    }

    @Nonnull
    @Override
    public LaconValue<?> getByIndex(@Nonnull LaconValue<?> value) {
        LaconValue<?> val = getValue().get(value);
        if (val == null) {
            throw new IllegalStateException("Unknown value in map. There is no such key: " + value);
        }
        return val;
    }
}
