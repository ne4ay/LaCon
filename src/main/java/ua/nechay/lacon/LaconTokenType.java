package ua.nechay;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author anechaev
 * @since 03.03.2023
 */
public enum LaconTokenType {
    SPACE(
        Pattern.compile("[ \t\n]"), false
    ),
    EOF(
        Pattern.compile("[^\n]*"), false
    ),
    ASSIGNMENT(
        Pattern.compile("="), true
    ),
    LEFT_BRACKET(
        Pattern.compile("\\("),true
    ),
    RIGHT_BRACKET(
        Pattern.compile("\\)"), true
    ),
    NUMBER(
        Pattern.compile("[0-9]+"), true
    ),
    IDENTIFIER(
        Pattern.compile("[A-Za-z][A-Za-z0-9_]*"), true
    )
    ;

    private final Pattern pattern;
    private final boolean handle;

    LaconTokenType(Pattern pattern, boolean handle) {
        this.pattern = pattern;
        this.handle = handle;
    }

    public Matcher match(CharSequence characters) {
        return pattern.matcher(characters);
    }

    public boolean isHandle() {
        return handle;
    }
}
