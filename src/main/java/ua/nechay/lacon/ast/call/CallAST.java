package ua.nechay.lacon.ast.call;

import ua.nechay.lacon.ast.AST;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconValue;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 04.05.2023
 */
public class CallAST implements AST {
    private final AST callable;
    private final AST callArgs;

    public CallAST(@Nonnull AST callable, @Nonnull AST callArgs) {
        this.callable = callable;
        this.callArgs = callArgs;
    }

    @Nonnull
    @Override
    public LaconProgramState interpret(@Nonnull LaconProgramState state) {
        LaconProgramState afterCallableState = callable.interpret(state);
        LaconValue<?> callableValue = afterCallableState.popValue();

        LaconProgramState afterArgsState = callArgs.interpret(afterCallableState);
        LaconValue<?> argsValue = afterArgsState.popValue();

        LaconValue<?> callableResult = callableValue.call(state, argsValue);
        return state.pushValue(callableResult);
    }
}
