package ua.nechay.lacon;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author anechaev
 * @since 21.04.2023
 */
public enum LaconReservedWord {
    TRUE("true"),
    FALSE("false"),
    AND("and"),
    AMPERSAND_AND("&&"),
    OR("or"),
    LINE_OR("||"),
    NOT_EQUALS("!="),
    IF("if"),
    ELIF("elif"),
    ELSE("else"),
    WHILE("while"),
    LESS_OR_EQUAL("<="),
    GREATER_OR_EQUAL(">="),
    DEF("def"),
    RETURN("return"),
    FOR("for"),
    RANGE("::"),
    IN("in"),
    DOT(".")
    ;

    private static final Map<String, LaconReservedWord> ACCESS_MAP = Arrays.stream(values())
        .collect(Collectors.toMap(
            LaconReservedWord::getRepresentation,
            type -> type
        ));

    private final String representation;

    LaconReservedWord(@Nonnull String representation) {
        this.representation = representation;
    }

    @Nullable
    public static LaconReservedWord getForRepresentation(@Nonnull String representation) {
        return ACCESS_MAP.get(representation);
    }

    @Nonnull
    public String getRepresentation() {
        return representation;
    }

}
