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
    SEMICOLON(
        character -> character == ';'
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
    EQUALS(
        character -> character == '='
    ) {
        @Override
        public boolean matches(@Nonnull Scanner lexer) {
            Character nextChar = lexer.peek(1);
            if (nextChar == null) {
                throw new IllegalStateException("Unable to assign to nothing!");
            }
            Character afterNextChar = lexer.peek(2);
            if (afterNextChar == null || afterNextChar.equals('=')) {
                throw new IllegalStateException("Illegal character: " + afterNextChar + " at " + lexer.getCurrentPosition() + 2);
            }
            return super.matches(lexer) && nextChar.equals('=');
        }

        @Override
        public LaconToken toToken(@Nonnull Scanner lexer, @Nullable LaconToken previousToken) {
            StringBuilder resultBuilder = new StringBuilder();
            int position = lexer.getCurrentPosition();
            Character character1 = lexer.getCurrentChar();
            if (character1 == null) {
                throw new NullPointerException("Unable to construct Equals!");
            }
            resultBuilder.append(character1);
            lexer.advance();
            Character character2 = lexer.getCurrentChar();
            if (character2 == null || !character2.equals('=')) {
                throw new IllegalStateException("Illegal character: " + character2 + " at " + lexer.getCurrentPosition());
            }
            resultBuilder.append(character2);
            lexer.advance();
            return new LaconToken(this, resultBuilder.toString(), position);
        }
    },
    ASSIGNMENT(
        character -> character == '='
    ) {
        @Override
        public boolean matches(@Nonnull Scanner lexer) {
            Character nextChar = lexer.peek(1);
            if (nextChar == null) {
                throw new IllegalStateException("Unable to assign to nothing!");
            }
            return super.matches(lexer) && !nextChar.equals('=');
        }
    },
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
        Pattern.compile("[1-9][0-9]")
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
        Pattern.compile("[A-Za-z][A-Za-z0-9_$]")
    )
    ;

    private final Predicate<Character> matchingPredicate;

    LaconTokenType(@Nonnull Predicate<Character> matchingPredicate) {
        this.matchingPredicate = matchingPredicate;
    }

    LaconTokenType(@Nonnull Pattern pattern) {
        this.matchingPredicate = character -> pattern.matcher(String.valueOf(character)).find();
    }

    public boolean matches(@Nonnull Scanner lexer) {
        Character currentCharacter = lexer.getCurrentChar();
        if (currentCharacter == null) {
            return false;
        }
        return matches(currentCharacter);
    }

    protected boolean matches(char character) {
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

    private LaconToken toToken(char character, int currentPosition) {
        // simple implementation for single character tokens
        return new LaconToken(this, String.valueOf(character), currentPosition);
    }
}
