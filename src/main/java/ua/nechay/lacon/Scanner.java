package ua.nechay.lacon;


import ua.nechay.lacon.utils.LaconScannerState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author anechaev
 * @since 06.03.2023
 */
public interface Scanner {

    /**
     * @return not advance
     */
    @Nullable
    Character getCurrentChar();


    /**
     * Examine next character (after {@link Scanner#getCurrentChar}), but not advance
     *
     * @param i how many character to cross. Use 1 if you want to peek next character
     * @return peeked character
     */
    @Nullable
    Character peek(int i);

    @Nonnull
    LaconScannerState getState();

    int getCurrentPosition();

    void advance();
}
