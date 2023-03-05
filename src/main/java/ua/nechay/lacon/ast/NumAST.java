package ua.nechay.lacon.ast;

import ua.nechay.lacon.LaconToken;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 06.03.2023
 */
public class NumAST {

    private final LaconToken token;

    public NumAST(@Nonnull LaconToken token) {
        this.token = token;
    }

    public LaconToken getToken() {
        return token;
    }

    public int getValue() {
        if (token.getText() == null) {
            throw new NullPointerException();
        }
        return Integer.parseInt(token.getText());
    }
}
