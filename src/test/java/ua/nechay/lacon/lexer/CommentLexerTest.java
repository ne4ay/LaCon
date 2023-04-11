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
 * @since 11.04.2023
 */
public class CommentLexerTest extends AbstractLexerTestBase  {

    @Test
    public void testSimpleComment() {
        LaconLexer lexer = new LaconLexer("//Some interesting comment\n");
        List<LaconToken> tokens = getTokens(lexer);
        assertThat(tokens.size(), equalTo(1));

        LaconToken token = tokens.get(0);
        assertThat(token.getType(), equalTo(LaconTokenType.COMMENT));
    }
}
