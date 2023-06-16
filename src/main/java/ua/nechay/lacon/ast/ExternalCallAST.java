package ua.nechay.lacon.ast;


import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.function.FunctionLaconValue;
import ua.nechay.lacon.core.function.built.external.LaconBuiltInExternalCall;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 22.05.2023
 */
public class ExternalCallAST implements AST {

    private final AST expression;

    public ExternalCallAST(@Nonnull AST expression) {
        this.expression = expression;
    }

    @Nonnull
    @Override
    public LaconProgramState interpret(@Nonnull LaconProgramState state) {
        LaconProgramState afterExpressionState = expression.interpret(state);
        FunctionLaconValue externalCall = afterExpressionState.getExternalCallFunction();
        LaconValue<?> value = afterExpressionState.popValue();
        LaconValue<?> callResult = externalCall.call(afterExpressionState, LaconBuiltInExternalCall.adjustArg(value));
        return afterExpressionState.pushValue(callResult);
    }
}