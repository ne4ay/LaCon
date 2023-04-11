package ua.nechay.lacon.lexer;

import org.junit.Test;
import ua.nechay.lacon.LaconLexer;
import ua.nechay.lacon.LaconToken;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

/**
 * @author anechaev
 * @since 16.03.2023
 */
public class LexerTest extends AbstractLexerTestBase {

    @Test
    public void testSomeExpression() {
        LaconLexer lexer = new LaconLexer("7 + 3 * (10 / (12 / (3 + 1) - 1))");
        List<LaconToken> tokens = getTokens(lexer);
        assertThat(tokens.size(), equalTo(19));
        assertThat(tokens.get(0).getText(), equalTo("7"));
        assertThat(tokens.get(1).getText(), equalTo("+"));
        assertThat(tokens.get(2).getText(), equalTo("3"));
        assertThat(tokens.get(3).getText(), equalTo("*"));
        assertThat(tokens.get(4).getText(), equalTo("("));
        assertThat(tokens.get(5).getText(), equalTo("10"));
        assertThat(tokens.get(6).getText(), equalTo("/"));
        assertThat(tokens.get(7).getText(), equalTo("("));
        assertThat(tokens.get(8).getText(), equalTo("12"));
        assertThat(tokens.get(9).getText(), equalTo("/"));
        assertThat(tokens.get(10).getText(), equalTo("("));
        assertThat(tokens.get(11).getText(), equalTo("3"));
        assertThat(tokens.get(12).getText(), equalTo("+"));
        assertThat(tokens.get(13).getText(), equalTo("1"));
        assertThat(tokens.get(14).getText(), equalTo(")"));
        assertThat(tokens.get(15).getText(), equalTo("-"));
        assertThat(tokens.get(16).getText(), equalTo("1"));
        assertThat(tokens.get(17).getText(), equalTo(")"));
        assertThat(tokens.get(18).getText(), equalTo(")"));
    }

    @Test
    public void testRightBracketAfterLeftBracketThrowsAnException() {
        LaconLexer lexer = new LaconLexer("(5 - 1)(1 + 6)");
        Exception exception = null;
        try {
            getTokens(lexer);
        } catch (Exception e) {
            exception = e;
        }
        assertNotNull(exception);
    }

    @Test
    public void testUnaryOperation() {
        LaconLexer lexer = new LaconLexer("7 + 3 * (10 / (12 / (3 + 1) - 1))");
        List<LaconToken> tokens = getTokens(lexer);
    }
}
