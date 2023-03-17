package ua.nechay.lacon;

import ua.nechay.lacon.ast.AST;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 08.03.2023
 */
public class LaconInterpreter {

    private final Parser parser;

    public LaconInterpreter(@Nonnull Parser parser) {
        this.parser = parser;
    }

    int visit(@Nonnull AST ast) {
        throw new IllegalStateException("Illegal!");
    }

    int interpret() {
        return parser.parse().interpret();
    }
}
