package ua.nechay.lacon;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
    MUL(
        character -> character == '*'
    ),
    DIV(
        character -> character == '/'
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
    ) {
        @Override
        public LaconToken toToken(@Nonnull Scanner lexer, @Nullable LaconToken previousToken) {
            if (previousToken != null && previousToken.getType() == RIGHT_BRACKET) {
                throw new IllegalStateException("Unable to put '(' after ')' at position: " + lexer.getCurrentPosition());
            }
            return super.toToken(lexer, previousToken);
        }
    },
    RIGHT_BRACKET(
        character -> character == ')'
    ),
    LEFT_CURLY_BRACKET(
        character -> character == '{'
    ),
    RIGHT_CURLY_BRACKET(
      character -> character == '}'
    ),
    INTEGER(
        Pattern.compile("[0-9]")
    ) {
        @Override
        public LaconToken toToken(@Nonnull Scanner lexer, @Nullable LaconToken previousToken) {
            StringBuilder resultBuilder = new StringBuilder();
            int position = lexer.getCurrentPosition();
            char character;
            while (lexer.getCurrentChar() != null && matches(character = lexer.getCurrentChar())) { //isDigit?
                resultBuilder.append(character);
                lexer.advance();
            }
            return new LaconToken(this, resultBuilder.toString(), position);
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

    public LaconToken toToken(@Nonnull Scanner lexer, @Nullable LaconToken previousToken) {
        Character character = lexer.getCurrentChar();
        if (character == null) {
            return new LaconToken(EOF, null, lexer.getCurrentPosition());
        }
        int position = lexer.getCurrentPosition();
        lexer.advance();
        return toToken(character, position);
    }

    public LaconToken toToken(char character, int currentPosition) {
        // simple implementation for single character tokens
        return new LaconToken(this, String.valueOf(character), currentPosition);
    }
}
