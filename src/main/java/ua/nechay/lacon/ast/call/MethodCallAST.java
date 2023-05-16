package ua.nechay.lacon.ast.call;

import ua.nechay.lacon.LaconToken;
import ua.nechay.lacon.ast.AST;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconValue;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 16.05.2023
 */
public class MethodCallAST implements AST {

    private final AST callableAST;
    private final LaconToken methodIdentifier;
    private final AST callArgs;

    public MethodCallAST(@Nonnull AST callableAST, @Nonnull LaconToken methodIdentifier, @Nonnull AST callArgs) {
        this.callableAST = callableAST;
        this.methodIdentifier = methodIdentifier;
        this.callArgs = callArgs;
    }

    @Nonnull
    @Override
    public LaconProgramState interpret(@Nonnull LaconProgramState state) {
        LaconProgramState afterCallableState = callableAST.interpret(state);
        LaconValue<?> callableValue = afterCallableState.popValue();

        LaconProgramState afterArgState = callArgs.interpret(afterCallableState);
        LaconValue<?> argsValue = afterArgState.popValue();

        LaconValue<?> result = callableValue.callMethod(methodIdentifier.getText(), afterArgState, argsValue);
        return afterArgState.pushValue(result);
    }
}
