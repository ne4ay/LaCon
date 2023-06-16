package ua.nechay.lacon;

import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.utils.LaconScannerState;
import ua.nechay.lacon.utils.LaconUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author anechaev
 * @since 03.03.2023
 */
public enum LaconTokenType {
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
    ) {
        @Override
        public boolean matches(@Nonnull Scanner lexer, @Nullable LaconToken previousToken) {
            return super.matches(lexer, previousToken) && !lexer.getState().isInsideQuotes();
        }
    },
    NEXT_LINE(
        LaconUtils::isNextLine, false
    ) {
        @Override
        public boolean matches(@Nonnull Scanner lexer, @Nullable LaconToken previousToken) {
            return super.matches(lexer, previousToken) && !lexer.getState().isInsideQuotes();
        }
    },
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
            StringBuilder resultBuilder = new StringBuilder();
            int position = lexer.getCurrentPosition();
            char character;
            while (lexer.getCurrentChar() != null && !QUOTE.matches(character = lexer.getCurrentChar())) {
                if (character != '\\') {
                    resultBuilder.append(character);
                    lexer.advance();
                    continue;
                }
                char slashChar = character;
                lexer.advance();
                character = lexer.getCurrentChar();
                if (QUOTE.matches(character)) {
                    resultBuilder.append(slashChar);
                    break;
                }
                switch (character) {
                case 'n':
                    resultBuilder.append('\n');
                    break;
                case '"':
                    resultBuilder.append('\"');
                    break;
                case 't':
                    resultBuilder.append('\t');
                    break;
                case 'b':
                    resultBuilder.append('\b');
                    break;
                case 'r':
                    resultBuilder.append('\r');
                    break;
                case '\\':
                    resultBuilder.append('\\');
                    break;
                default:
                    resultBuilder.append(slashChar).append(character);
                }
                lexer.advance();
            }
            return new LaconToken(this, resultBuilder.toString(), position);
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
    SEMICOLON(';'),
    COMA(','),
    MUL('*'),
    DIV('/'),
    PLUS('+'),
    MINUS('-'),
    MODULUS('%'),
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
    NOT_EQUALS(LaconReservedWord.NOT_EQUALS) {
        @Override
        public boolean matches(@Nonnull Scanner lexer, @Nullable LaconToken previousToken) {
            return LaconUtils.matchesOperator(lexer, LaconReservedWord.NOT_EQUALS);
        }
    },
    LESS_OR_EQUAL(LaconReservedWord.LESS_OR_EQUAL) {
        @Override
        public boolean matches(@Nonnull Scanner lexer, @Nullable LaconToken previousToken) {
            return LaconUtils.matchesOperator(lexer, LaconReservedWord.LESS_OR_EQUAL);
        }
    },
    GREATER_OR_EQUAL(LaconReservedWord.GREATER_OR_EQUAL)  {
        @Override
        public boolean matches(@Nonnull Scanner lexer, @Nullable LaconToken previousToken) {
            return LaconUtils.matchesOperator(lexer, LaconReservedWord.GREATER_OR_EQUAL);
        }
    },
    EXTERNAL_CALL_OPEN(LaconReservedWord.EXTERNAL_CALL_OPEN) {
        @Override
        public boolean matches(@Nonnull Scanner lexer, @Nullable LaconToken previousToken) {
            return LaconUtils.matchesOperator(lexer, LaconReservedWord.EXTERNAL_CALL_OPEN);
        }
    },
    EXTERNAL_CALL_CLOSE(LaconReservedWord.EXTERNAL_CALL_CLOSE) {
        @Override
        public boolean matches(@Nonnull Scanner lexer, @Nullable LaconToken previousToken) {
            return LaconUtils.matchesOperator(lexer, LaconReservedWord.EXTERNAL_CALL_CLOSE);
        }
    },
    NOT('!'),
    LESS('<'),
    GREATER('>'),
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
    LEFT_BRACKET('('),
    RIGHT_BRACKET(')'),
    LEFT_SQUARE_BRACKET('['),
    RIGHT_SQUARE_BRACKET(']'),
    LEFT_CURLY_BRACKET('{'),
    RIGHT_CURLY_BRACKET('}'),
    IF(LaconReservedWord.IF),
    ELIF(LaconReservedWord.ELIF),
    ELSE(LaconReservedWord.ELSE),
    WHILE(LaconReservedWord.WHILE),
    DEF(LaconReservedWord.DEF),
    RETURN(LaconReservedWord.RETURN),
    FOR(LaconReservedWord.FOR),
    IN(LaconReservedWord.IN),
    RANGE(LaconReservedWord.RANGE) {
        @Override
        public boolean matches(@Nonnull Scanner lexer, @Nullable LaconToken previousToken) {
            return LaconUtils.matchesOperator(lexer, LaconReservedWord.RANGE);
        }
    },
    COLON(':'),
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
        Pattern.compile("[0-9]")
    ) {
        private final Set<Character> appropriateFirstSymbols = Set.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
        private final Set<Character> appropriateNSymbols = Set.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '_');

        @Override
        public boolean matches(char character) {
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
                return nChar != '.';
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
        public boolean matches(char character) {
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
                return doublePattern.matcher(builder.toString()).matches();
            }
            return doublePattern.matcher(builder.toString()).matches();
        }

        @Override
        public LaconToken toToken(@Nonnull Scanner lexer, @Nullable LaconToken previousToken) {
            return LaconUtils.eatToken(lexer, this::matches, this);
        }
    },
    DOT(LaconReservedWord.DOT) {
        @Override
        public boolean matches(@Nonnull Scanner lexer, @Nullable LaconToken previousToken) {
            return LaconUtils.matchesOperator(lexer, LaconReservedWord.DOT);
        }
    },
    CAST(
        Pattern.compile("[a-z(]")
    ) {
        private final Pattern pattern = Pattern.compile(Arrays.stream(LaconBuiltInType.values())
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
        private final Pattern pattern = Pattern.compile(Arrays.stream(LaconBuiltInType.values())
            .map(LaconBuiltInType::getRepresentation)
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
        Pattern.compile("[A-Za-z]")
    ) {
        private final Pattern nCharPattern = Pattern.compile("[_A-Za-z0-9]+");
        private final Pattern pattern = Pattern.compile("(?=.*[A-Za-z])_*[_A-Za-z0-9]+");

        @Override
        public boolean matches(@Nonnull Scanner lexer) {
            Character firstChar = lexer.getCurrentChar();
            if (firstChar == null || !matches(firstChar)) {
                return false;
            }
            return pattern.matcher(LaconUtils.examineUntil(lexer, this::matchesToMainIdPattern)).matches();
        }

        @Override
        public LaconToken toToken(@Nonnull Scanner lexer, @Nullable LaconToken previousToken) {
            return LaconUtils.eatToken(lexer, this::matchesToMainIdPattern, this);
        }

        private boolean matchesToMainIdPattern(char character) {
            return nCharPattern.matcher(String.valueOf(character)).matches();
        }
    }
    ;
    private static final EnumSet<LaconTokenType> OPERATORS = EnumSet.of(MUL, DIV, PLUS, MINUS);

    protected final MatchPredicate matchPredicate;
    protected final TokenEater tokenEater;
    protected final Predicate<Character> charMatchingPredicate;
    protected final boolean isStandardToken;

    LaconTokenType(char expectedCharacter) {
        this(character -> character == expectedCharacter);
    }

    LaconTokenType(@Nonnull Predicate<Character> charMatchingPredicate) {
        this(charMatchingPredicate, true);
    }

    LaconTokenType(@Nonnull Pattern pattern) {
        this(pattern, true);
    }

    LaconTokenType(@Nonnull Predicate<Character> charMatchingPredicate, boolean isStandardToken) {
        this.charMatchingPredicate = charMatchingPredicate;
        this.matchPredicate = (lexer, ignored) -> matches(lexer);
        this.tokenEater = this::defaultEatToken;
        this.isStandardToken = isStandardToken;
    }

    LaconTokenType(@Nonnull Pattern pattern, boolean isStandardToken) {
        this.charMatchingPredicate = character -> pattern.matcher(String.valueOf(character)).matches();
        this.matchPredicate = (lexer, ignored) -> matches(lexer);
        this.tokenEater = this::defaultEatToken;
        this.isStandardToken = isStandardToken;
    }

    LaconTokenType(@Nonnull LaconReservedWord reservedWord) {
        this.charMatchingPredicate = character -> reservedWord.getRepresentation().charAt(0) == character;
        this.matchPredicate = (lexer, prevToken) -> LaconUtils.matchesWord(lexer, reservedWord);
        this.tokenEater = (lexer, prevToken) -> LaconUtils.eatWord(lexer, reservedWord, this);
        this.isStandardToken = true;
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
        return matchPredicate.test(lexer, previousToken);
    }

    public boolean matches(char character) {
        return charMatchingPredicate.test(character);
    }

    public LaconToken toToken(@Nonnull Scanner lexer, @Nullable LaconToken previousToken) {
        return tokenEater.apply(lexer, previousToken);
    }

    private LaconToken defaultEatToken(@Nonnull Scanner lexer, @Nullable LaconToken previousToken) {
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

    protected interface MatchPredicate extends BiPredicate<Scanner, LaconToken> {}
    protected interface TokenEater extends BiFunction<Scanner, LaconToken, LaconToken> {}
}
