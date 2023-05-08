package ua.nechay.lacon.core.function;

import ua.nechay.lacon.ast.AST;
import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconOperation;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconType;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.val.ListLaconValue;
import ua.nechay.lacon.core.val.MapLaconValue;
import ua.nechay.lacon.core.val.StringLaconValue;
import ua.nechay.lacon.core.var.LaconVariable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Function;

/**
 * @author anechaev
 * @since 02.05.2023
 */
public class FunctionLaconValue extends LaconValue<Function<LaconProgramState,LaconValue<?>>> {

    private final List<LaconFunctionArgument> args;
    private final LaconType returnType;

    public FunctionLaconValue(@Nonnull List<LaconFunctionArgument> args,
        @Nonnull Function<LaconProgramState, LaconValue<?>> function,
        @Nonnull LaconType returnType)
    {
        super(function, LaconBuiltInType.FUNCTION);
        this.args = args;
        this.returnType = returnType;
    }

    public static FunctionLaconValue create(@Nonnull List<LaconFunctionArgument> args, @Nonnull AST body, @Nonnull LaconType returnType) {
        return new FunctionLaconValue(args, toFunction(body, returnType), returnType);
    }

    private static Function<LaconProgramState, LaconValue<?>> toFunction(@Nonnull AST body, @Nonnull LaconType returnType) {
        return state -> {
            LaconValue<?> result = body.interpret(state).popValue();
            if (!result.getType().equals(returnType)) {
                throw new IllegalStateException("The method returned the " + result.getType() + ". " + returnType + " was expected!");
            }
            return result;
        };
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
    public LaconValue<?> call(@Nonnull LaconProgramState currentState, @Nonnull LaconValue<?> value) {
        LaconBuiltInType type = value.getType();
        switch (type) {
        case LIST:
            return positionCall(currentState, value);
        case DICT:
            return namedCall(currentState, value);
        default:
            throw new IllegalStateException("Unknown type of call arguments: " + value);
        }
    }

    @Nonnull
    protected LaconValue<?> positionCall(@Nonnull LaconProgramState currentState, @Nonnull LaconValue<?> args) {
        if (!(args instanceof ListLaconValue)) {
            throw new IllegalStateException("Unexpected args value type:" +  args);
        }
        ListLaconValue argList = (ListLaconValue) args;
        List<LaconFunctionArgument> expectedArgs = getArgs();
        int passingArgsNumber = argList.getValue().size();
        if (passingArgsNumber != expectedArgs.size()) {
            throw new IllegalStateException("Unexpected number of passed args: " + passingArgsNumber
                + ". Expected " + expectedArgs.size() + " instead of");
        }
        LaconProgramState localState = LaconProgramState.create()
            .addFunctions(currentState.getAllFunctions());
        for (int i = 0; i < passingArgsNumber; i++) {
            LaconValue<?> nextPassingArg = argList.getValue().get(i);
            LaconFunctionArgument nextExpectedArgument = expectedArgs.get(i);
            if (!nextExpectedArgument.getType().equals(nextPassingArg.getType())) {
                throw new IllegalStateException("Incompatible type during function call: " + nextPassingArg.getType()
                + ". " + nextExpectedArgument.getType() + " was expected!");
            }
            localState.putVar(nextExpectedArgument.getName(), LaconVariable.initialized(nextPassingArg));
        }
        return getValue().apply(localState);
    }

    @Nonnull
    protected LaconValue<?> namedCall(@Nonnull LaconProgramState currentState, @Nonnull LaconValue<?> args) {
        if (!(args instanceof MapLaconValue)) {
            throw new IllegalStateException("Unexpected args value type:" +  args);
        }
        MapLaconValue argMap = (MapLaconValue) args;
        List<LaconFunctionArgument> expectedArgs = getArgs();
        int passingArgsNumber = argMap.getValue().size();
        if (passingArgsNumber != expectedArgs.size()) {
            throw new IllegalStateException("Unexpected number of passed args: " + passingArgsNumber
                + ". Expected " + expectedArgs.size() + " instead of");
        }
        LaconProgramState localState = LaconProgramState.create()
            .addFunctions(currentState.getAllFunctions());
        for (int i = 0; i < passingArgsNumber; i++) {
            LaconFunctionArgument nextExpectedArgument = expectedArgs.get(i);
            String argName = nextExpectedArgument.getName();
            LaconValue<?> nextPassingArg = argMap.getValue().get(new StringLaconValue(argName));
            if (nextPassingArg == null) {
                throw new IllegalStateException("Unfortunately, the '" + argName + "' was not passed");
            }
            localState.putVar(argName, LaconVariable.initialized(nextPassingArg));
        }
        return getValue().apply(localState);
    }

    @Nonnull
    public List<LaconFunctionArgument> getArgs() {
        return args;
    }

}

