package ua.nechay.lacon.ast;

import ua.nechay.lacon.LaconToken;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.var.LaconVariable;

import javax.annotation.Nonnull;
import java.util.Iterator;

/**
 * @author anechaev
 * @since 14.05.2023
 */
public class ForCycleAST implements AST {

    private final LaconToken identifier;
    private final AST iterable;
    private final AST forBlock;

    public ForCycleAST(LaconToken identifier, AST iterable, AST forBlock) {
        this.identifier = identifier;
        this.iterable = iterable;
        this.forBlock = forBlock;
    }

    @Nonnull
    @Override
    public LaconProgramState interpret(@Nonnull LaconProgramState state) {
        String id = identifier.getText();
        AST iterableAST = iterable;
        if (iterable instanceof RangeAST) {
            iterableAST = ((RangeAST)iterable).asLazy();
        }
        LaconProgramState afterIterableState = iterableAST.interpret(state);
        LaconValue<?> iterableValue = afterIterableState.popValue();
        Iterator<LaconValue<?>> iterator = iterableValue.iterator();

        LaconProgramState iterState = afterIterableState;
        while (iterator.hasNext()) {
            LaconValue<?> nextValue = iterator.next();
            iterState = iterState.putVar(id, LaconVariable.initialized(nextValue));
            iterState = forBlock.interpret(iterState);
        }
        iterState.removeVar(id);
        return iterState;
    }
}
