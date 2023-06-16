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
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author anechaev
 * @since 12.05.2023
 */
public class LaconBuiltInPrintFunction extends FunctionLaconValue {
    private static final String TEXT_ARGUMENT_NAME = "text";
    private static final LaconFunctionArgument TEXT_ARGUMENT = LaconFunctionArgument.create(TEXT_ARGUMENT_NAME, LaconBuiltInType.STRING);

    private LaconBuiltInPrintFunction(
        @Nonnull List<LaconFunctionArgument> args,
        @Nonnull Function<LaconProgramState, LaconValue<?>> function,
        @Nonnull LaconType returnType)
    {
        super(args, function, returnType);
    }

    public static LaconBuiltInPrintFunction createPrint(@Nonnull Consumer<String> prompter) {
        return new LaconBuiltInPrintFunction(List.of(
            TEXT_ARGUMENT),
            state -> {
                LaconVariable variable = state.getVar(TEXT_ARGUMENT_NAME);
                if (variable == null) {
                    throw new IllegalStateException("Mandatory argument " + TEXT_ARGUMENT + " is missing!");
                }
                prompter.accept(variable.getValue().toString());
                return VoidLaconValue.create();
            },
            LaconBuiltInType.VOID);
    }

    public static LaconBuiltInPrintFunction createPrintln(@Nonnull Consumer<String> prompter) {
        return new LaconBuiltInPrintFunction(List.of(
            TEXT_ARGUMENT),
            state -> {
                LaconVariable variable = state.getVar(TEXT_ARGUMENT_NAME);
                if (variable == null) {
                    throw new IllegalStateException("Mandatory argument " + TEXT_ARGUMENT + " is missing!");
                }
                prompter.accept(variable.getValue() + "\n");
                return VoidLaconValue.create();
            },
            LaconBuiltInType.VOID);
    }
}