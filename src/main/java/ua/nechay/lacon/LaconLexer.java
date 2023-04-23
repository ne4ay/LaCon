package ua.nechay.lacon;

import ua.nechay.lacon.utils.LaconScannerState;
import ua.nechay.lacon.utils.LaconUtils;

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
    private final LaconScannerState state;
    private int position;
    private Character currentChar;

    public LaconLexer(@Nonnull String text) {
        this.text = text;
        this.state = LaconScannerState.create();
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

    @Nonnull
    @Override
    public LaconScannerState getState() {
        return state;
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
            if (LaconTokenType.SPACE.matches(this, previousToken) || LaconTokenType.NEXT_LINE.matches(this, previousToken)) {
                skipWhiteSpace();
                continue;
            }
            return getStandardToken(LaconTokenType.getStandardTypes(), previousToken);
        }
        return new LaconToken(LaconTokenType.EOF, "", getCurrentPosition());
    }

    private LaconToken getStandardToken(@Nonnull List<LaconTokenType> tokenTypes, LaconToken previousToken) {
        LaconTokenType appropriateType = null;
        for (var type : tokenTypes) {
            if (type.matches(this, previousToken)) {
                appropriateType = type;
                break;
            }
        }
        if (appropriateType == null) {
            throw new IllegalStateException("Unable to parse at char " + getCurrentChar() + " at position: " + getCurrentPosition());
        }
        return appropriateType.toToken(this, previousToken);
    }

}
