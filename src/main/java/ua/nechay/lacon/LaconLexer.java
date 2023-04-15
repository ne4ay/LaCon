package ua.nechay.lacon;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

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

    @Nullable
    @Override
    public Character peek(int i) {
        int nextPosition = this.position + i;
        if (nextPosition >= text.length()) {
            return null;
        } else {
            return text.charAt(nextPosition);
        }
    }

    @Override
    public int getCurrentPosition() {
        return position;
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
        while (this.currentChar != null && (LaconUtils.isSpace(this.currentChar) || LaconUtils.isNextLine(this.currentChar))) {
            this.advance();
        }
    }

    @Nonnull
    @Override
    public LaconToken getNextToken(@Nullable LaconToken previousToken) {
        while (this.currentChar != null) {

            if (LaconTokenType.SPACE.matches(this)) {
                skipWhiteSpace();
                continue;
            }
            return getStandardToken(LaconTokenType.getStandardTypes(), previousToken)
                .orElseThrow(() -> new IllegalStateException("Unknown character: " + this.currentChar));
        }
        return new LaconToken(LaconTokenType.EOF, "", position);
    }

    private Optional<LaconToken> getStandardToken(@Nonnull List<LaconTokenType> tokenTypes, LaconToken previousToken) {
        return tokenTypes.stream()
           .filter(type -> type.matches(this))
           .findFirst()
           .map(type -> type.toToken(this, previousToken));
    }

}
