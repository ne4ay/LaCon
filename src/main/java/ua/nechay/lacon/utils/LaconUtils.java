package ua.nechay.lacon.utils;

import ua.nechay.lacon.LaconInterpreter;
import ua.nechay.lacon.LaconLexer;
import ua.nechay.lacon.LaconParser;
import ua.nechay.lacon.LaconToken;
import ua.nechay.lacon.LaconTokenType;
import ua.nechay.lacon.Scanner;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconType;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

/**
 * @author anechaev
 * @since 05.03.2023
 */
public class LaconUtils {

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

    public static LaconProgramState exec(@Nonnull String text) {
        return new LaconInterpreter(new LaconParser(new LaconLexer(text))).interpret();
    }
}
