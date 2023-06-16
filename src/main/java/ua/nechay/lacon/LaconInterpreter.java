package ua.nechay.lacon;

import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.function.FunctionLaconValue;
import ua.nechay.lacon.core.function.built.BuiltInFunction;
import ua.nechay.lacon.core.function.built.LaconBuiltInPrintFunction;
import ua.nechay.lacon.core.function.built.external.LaconBuiltInExternalCall;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author anechaev
 * @since 08.03.2023
 */
public class LaconInterpreter implements Interpreter {

    private final Consumer<String> prompter;
    private final Function<String, String> callReferrer;
    private final Map<String, FunctionLaconValue> importedFunctions;

    public LaconInterpreter(@Nonnull Consumer<String> prompter, @Nonnull Function<String, String> callReferrer,
        @Nonnull Map<String, FunctionLaconValue> importedFunctions)
    {
        this.prompter = prompter;
        this.callReferrer = callReferrer;
        this.importedFunctions = importedFunctions;
    }

    @Nonnull
    private LaconProgramState createInitialState() {
        LaconBuiltInExternalCall externalCallFunction = LaconBuiltInExternalCall.create(callReferrer);
        return LaconProgramState.create(externalCallFunction, importedFunctions)
            .putFunction(BuiltInFunction.PRINT.getName(), LaconBuiltInPrintFunction.createPrint(prompter))
            .putFunction(BuiltInFunction.PRINTLN.getName(), LaconBuiltInPrintFunction.createPrintln(prompter));
    }

    @Nonnull
    public LaconProgramState interpret(@Nonnull Parser parser) {
        return parser.parse().interpret(createInitialState());
    }
}