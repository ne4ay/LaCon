package ua.nechay.lacon.lexer;

import org.junit.Test;
import ua.nechay.lacon.LaconLexer;
import ua.nechay.lacon.LaconToken;
import ua.nechay.lacon.LaconTokenType;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author anechaev
 * @since 20.04.2023
 */
public class StringLexerTest extends AbstractLexerTestBase {

    @Test
    public void testEmptyString() {
        LaconLexer lexer = new LaconLexer("\"\"");
        List<LaconToken> tokens = getTokens(lexer);
        assertThat(tokens.size(), equalTo(3));

        LaconToken token0 = tokens.get(0);
        assertThat(token0.getType(), equalTo(LaconTokenType.QUOTE));
        assertThat(token0.getText(), equalTo("\""));

        LaconToken token1 = tokens.get(1);
        assertThat(token1.getType(), equalTo(LaconTokenType.STRING));
        assertThat(token1.getText(), equalTo(""));

        LaconToken token2 = tokens.get(2);
        assertThat(token2.getType(), equalTo(LaconTokenType.QUOTE));
        assertThat(token2.getText(), equalTo("\""));
    }

    @Test
    public void testStringWithOneLetter() {
        LaconLexer lexer = new LaconLexer("\"a\"");
        List<LaconToken> tokens = getTokens(lexer);
        assertThat(tokens.size(), equalTo(3));

        LaconToken token0 = tokens.get(0);
        assertThat(token0.getType(), equalTo(LaconTokenType.QUOTE));
        assertThat(token0.getText(), equalTo("\""));

        LaconToken token1 = tokens.get(1);
        assertThat(token1.getType(), equalTo(LaconTokenType.STRING));
        assertThat(token1.getText(), equalTo("a"));

        LaconToken token2 = tokens.get(2);
        assertThat(token2.getType(), equalTo(LaconTokenType.QUOTE));
        assertThat(token2.getText(), equalTo("\""));
    }

    @Test
    public void testSeveralStrings() {
        LaconLexer lexer = new LaconLexer("\"a\" + \"b\"");
        List<LaconToken> tokens = getTokens(lexer);
        assertThat(tokens.size(), equalTo(7));

        LaconToken token0 = tokens.get(0);
        assertThat(token0.getType(), equalTo(LaconTokenType.QUOTE));
        assertThat(token0.getText(), equalTo("\""));

        LaconToken token1 = tokens.get(1);
        assertThat(token1.getType(), equalTo(LaconTokenType.STRING));
        assertThat(token1.getText(), equalTo("a"));

        LaconToken token2 = tokens.get(2);
        assertThat(token2.getType(), equalTo(LaconTokenType.QUOTE));
        assertThat(token2.getText(), equalTo("\""));

        LaconToken token3 = tokens.get(3);
        assertThat(token3.getType(), equalTo(LaconTokenType.PLUS));
        assertThat(token3.getText(), equalTo("+"));

        LaconToken token4 = tokens.get(4);
        assertThat(token4.getType(), equalTo(LaconTokenType.QUOTE));
        assertThat(token4.getText(), equalTo("\""));

        LaconToken token5 = tokens.get(5);
        assertThat(token5.getType(), equalTo(LaconTokenType.STRING));
        assertThat(token5.getText(), equalTo("b"));

        LaconToken token6 = tokens.get(6);
        assertThat(token6.getType(), equalTo(LaconTokenType.QUOTE));
        assertThat(token6.getText(), equalTo("\""));
    }
}
