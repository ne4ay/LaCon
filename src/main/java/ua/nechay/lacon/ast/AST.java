package ua.nechay.lacon.ast;

import ua.nechay.lacon.core.LaconProgramState;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 05.03.2023
 */
public interface AST {

    @Nonnull
    LaconProgramState interpret(@Nonnull LaconProgramState state);

    @Nonnull
    default LaconProgramState beginInterpret() {
        return interpret(LaconProgramState.create());
    }
}