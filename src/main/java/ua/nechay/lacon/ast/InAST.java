package ua.nechay.lacon.ast;

import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconValue;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 18.05.2023
 */
public class InAST implements AST {

    private final AST elementAST;
    private final AST collectionAST;

    public InAST(@Nonnull AST elementAST, @Nonnull AST collectionAST) {
        this.elementAST = elementAST;
        this.collectionAST = collectionAST;
    }

    @Nonnull
    @Override
    public LaconProgramState interpret(@Nonnull LaconProgramState state) {
        LaconProgramState afterElemState = elementAST.interpret(state);
        LaconValue<?> elemVal = afterElemState.popValue();

        LaconProgramState afterColState = collectionAST.interpret(state);
        LaconValue<?> colVal = afterColState.popValue();
        return afterColState.pushValue(colVal.contains(elemVal));
    }
}
