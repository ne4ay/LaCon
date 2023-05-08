package ua.nechay.lacon.utils;

import ua.nechay.lacon.LaconInterpreter;
import ua.nechay.lacon.LaconLexer;
import ua.nechay.lacon.LaconParser;
import ua.nechay.lacon.LaconReservedWord;
import ua.nechay.lacon.LaconToken;
import ua.nechay.lacon.LaconTokenType;
import ua.nechay.lacon.Scanner;
import ua.nechay.lacon.ast.AST;
import ua.nechay.lacon.core.LaconProgramState;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author anechaev
 * @since 05.03.2023
 */
public class LaconUtils {

    public static <U> BinaryOperator<U> DONT_USE_PARALLEL_STREAM_HERE() {
        return ($_1, $_2) -> $_2;
    }

    public static boolean isSpace(char character) {
        return character == ' ' || character == '\t';
    }

    public static boolean isNextLine(char character) {
        return character == '\n' || character == '\r';
    }

    public static String examineUntil(@Nonnull Scanner lexer, @Nonnull Predicate<Character> matches) {
        StringBuilder resultBuilder = new StringBuilder();
        int counter = 0;
        Character character;
        while ((character = lexer.peek(counter)) != null && matches.test(character)) {
            resultBuilder.append(character);
            counter++;
        }
        return resultBuilder.toString();
    }

    public static LaconToken eatToken(@Nonnull Scanner lexer, @Nonnull Predicate<Character> matches, @Nonnull LaconTokenType type) {
        StringBuilder resultBuilder = new StringBuilder();
        int position = lexer.getCurrentPosition();
        char character;
        while (lexer.getCurrentChar() != null && matches.test(character = lexer.getCurrentChar())) {
            resultBuilder.append(character);
            lexer.advance();
        }
        return new LaconToken(type, resultBuilder.toString(), position);
    }

    public static boolean matchesWord(@Nonnull Scanner scanner, @Nonnull LaconReservedWord word) {
        return matchesText(scanner, word.getRepresentation());
    }

    public static boolean matchesText(@Nonnull Scanner scanner, @Nonnull String text) {
        boolean matchesString = matchesSomeString(scanner, text);
        Character characterAfterText = scanner.peek(text.length());
        return matchesString && (characterAfterText == null || !LaconTokenType.IDENTIFIER.matches(characterAfterText));
    }

    public static boolean matchesOperator(@Nonnull Scanner scanner, @Nonnull LaconReservedWord word) {
        return matchesOperator(scanner, word.getRepresentation());
    }

    public static boolean matchesOperator(@Nonnull Scanner scanner, @Nonnull String text) {
        return matchesSomeString(scanner, text);
    }

    private static boolean matchesSomeString(@Nonnull Scanner scanner, @Nonnull String text) {
        for (int i = 0; i < text.length(); i++) {
            Character nextChar = scanner.peek(i);
            char textChar = text.charAt(i);
            if (!(nextChar != null && nextChar == textChar)) {
                return false;
            }
        }
        return true;
    }

    public static boolean matchesAnyTexts(@Nonnull Scanner scanner, @Nonnull String... words) {
        return Arrays.stream(words)
            .anyMatch(text -> matchesText(scanner, text));
    }

    public static boolean matchesAnyTexts(@Nonnull Scanner scanner, @Nonnull LaconReservedWord... words) {
        return Arrays.stream(words)
            .map(LaconReservedWord::getRepresentation)
            .anyMatch(text -> matchesText(scanner, text));
    }

    public static LaconToken eatWord(@Nonnull Scanner scanner, @Nonnull LaconReservedWord word, @Nonnull LaconTokenType type) {
        StringBuilder builder = new StringBuilder();
        String representation = word.getRepresentation();
        int position = scanner.getCurrentPosition();
        Character character;
        int i = 0;
        while ((character = scanner.getCurrentChar()) != null && i < representation.length()) {
            char sampleChar = representation.charAt(i++);
            if (character != sampleChar) {
                throw new IllegalStateException("Unable to eat word " + builder + " as a word " + representation);
            }
            builder.append(character);
            scanner.advance();
        }
        return new LaconToken(type, builder.toString(), position);
    }

    @Nonnull
    public static LaconProgramState exec(@Nonnull String text) {
        return new LaconInterpreter(new LaconParser(new LaconLexer(text))).interpret();
    }

    public static AST compose(@Nonnull AST identity, @Nonnull List<Function<AST, AST>> astHandlers) {
        return astHandlers.stream().reduce(identity,
            (nextAST, handler) -> handler.apply(nextAST),
            DONT_USE_PARALLEL_STREAM_HERE());
    }
}
