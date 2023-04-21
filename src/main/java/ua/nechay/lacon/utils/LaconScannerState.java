package ua.nechay.lacon.utils;

import ua.nechay.lacon.LaconLexer;
import ua.nechay.lacon.LaconToken;
import ua.nechay.lacon.LaconTokenType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author anechaev
 * @since 20.04.2023
 */
public class LaconScannerState {

    private final Deque<LaconToken> openedQuotes = new ArrayDeque<>();
    private final Deque<LaconToken> openedSquareBrackets = new ArrayDeque<>();
    private final Deque<LaconToken> openedCurlBrackets = new ArrayDeque<>();
    private final Deque<LaconToken> openedBraces = new ArrayDeque<>();

    private LaconScannerState() {

    }

    public static LaconScannerState create() {
        return new LaconScannerState();
    }

    @Nonnull
    public LaconScannerState pushQuote(@Nonnull LaconToken quote) {
        return pushToken(quote, LaconTokenType.QUOTE, openedQuotes);
    }

    public LaconScannerState acceptNextQuote(@Nonnull LaconToken quote) {
        if (isInsideQuotes()) {
            popQuote();
        } else {
            pushQuote(quote);
        }
        return this;
    }

    public boolean isInsideQuotes() {
        return !openedQuotes.isEmpty();
    }

    @Nonnull
    public LaconScannerState pushCurlBracket(@Nonnull LaconToken curlBracket) {
        return pushToken(curlBracket, LaconTokenType.LEFT_CURLY_BRACKET, openedCurlBrackets);
    }

    @Nonnull
    public LaconScannerState pushBrace(@Nonnull LaconToken brace) {
        return pushToken(brace, LaconTokenType.LEFT_BRACKET, openedBraces);
    }

    @Nullable
    public LaconToken popQuote() {
        return popToken(openedQuotes);
    }

    @Nullable
    public LaconToken popCurlBracket() {
        return popToken(openedCurlBrackets);
    }

    @Nullable
    public LaconToken popBrace() {
        return popToken(openedBraces);
    }

    @Nullable
    private LaconToken popToken(@Nonnull Deque<LaconToken> stack) {
        return stack.pop();
    }

    private LaconScannerState pushToken(@Nonnull LaconToken token, @Nonnull LaconTokenType type, @Nonnull Deque<LaconToken> appropriateStack) {
        if (token.getType() != type) {
            throw new IllegalStateException("Unable to push token " + token + " into " + type + " stack!!!");
        }
        appropriateStack.push(token);
        return this;
    }
}
