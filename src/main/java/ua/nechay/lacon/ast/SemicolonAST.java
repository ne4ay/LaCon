package ua.nechay.lacon.ast;

import ua.nechay.lacon.core.LaconProgramState;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 19.04.2023
 */
public class SemicolonAST implements AST {
    @Nonnull
    @Override
    public LaconProgramState interpret(@Nonnull LaconProgramState state) {
        return state.clearValueStack();
    }
}
