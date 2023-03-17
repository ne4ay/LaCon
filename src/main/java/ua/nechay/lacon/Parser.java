package ua.nechay.lacon;


import ua.nechay.lacon.ast.AST;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 08.03.2023
 */
public interface Parser {

    @Nonnull
    AST parse();
}
