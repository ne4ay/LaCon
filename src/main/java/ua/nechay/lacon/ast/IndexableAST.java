package ua.nechay.lacon.ast;

import ua.nechay.lacon.core.LaconProgramState;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 28.04.2023
 */
public class IndexableAST implements AST {

    private final AST indexable;
    private final AST index;

    public IndexableAST(@Nonnull AST indexable, @Nonnull AST index) {
        this.indexable = indexable;
        this.index = index;
    }

    @Nonnull
    @Override
    public LaconProgramState interpret(@Nonnull LaconProgramState state) {
        LaconProgramState afterIndexableState = indexable.interpret(state);
        var indexableValue = afterIndexableState.popValue();

        LaconProgramState afterIndexState = index.interpret(afterIndexableState);
        var indexValue = afterIndexState.popValue();
        return afterIndexState.pushValue(indexableValue.getByIndex(indexValue));
    }
}
