package ua.nechay.lacon.lexer;

import ua.nechay.lacon.LaconToken;
import ua.nechay.lacon.LaconTokenType;
import ua.nechay.lacon.Lexer;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author anechaev
 * @since 11.04.2023
 */
public abstract class AbstractLexerTestBase {

    protected List<LaconToken> getTokens(@Nonnull Lexer lexer) {
        LaconToken token = null;
        List<LaconToken> tokens = new ArrayList<>();
        while ((token = lexer.getNextToken(token)).getType() != LaconTokenType.EOF) {
            tokens.add(token);
        }
        return tokens;
    }
}
