package ua.nechay.lacon.ast;

import ua.nechay.lacon.core.LaconProgramState;

/**
 * @author anechaev
 * @since 05.03.2023
 */
public interface AST {

    LaconProgramState interpret();
}
