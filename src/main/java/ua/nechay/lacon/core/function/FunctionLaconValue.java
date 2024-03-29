package ua.nechay.lacon.core.function;

import ua.nechay.lacon.ast.AST;
import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconOperation;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconType;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.LaconValueUtils;
import ua.nechay.lacon.core.touch.TypeTouch;
import ua.nechay.lacon.core.touch.TypeTouchBuilder;
import ua.nechay.lacon.core.val.DictLaconValue;
import ua.nechay.lacon.core.val.ListLaconValue;
import ua.nechay.lacon.core.val.StringLaconValue;
import ua.nechay.lacon.core.val.VoidLaconValue;
import ua.nechay.lacon.core.var.LaconVariable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ua.nechay.lacon.core.LaconOperation.CAST;
import static ua.nechay.lacon.core.LaconOperation.MUL;
import static ua.nechay.lacon.core.LaconOperation.PLUS;

/**
 * @author anechaev
 * @since 02.05.2023
 */
public class FunctionLaconValue extends LaconValue<Function<LaconProgramState,LaconValue<?>>> {
    public static final FunctionLaconValue EMPTY = createWithoutArgs(state -> VoidLaconValue.create(), LaconBuiltInType.VOID);
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

    public static FunctionLaconValue createWithoutArgs(@Nonnull Function<LaconProgramState, LaconValue<?>> function, @Nonnull LaconType returnType) {
        return new FunctionLaconValue(Collections.emptyList(), function, returnType);
    }

    public static FunctionLaconValue create(@Nonnull List<LaconFunctionArgument> args, @Nonnull AST body, @Nonnull LaconType returnType) {
        return new FunctionLaconValue(args, toFunction(body, returnType), returnType);
    }

    public static FunctionLaconValue create(
        @Nonnull List<LaconFunctionArgument> args,
        @Nonnull Function<LaconProgramState, LaconValue<?>> function,
        @Nonnull LaconType returnType) {
        return new FunctionLaconValue(args, function, returnType);
    }

    public static FunctionLaconValue createSupplier(@Nonnull LaconValue<?> value) {
        return new FunctionLaconValue(Collections.emptyList(), state ->  value, value.getType());
    }

    private static Function<LaconProgramState, LaconValue<?>> toFunction(@Nonnull AST body, @Nonnull LaconType returnType) {
        return state -> {
            LaconValue<?> result = body.interpret(state).popValue();
            if (!returnType.accepts(result.getType())) {
                throw new IllegalStateException("The method returned the " + result.getType() + ". " + returnType + " was expected!");
            }
            return result;
        };
    }

    @Nonnull
    @Override
    public LaconValue<?> plus(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), getDefaultTypeTouchBuilder(PLUS, value)
            .setFunction(() -> addFunctions(this, (FunctionLaconValue) value))
            .build());
    }

    private static FunctionLaconValue addFunctions(FunctionLaconValue fun1, FunctionLaconValue fun2) {
        LaconType fun1ReturnType = fun1.getReturnType();
        List<LaconFunctionArgument> fun2Arguments = fun2.getArgs();
        if (fun2Arguments.isEmpty()) {
            return functionsDoNotMatch(fun1ReturnType, fun2Arguments);
        }
        if (fun2Arguments.size() == 1) {
            LaconFunctionArgument fun2Arg = fun2Arguments.get(0);
            if (!fun2Arg.getType().accepts(fun1ReturnType)) {
                return functionsDoNotMatch(fun1ReturnType, fun2Arguments);
            }
            return new FunctionLaconValue(fun1.getArgs(), state -> {
                LaconValue<?> firstValue = fun1.getValue().apply(state);
                return fun2.call(state, ListLaconValue.create(firstValue));
            }, fun2.getReturnType());
        }
        if (fun1ReturnType.equals(LaconBuiltInType.LIST) || fun1ReturnType.equals(LaconBuiltInType.DICT)) {
            return new FunctionLaconValue(fun1.getArgs(), state -> {
                LaconValue<?> firstValue = fun1.getValue().apply(state);
                return fun2.call(state, firstValue);
            }, fun2.getReturnType());
        }
        return functionsDoNotMatch(fun1ReturnType, fun2Arguments);
    }

    private static FunctionLaconValue functionsDoNotMatch(@Nonnull LaconType fun1ReturnType, @Nonnull List<LaconFunctionArgument> fun2Arguments) {
        throw new IllegalStateException("Unable to compose functions -> return type of first function: " + fun1ReturnType + " does not "
            + "match with arguments of second function: " + fun2Arguments);
    }

    @Nonnull
    @Override
    public LaconValue<?> minus(@Nonnull LaconValue<?> value) {
        return unsupported(LaconOperation.MINUS, value);
    }

    @Nonnull
    @Override
    public LaconValue<?> mul(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), getDefaultTypeTouchBuilder(MUL, value)
            .setInteger(() -> multiplyFunction(this, (long) value.getValue()))
            .setList(() -> multiplyFunction(this, (ListLaconValue) value))
            .build());
    }

    public static FunctionLaconValue multiplyFunction(@Nonnull FunctionLaconValue function, long times) {
        if (times < 1) {
            throw new IllegalStateException("Unable to multiply a function on non-positive number: " + times);
        }
        return new FunctionLaconValue(function.getArgs(), state -> {
            LaconValue<?> value = null;
            for (int i = 0; i < times; i++) {
                value = function.getValue().apply(state);
            }
            return value;
        }, function.getReturnType());
    }

    @Nonnull
    public static FunctionLaconValue multiplyFunction(@Nonnull FunctionLaconValue function, @Nonnull ListLaconValue list) {
        List<LaconFunctionArgument> args = function.getArgs();
        if (args.size() == 0) {
            throw new IllegalStateException("Unable to multiply function without args on a list: " + list.getValue());
        }
        return new FunctionLaconValue(Collections.emptyList(), state -> sequenceCall(state, function, list), LaconBuiltInType.LIST);
    }

    @Nonnull
    private static LaconValue<?> sequenceCall(@Nonnull LaconProgramState state,
        @Nonnull FunctionLaconValue function, @Nonnull ListLaconValue list)
    {
        List<LaconValue<?>> results = new ArrayList<>();
        boolean isSingleArg = function.getArgs().size() == 1;
        for (var nextValue : list.getValue()) {
            LaconValue<?> passingArgument = nextValue;
            if (isSingleArg) {
                passingArgument = new ListLaconValue(List.of(passingArgument));
            }
            LaconValue<?> result = function.call(state, passingArgument);
            results.add(result);
        }
        return new ListLaconValue(results);
    }

    @Nonnull
    @Override
    public LaconValue<?> castTo(@Nonnull LaconType type) {
        return TypeTouch.touch(type, TypeTouchBuilder.<LaconValue<?>>create(() -> unsupported(CAST, type))
            .setString(() -> castToStringValue(this))
            .setFunction(() -> this)
            .setList(() -> ListLaconValue.create(this))
            .build());
    }

    @Nonnull
    @Override
    public LaconValue<?> call(@Nonnull LaconProgramState currentState, @Nonnull LaconValue<?> args) {
        LaconType type = args.getType();
        if (!type.isBuiltIn()) {
            throw new IllegalStateException("Unknown type of call arguments: " + args);
        }
        switch ((LaconBuiltInType) type) {
        case LIST:
            return positionCall(currentState, args, Collections.emptyList());
        case DICT:
            return namedCall(currentState, args, Collections.emptyList());
        default:
            throw new IllegalStateException("Unknown type of call arguments: " + args);
        }
    }

    @Override
    protected LaconValue<?> methodCall(@Nonnull LaconProgramState currentState, @Nonnull LaconValue<?> args, @Nonnull LaconValue<?> ref) {
        LaconType type = args.getType();
        if (!type.isBuiltIn()) {
            throw new IllegalStateException("Unknown type of call arguments: " + args);
        }
        switch ((LaconBuiltInType) type) {
        case LIST:
            return positionCall(currentState, args, List.of(ref));
        case DICT:
            return namedCall(currentState, args, List.of(ref));
        default:
            throw new IllegalStateException("Unknown type of call arguments: " + args);
        }
    }

    @Nonnull
    protected LaconValue<?> positionCall(
        @Nonnull LaconProgramState currentState,
        @Nonnull LaconValue<?> args,
        @Nonnull List<LaconValue<?>> stackValues)
    {
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
        LaconProgramState localState = LaconProgramState.createFromOuterScope(currentState)
            .pushValues(stackValues);
        for (int i = 0; i < passingArgsNumber; i++) {
            LaconValue<?> nextPassingArg = argList.getValue().get(i);
            LaconFunctionArgument nextExpectedArgument = expectedArgs.get(i);
            LaconType expectedType = nextExpectedArgument.getType();
            if (!expectedType.accepts(nextPassingArg.getType())) {
                if (expectedType == LaconBuiltInType.STRING) {
                    nextPassingArg = LaconValueUtils.castToStr(nextPassingArg);
                } else {
                    throw new IllegalStateException("Incompatible type during function call: " + nextPassingArg.getType()
                        + ". " + nextExpectedArgument.getType() + " was expected!");
                }
            }
            localState.putVar(nextExpectedArgument.getName(), LaconVariable.initialized(nextPassingArg));
        }
        return getValue().apply(localState);
    }

    @Nonnull
    protected LaconValue<?> namedCall(
        @Nonnull LaconProgramState currentState,
        @Nonnull LaconValue<?> args,
        @Nonnull List<LaconValue<?>> stackValues)
    {
        if (!(args instanceof DictLaconValue)) {
            throw new IllegalStateException("Unexpected args value type:" +  args);
        }
        DictLaconValue argMap = (DictLaconValue) args;
        List<LaconFunctionArgument> expectedArgs = getArgs();
        int passingArgsNumber = argMap.getValue().size();
        if (passingArgsNumber != expectedArgs.size()) {
            throw new IllegalStateException("Unexpected number of passed args: " + passingArgsNumber
                + ". Expected " + expectedArgs.size() + " instead of");
        }
        LaconProgramState localState = LaconProgramState.createFromOuterScope(currentState)
            .pushValues(stackValues);
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
    public static LaconValue<?> castToStringValue(@Nonnull FunctionLaconValue function) {
        return new StringLaconValue(new StringBuilder("function(")
            .append(function.getArgs()
                .stream()
                .map(arg -> arg.getName() + ":" + arg.getType())
                .collect(Collectors.joining(",")))
            .append(":")
            .append(function.getReturnType())
            .toString());
    }

    @Nonnull
    public List<LaconFunctionArgument> getArgs() {
        return args;
    }

    @Nonnull
    public LaconType getReturnType() {
        return returnType;
    }
}

