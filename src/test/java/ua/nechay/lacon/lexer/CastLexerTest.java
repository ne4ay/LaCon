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
 * @since 22.04.2023
 */
public class CastLexerTest extends AbstractLexerTestBase{

    @Test
    public void testCasting() {
        LaconLexer lexer = new LaconLexer("a : int = int(b + (\"als\" + 123.32) - 12)");
        List<LaconToken> tokens = getTokens(lexer);

        assertThat(tokens.size(), equalTo(18));

        assertThat(tokens.get(0).getText(), equalTo("a"));
        assertThat(tokens.get(0).getType(), equalTo(LaconTokenType.IDENTIFIER));

        assertThat(tokens.get(1).getText(), equalTo(":"));
        assertThat(tokens.get(1).getType(), equalTo(LaconTokenType.COLON));

        assertThat(tokens.get(2).getText(), equalTo("int"));
        assertThat(tokens.get(2).getType(), equalTo(LaconTokenType.TYPE));

        assertThat(tokens.get(3).getText(), equalTo("="));
        assertThat(tokens.get(3).getType(), equalTo(LaconTokenType.ASSIGNMENT));

        assertThat(tokens.get(4).getText(), equalTo("int"));
        assertThat(tokens.get(4).getType(), equalTo(LaconTokenType.CAST));
    }
}
