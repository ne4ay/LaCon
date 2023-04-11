package ua.nechay.lacon;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * @author anechaev
 * @since 03.03.2023
 */
public enum LaconTokenType {
    //TODO change from predicate to Char
    EOF(
        Objects::isNull
    ),
    COMMENT(
        character -> character == '/'
    ) {
        @Override
        public boolean matches(@Nonnull Scanner lexer) {
            Character nextChar = lexer.peek(1);
            if (nextChar == null) {
                throw new IllegalStateException("Unable to assign to nothing!");
            }
            return super.matches(lexer) && nextChar.equals('/');
        }

        @Override
        public LaconToken toToken(@Nonnull Scanner lexer, @Nullable LaconToken previousToken) {
            StringBuilder resultBuilder = new StringBuilder();
            int position = lexer.getCurrentPosition();
            char character;
            while (lexer.getCurrentChar() != null && (character = lexer.getCurrentChar()) != '\n') {
                resultBuilder.append(character);
                lexer.advance();
            }
            return new LaconToken(this, resultBuilder.toString(), position);
        }
    },
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
        Pattern.compile("[1-9]")
    ) {
        private final Set<Character> appropriateFirstSymbols = Set.of('1', '2', '3', '4', '5', '6', '7', '8', '9');
        private final Set<Character> appropriateNSymbols = Set.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '_');

        @Override
        protected boolean matches(char character) {
            return appropriateNSymbols.contains(character);
        }

        @Override
        public boolean matches(@Nonnull Scanner lexer) {
            Character character1 = lexer.getCurrentChar();
            if (character1 == null) {
                throw new NullPointerException("Illegal!!!");
            }
            if (!appropriateFirstSymbols.contains(character1)) {
                return false;
            }
            int shift = 1;
            Character nChar;
            while ((nChar = lexer.peek(shift)) != null) {
                if (appropriateNSymbols.contains(nChar)) {
                    shift++;
                    continue;
                }
                Character nPlus1Char = lexer.peek(shift + 1);
                return nPlus1Char != null &&
                    (SEMICOLON.matches(nPlus1Char) || SPACE.matches(nPlus1Char)
                        || LaconTokenType.isOperator(nPlus1Char) || RIGHT_BRACKET.matches(nPlus1Char));
            }
            throw new IllegalStateException("Unable to parse!!");
        }

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
    ) ////TODO: override matches method
    ;
    private static final EnumSet<LaconTokenType> OPERATORS = EnumSet.of(MUL, DIV, PLUS, MINUS);

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

    private static boolean isOperator(@Nullable Character character) {
        if (character == null) {
            return false;
        }
        return OPERATORS.stream()
            .anyMatch(operatorType -> operatorType.matches(character));
    }
}
