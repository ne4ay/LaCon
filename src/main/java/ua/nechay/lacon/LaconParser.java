package ua.nechay.lacon;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 06.03.2023
 */
public class LaconParser {

    private final Lexer lexer;

    private LaconToken currentToken;

    public LaconParser(Lexer lexer) {
        this.lexer = lexer;
        this.currentToken = lexer.getNextToken();
    }

    /**
     * Compares the current token type with the passed token type and
     * if they match then "eat" the current token
     * and assign the next token to the {@link this#currentToken},
     * otherwise throws an exception.
     */
    public void eat(@Nonnull LaconTokenType tokenType) {
        if (currentToken.getType() == tokenType) {
            this.currentToken = lexer.getNextToken();
        }
        throw new IllegalStateException("Illegal syntax!!!");
    }

    public void factor() {
        LaconTokenType type = this.currentToken.getType();
    }
}
