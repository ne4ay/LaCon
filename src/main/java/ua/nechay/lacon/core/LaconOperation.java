package ua.nechay.lacon.core;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 07.05.2023
 */
public enum LaconOperation {
    PLUS("+"),
    MINUS("-"),
    MUL("*"),
    DIV("/"),
    MODULUS("%"),
    NOT("!"),
    GET_BY_INDEX("'[n]'"),
    CALL("'call'")
    ;
    private final String representation;

    LaconOperation(@Nonnull String representation) {
        this.representation = representation;
    }

    public String getRepresentation() {
        return representation;
    }
}
