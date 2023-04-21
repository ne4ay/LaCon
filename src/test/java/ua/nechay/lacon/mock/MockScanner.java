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
        return text.charAt(pos + i);
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
