package ua.nechay.lacon;

import ua.nechay.lacon.core.LaconType;
import ua.nechay.lacon.utils.LaconScannerState;
import ua.nechay.lacon.utils.LaconUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author anechaev
 * @since 03.03.2023
 */
public enum LaconTokenType {
    //TODO change from predicate to Char
    EOF(
        Objects::isNull, true
    ),
    COMMENT(
        character -> character == '/' , true
    ) {
        @Override
        public boolean matches(@Nonnull Scanner lexer) {
            Character nextChar = lexer.peek(1);
            if (nextChar == null) {
                return false;
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
        LaconUtils::isSpace, false
    ),
    NEXT_LINE(
        LaconUtils::isNextLine, false
    ),
    STRING(Pattern.compile(".")) {
        @Override
        public boolean matches(@Nonnull Scanner lexer, @Nullable LaconToken previousToken) {
            return previousToken != null && previousToken.getType() == QUOTE && lexer.getState().isInsideQuotes();
        }

        @Override
        public LaconToken toToken(@Nonnull Scanner lexer, @Nullable LaconToken previousToken) {
            // TODO: add a state for lexer
            Character currentChar = lexer.getCurrentChar();
            if (currentChar == null) {
                throw new NullPointerException("Illegal!!");
            }
            if (QUOTE.matches(currentChar)) {
                int position = lexer.getCurrentPosition();
                return new LaconToken(this, "", position);
            }

            return LaconUtils.eatToken(lexer, character -> !QUOTE.matches(character), this);
        }
    },
    // String should be higher than quote to give a possibility of creating empty strings
    QUOTE('"') {
        @Override
        public LaconToken toToken(@Nonnull Scanner lexer, @Nullable LaconToken previousToken) {
            LaconToken quoteToken = super.toToken(lexer, previousToken);
            lexer.getState().acceptNextQuote(quoteToken);
            return quoteToken;
        }
    },
    COLON(':'),
    SEMICOLON(';'),
    MUL('*'),
    DIV('/'),
    PLUS('+'),
    MINUS('-'),
    NOT('!'),
    AND(
        character -> character == '&' || character == 'a'
    ) {
        @Override
        public boolean matches(@Nonnull Scanner lexer, @Nullable LaconToken previousToken) {
            return LaconUtils.matchesAnyTexts(lexer,
                LaconReservedWord.AND, LaconReservedWord.AMPERSAND_AND);
        }

        @Override
        public LaconToken toToken(@Nonnull Scanner lexer, @Nullable LaconToken previousToken) {
            Character character = lexer.getCurrentChar();
            if (character == null) {
                throw new NullPointerException("Unexpected!");
            }
            switch (character) {
            case '&':
                return LaconUtils.eatWord(lexer, LaconReservedWord.AMPERSAND_AND, this);
            case 'a':
                return LaconUtils.eatWord(lexer, LaconReservedWord.AND, this);
            default:
                throw new IllegalStateException("Unexpected character: " + character + ". 'true' or 'false' expected");
            }
        }
    },
    OR(
        character -> character == '|' || character == 'o'
    ) {
        @Override
        public boolean matches(@Nonnull Scanner lexer, @Nullable LaconToken previousToken) {
            return LaconUtils.matchesAnyTexts(lexer,
                LaconReservedWord.OR, LaconReservedWord.LINE_OR);
        }

        @Override
        public LaconToken toToken(@Nonnull Scanner lexer, @Nullable LaconToken previousToken) {
            Character character = lexer.getCurrentChar();
            if (character == null) {
                throw new NullPointerException("Unexpected!");
            }
            switch (character) {
            case '|':
                return LaconUtils.eatWord(lexer, LaconReservedWord.LINE_OR, this);
            case 'o':
                return LaconUtils.eatWord(lexer, LaconReservedWord.OR, this);
            default:
                throw new IllegalStateException("Unexpected character: " + character + ". 'true' or 'false' expected");
            }
        }
    },
    EQUALS('=') {
        @Override
        public boolean matches(@Nonnull Scanner lexer) {
            Character nextChar = lexer.peek(1);
            if (nextChar == null || !nextChar.equals('=')) {
                return false;
            }
            Character afterNextChar = lexer.peek(2);
            if (afterNextChar == null || afterNextChar.equals('=')) {
                throw new IllegalStateException("Illegal character: " + afterNextChar + " at " + lexer.getCurrentPosition() + 2);
            }
            return super.matches(lexer);
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
    NOT_EQUALS('!') {
        @Override
        public boolean matches(@Nonnull Scanner lexer, @Nullable LaconToken previousToken) {
            return LaconUtils.matchesText(lexer, LaconReservedWord.NOT_EQUALS.getRepresentation());
        }

        @Override
        public LaconToken toToken(@Nonnull Scanner lexer, @Nullable LaconToken previousToken) {
            return LaconUtils.eatWord(lexer, LaconReservedWord.NOT_EQUALS, this);
        }
    },
    ASSIGNMENT(
        character -> character == '='
    ) {
        @Override
        public boolean matches(@Nonnull Scanner lexer) {
            Character nextChar = lexer.peek(1);
            if (nextChar == null) {
                return false;
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
    RIGHT_BRACKET(')'),
    LEFT_CURLY_BRACKET('{'),
    RIGHT_CURLY_BRACKET('}'),
    BOOLEAN(
        character -> character == 't' || character == 'f'
    ) {
        @Override
        public boolean matches(@Nonnull Scanner lexer, @Nullable LaconToken previousToken) {
            return LaconUtils.matchesAnyTexts(lexer,
                LaconReservedWord.TRUE, LaconReservedWord.FALSE);
        }

        @Override
        public LaconToken toToken(@Nonnull Scanner lexer, @Nullable LaconToken previousToken) {
            Character character = lexer.getCurrentChar();
            if (character == null) {
                throw new NullPointerException("Unexpected!");
            }
            switch (character) {
            case 't':
                return LaconUtils.eatWord(lexer, LaconReservedWord.TRUE, this);
            case 'f':
                return LaconUtils.eatWord(lexer, LaconReservedWord.FALSE, this);
            default:
                throw new IllegalStateException("Unexpected character: " + character + ". 'true' or 'false' expected");
            }
        }
    },
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
                return SEMICOLON.matches(nChar)
                    || SPACE.matches(nChar)
                    || NEXT_LINE.matches(nChar)
                    || LaconTokenType.isOperator(nChar)
                    || RIGHT_BRACKET.matches(nChar);
            }
            return true;
        }

        @Override
        public LaconToken toToken(@Nonnull Scanner lexer, @Nullable LaconToken previousToken) {
            return LaconUtils.eatToken(lexer, this::matches, this);
        }
    },
    REAL(
        Pattern.compile("[0-9.]")
    ) {
        private final Set<Character> appropriateFirstSymbols = Set.of('.', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
        private final Set<Character> appropriateNSymbols = Set.of('.', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '_');
        private final Pattern doublePattern = Pattern.compile("(\\d+[\\d_]*\\d+\\.\\d+[\\d_]*\\d+)|(\\d+[\\d_]*\\d+\\.\\d*)|(\\d*\\.\\d+[\\d_]*\\d+)|(\\d+\\.\\d+)");

        @Override
        protected boolean matches(char character) {
            return appropriateNSymbols.contains(character);
        }

        @Override
        public boolean matches(@Nonnull Scanner lexer) {
            StringBuilder builder = new StringBuilder();
            Character character1 = lexer.getCurrentChar();
            if (character1 == null) {
                throw new NullPointerException("Illegal!!!");
            }
            builder.append(character1);
            if (!appropriateFirstSymbols.contains(character1)) {
                return false;
            }
            int shift = 1;
            Character nChar;
            while ((nChar = lexer.peek(shift)) != null) {
                if (appropriateNSymbols.contains(nChar)) {
                    builder.append(nChar);
                    shift++;
                    continue;
                }
                if (SEMICOLON.matches(nChar)
                    || SPACE.matches(nChar)
                    || NEXT_LINE.matches(nChar)
                    || LaconTokenType.isOperator(nChar)
                    || RIGHT_BRACKET.matches(nChar))
                {
                    return doublePattern.matcher(builder.toString()).matches();
                }
            }
            return doublePattern.matcher(builder.toString()).matches();
        }

        @Override
        public LaconToken toToken(@Nonnull Scanner lexer, @Nullable LaconToken previousToken) {
            return LaconUtils.eatToken(lexer, this::matches, this);
        }
    },
    CAST(
        Pattern.compile("[a-z(]")
    ) {
        private final Pattern pattern = Pattern.compile(Arrays.stream(LaconType.values())
            .map(type -> type.getRepresentation() + "\\s*\\(")
            .collect(Collectors.joining("|")));

        @Override
        public boolean matches(@Nonnull Scanner lexer) {
            LaconScannerState tokenScanState = LaconScannerState.create();
            StringBuilder resultBuilder = new StringBuilder();
            int counter = 0;
            Character character;
            while ((character = lexer.peek(counter)) != null && matches(character)) {
                if (LEFT_BRACKET.matches(character)) {
                    tokenScanState.pushBrace(new LaconToken(LEFT_BRACKET, String.valueOf(character), -1));
                } else if (tokenScanState.isInsideBraces()) {
                    break;
                }
                resultBuilder.append(character);
                counter++;
            }
            return pattern.matcher(resultBuilder.toString()).matches();
        }

        @Override
        public LaconToken toToken(@Nonnull Scanner lexer, @Nullable LaconToken previousToken) {
            return LaconUtils.eatToken(lexer, TYPE::matches, this);
        }
    },
    TYPE(
        Pattern.compile("[a-z]")
    ) {
        private final Pattern pattern = Pattern.compile(Arrays.stream(LaconType.values())
            .map(LaconType::getRepresentation)
            .collect(Collectors.joining("|")));

        @Override
        public boolean matches(@Nonnull Scanner lexer) {
            return pattern.matcher(LaconUtils.examineUntil(lexer, this::matches)).matches();
        }

        @Override
        public LaconToken toToken(@Nonnull Scanner lexer, @Nullable LaconToken previousToken) {
            return LaconUtils.eatToken(lexer, this::matches, this);
        }
    },
    IDENTIFIER(
        Pattern.compile("[_A-Za-z]")
    ) {
        private final Pattern pattern = Pattern.compile("(?=.*[A-Za-z])_*[_A-Za-z0-9]+");

        @Override
        public boolean matches(@Nonnull Scanner lexer) {
            return pattern.matcher(LaconUtils.examineUntil(lexer, this::matches)).matches();
        }

        @Override
        public LaconToken toToken(@Nonnull Scanner lexer, @Nullable LaconToken previousToken) {
            return LaconUtils.eatToken(lexer, this::matches, this);
        }
    }
    ;
    private static final EnumSet<LaconTokenType> OPERATORS = EnumSet.of(MUL, DIV, PLUS, MINUS);

    protected final Predicate<Character> matchingPredicate;
    protected final boolean isStandardToken;

    LaconTokenType(char expectedCharacter) {
        this(character -> character == expectedCharacter);
    }

    LaconTokenType(@Nonnull Predicate<Character> matchingPredicate) {
        this.matchingPredicate = matchingPredicate;
        this.isStandardToken = true;
    }

    LaconTokenType(@Nonnull Pattern pattern) {
        this.matchingPredicate = character -> pattern.matcher(String.valueOf(character)).matches();
        this.isStandardToken = true;
    }

    LaconTokenType(@Nonnull Predicate<Character> matchingPredicate, boolean isStandardToken) {
        this.matchingPredicate = matchingPredicate;
        this.isStandardToken = isStandardToken;
    }

    LaconTokenType(@Nonnull Pattern pattern, boolean isStandardToken) {
        this.matchingPredicate = character -> pattern.matcher(String.valueOf(character)).matches();
        this.isStandardToken = isStandardToken;
    }

    /**
     * @return not-skippable types
     */
    public static List<LaconTokenType> getStandardTypes() {
        return Arrays.asList(values());
    }

    public boolean matches(@Nonnull Scanner lexer) {
        Character currentCharacter = lexer.getCurrentChar();
        if (currentCharacter == null) {
            return false;
        }
        return matches(currentCharacter);
    }

    public boolean matches(@Nonnull Scanner lexer, @Nullable LaconToken previousToken) {
        return matches(lexer);
    }

    protected boolean matches(char character) {
        return matchingPredicate.test(character);
    }

    public LaconToken toToken(@Nonnull Scanner lexer, @Nullable LaconToken previousToken) {
        Character character = lexer.getCurrentChar();
        if (character == null) {
            return new LaconToken(EOF, "", lexer.getCurrentPosition());
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
