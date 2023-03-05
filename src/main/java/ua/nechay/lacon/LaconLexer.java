package ua.nechay.lacon;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author anechaev
 * @since 05.03.2023
 */
public class LaconLexer implements Scanner, Lexer {

    private final String text;
    private int position;
    private Character currentChar;

    public LaconLexer(@Nonnull String text) {
        this.text = text;
        this.position = 0;
        this.currentChar = text.charAt(position);
    }

    @Nullable
    @Override
    public Character getCurrentChar() {
        return currentChar;
    }

    /**
     * Advance the `position` pointer and set the `currentChar` variable.
     */
    public void advance() {
        if (++position >= text.length()) {
            this.currentChar = null;
        } else {
            this.currentChar = text.charAt(position);
        }
    }

    public void skipWhiteSpace() {
        while (this.currentChar != null && LaconUtils.isSpace(this.currentChar)) {
            this.advance();
        }
    }

    @Nonnull
    @Override
    public LaconToken getNextToken() {
        return null;
    }
}
