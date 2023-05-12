package ua.nechay.lacon.core.function.built;

import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconType;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.function.FunctionLaconValue;
import ua.nechay.lacon.core.function.LaconFunctionArgument;
import ua.nechay.lacon.core.val.VoidLaconValue;
import ua.nechay.lacon.core.var.LaconVariable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Function;

/**
 * @author anechaev
 * @since 12.05.2023
 */
public class LaconBuiltInPrintlnFunction extends FunctionLaconValue {
    private static final String TEXT_ARGUMENT = "text";

    private static final LaconBuiltInPrintlnFunction INSTANCE = new LaconBuiltInPrintlnFunction(
        List.of(LaconFunctionArgument.create(TEXT_ARGUMENT, LaconBuiltInType.STRING)),
        state -> {
            LaconVariable variable = state.getVar(TEXT_ARGUMENT);
            if (variable == null) {
                throw new IllegalStateException("Mandatory argument " + TEXT_ARGUMENT + " is missing!");
            }
            System.out.println(variable.getValue());
            return VoidLaconValue.create();
        },
        LaconBuiltInType.VOID
    );

    private LaconBuiltInPrintlnFunction(
        @Nonnull List<LaconFunctionArgument> args,
        @Nonnull Function<LaconProgramState, LaconValue<?>> function,
        @Nonnull LaconType returnType)
    {
        super(args, function, returnType);
    }

    @Nonnull
    public static LaconBuiltInPrintlnFunction getInstance() {
        return INSTANCE;
    }
}
