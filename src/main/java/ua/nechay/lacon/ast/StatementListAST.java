package ua.nechay.lacon.ast;

import ua.nechay.lacon.core.LaconProgramState;

import javax.annotation.Nonnull;
import java.util.List;

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
                ($_1, $_2) -> $_2 /*isn't used -> dedicated for async*/);
    }
}
