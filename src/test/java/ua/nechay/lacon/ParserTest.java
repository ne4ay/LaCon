package ua.nechay.lacon;

import org.junit.Test;

import java.util.List;

/**
 * @author anechaev
 * @since 04.03.2023
 */
public class ParserTest {
    private final LaconLexer lexer = new LaconLexer();

    private void parserTest(String code, LaconParser parser, String expected) {
        List<LaconToken> tokens = lexer.lex(code);

    }

    @Test
    public void testTag() {

    }
}
