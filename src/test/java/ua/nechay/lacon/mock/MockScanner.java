package ua.nechay.lacon.mock;

import ua.nechay.lacon.Scanner;
import ua.nechay.lacon.utils.LaconScannerState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author anechaev
 * @since 15.04.2023
 */
public class MockScanner implements Scanner {
    private final String text;
    private final LaconScannerState state;
    private int pos;

    public MockScanner(String text) {
        this.text = text;
        this.state = LaconScannerState.create();
        this.pos = 0;
    }

    @Nullable
    @Override
    public Character getCurrentChar() {
        return text.charAt(pos);
    }

    @Nullable
    @Override
    public Character peek(int i) {
        int nextPosition = this.pos + i;
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
        return pos;
    }

    @Override
    public void advance() {
        pos++;
    }
}
