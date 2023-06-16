package ua.nechay.lacon.core.function.built;

import ua.nechay.lacon.core.function.FunctionLaconValue;
import ua.nechay.lacon.core.function.built.regex.LaconBuiltInFindAllFunction;
import ua.nechay.lacon.core.function.built.regex.LaconBuiltInSearchFunction;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 12.05.2023
 */
public enum BuiltInFunction {
    PRINTLN("println", FunctionLaconValue.EMPTY), // SHOULD BE OVERRIDDEN 💅
    PRINT("print", FunctionLaconValue.EMPTY), // SHOULD BE OVERRIDDEN 💅
    SEARCH("search", LaconBuiltInSearchFunction.getInstance()),
    FIND_ALL("find_all", LaconBuiltInFindAllFunction.getInstance())
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
