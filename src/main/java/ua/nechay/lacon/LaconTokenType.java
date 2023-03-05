package ua.nechay.lacon;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * @author anechaev
 * @since 03.03.2023
 */
public enum LaconTokenType {
    EOF(
        Objects::isNull
    ),
    SPACE(
        LaconUtils::isSpace
    ),
    PLUS(
        character -> character == '+'
    ),
    MINUS(
        character -> character == '-'
    ),
    ASSIGNMENT(
        character -> character == '='
    ),
    LEFT_BRACKET(
        character -> character == '('
    ),
    RIGHT_BRACKET(
        character -> character == ')'
    ),
    INTEGER(
        Pattern.compile("[0-9]")
    ) {
        @Override
        public LaconToken toToken(Scanner lexer) {
            StringBuilder resultBuilder = new StringBuilder();
            char character;
            while (lexer.getCurrentChar() != null && matches(character = lexer.getCurrentChar())) { //isDigit?
                resultBuilder.append(character);
                lexer.advance();
            }
            return new LaconToken(this, resultBuilder.toString());
        }
    },
    IDENTIFIER(
        Pattern.compile("[A-Za-z]")
    )
    ;

    private final Predicate<Character> matchingPredicate;

    LaconTokenType(@Nonnull Predicate<Character> matchingPredicate) {
        this.matchingPredicate = matchingPredicate;
    }

    LaconTokenType(@Nonnull Pattern pattern) {
        this.matchingPredicate = character -> pattern.matcher(String.valueOf(character)).find();
    }

    public boolean matches(char character) {
        return matchingPredicate.test(character);
    }

    public LaconToken toToken(Scanner lexer) {
        Character character = lexer.getCurrentChar();
        if (character == null) {
            return new LaconToken(EOF, null);
        }
        return toToken(lexer.getCurrentChar());
    }

    public LaconToken toToken(char character) {
        // simple implementation for single character tokens
        return new LaconToken(this, String.valueOf(character));
    }
}
