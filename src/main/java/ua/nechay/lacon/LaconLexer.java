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
    public LaconToken getNextToken(@Nullable LaconToken previousToken) {
        while (this.currentChar != null) {

            if (LaconTokenType.SPACE.matches(this.currentChar)) {
                skipWhiteSpace();
                continue;
            }
            return getStandardToken(List.of(
                    LaconTokenType.INTEGER, LaconTokenType.PLUS, LaconTokenType.MINUS,
                    LaconTokenType.MUL, LaconTokenType.DIV, LaconTokenType.LEFT_BRACKET,
                    LaconTokenType.RIGHT_BRACKET),
                previousToken
                )
                .orElseThrow(() -> new IllegalStateException("Unknown character: " + this.currentChar));
        }
        return new LaconToken(LaconTokenType.EOF, "");
    }

    private Optional<LaconToken> getStandardToken(@Nonnull List<LaconTokenType> tokenTypes, LaconToken previousToken) {
         Optional<LaconToken> maybeToken = tokenTypes.stream()
            .filter(type -> type.matches(this.currentChar))
            .findFirst()
            .map(type -> type.toToken(this, previousToken));
        return maybeToken;
    }

}
