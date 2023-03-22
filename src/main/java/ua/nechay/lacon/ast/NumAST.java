package ua.nechay.lacon.ast;

import ua.nechay.lacon.LaconToken;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * @author anechaev
 * @since 06.03.2023
 */
public class NumAST implements AST {

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
        return Integer.parseInt(removeRedundantCharacters(token.getText()));
    }

    @Nonnull
    private String removeRedundantCharacters(@Nonnull String text) {
        return text.replace("_", "");
    }

    @Override
    public int interpret() {
        return getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof NumAST))
            return false;
        NumAST numAST = (NumAST) o;
        return Objects.equals(token, numAST.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }
}
