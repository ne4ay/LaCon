package ua.nechay.lacon.ast;

import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconValue;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 17.04.2023
 */
public interface AssignableAST extends AST {

    @Nonnull
    LaconProgramState assign(@Nonnull LaconProgramState state, @Nonnull LaconValue<?> value);
}
