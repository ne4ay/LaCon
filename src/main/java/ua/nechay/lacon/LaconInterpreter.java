package ua.nechay.lacon;

import ua.nechay.lacon.core.LaconProgramState;

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

    public LaconProgramState interpret() {
        return parser.parse().beginInterpret();
    }
}
