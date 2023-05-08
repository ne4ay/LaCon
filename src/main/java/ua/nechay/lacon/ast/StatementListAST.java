package ua.nechay.lacon.ast;

import ua.nechay.lacon.core.LaconProgramState;

import javax.annotation.Nonnull;
import java.util.List;

import static ua.nechay.lacon.utils.LaconUtils.DONT_USE_PARALLEL_STREAM_HERE;

/**
 * @author anechaev
 * @since 17.04.2023
 */
public class StatementListAST implements AST {

    private final List<AST> statements;

    public StatementListAST(@Nonnull List<AST> statements) {
        this.statements = statements;
    }

    @Nonnull
    @Override
    public LaconProgramState interpret(@Nonnull LaconProgramState state) {
        return statements.stream()
            .reduce(state,
                (nextState, statement) -> statement.interpret(nextState),
                DONT_USE_PARALLEL_STREAM_HERE());
    }
}
