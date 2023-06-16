package ua.nechay.lacon;

import ua.nechay.lacon.core.LaconProgramState;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 20.05.2023
 */
public interface Interpreter {

    @Nonnull
    LaconProgramState interpret(@Nonnull Parser parser);
}