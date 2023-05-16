package ua.nechay.lacon.core.function;

import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconType;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.var.LaconVariable;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 16.05.2023
 */
public final class LaconFunctionUtils {

    private LaconFunctionUtils() { }

    @Nonnull
    public static LaconValue<?> getMandatoryArgument(@Nonnull LaconProgramState state, @Nonnull String argumentName, @Nonnull LaconType type) {
        LaconVariable var = state.getVar(argumentName);
        if (var == null) {
            throw new IllegalStateException("Mandatory argument " + argumentName + " is missing!");
        }
        LaconValue<?> val = var.getValueObject();
        if (!val.getType().equals(type)) {
            throw new IllegalStateException("Mandatory argument " + argumentName + " has wrong type: " + val.getType() + ". Expected type: " + type);
        }
        return val;
    }
}
