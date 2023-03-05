package ua.nechay.lacon;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author anechaev
 * @since 06.03.2023
 */
public interface Lexer {

    @Nonnull
    LaconToken getNextToken();
}
