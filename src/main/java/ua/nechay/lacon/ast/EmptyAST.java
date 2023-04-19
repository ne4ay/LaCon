package ua.nechay.lacon.ast;

import ua.nechay.lacon.core.LaconProgramState;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 18.04.2023
 */
public class EmptyAST implements AST {

    @Nonnull @Override
    public LaconProgramState interpret(@Nonnull LaconProgramState state) {
        return state;
    }
}
