package ua.nechay.lacon.core.function.built;

import ua.nechay.lacon.core.function.FunctionLaconValue;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 12.05.2023
 */
public enum BuiltInFunction {
    PRINT("print", LaconBuiltPrintFunction.getInstance()),
    PRINTLN("println", LaconBuiltInPrintlnFunction.getInstance())
    ;

    private final String name;
    private final FunctionLaconValue function;

    BuiltInFunction(@Nonnull String name, @Nonnull FunctionLaconValue function) {
        this.name = name;
        this.function = function;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public FunctionLaconValue getFunction() {
        return function;
    }
}
