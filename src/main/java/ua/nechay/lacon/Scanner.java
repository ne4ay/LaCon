package ua.nechay.lacon;


import javax.annotation.Nullable;

/**
 * @author anechaev
 * @since 06.03.2023
 */
public interface Scanner {

    @Nullable
    Character getCurrentChar();

    int getCurrentPosition();

    void advance();
}
