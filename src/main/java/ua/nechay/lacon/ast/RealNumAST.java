package ua.nechay.lacon.ast;

import ua.nechay.lacon.LaconToken;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconValue;

import javax.annotation.Nonnull;
import java.util.Objects;

import static ua.nechay.lacon.core.LaconType.REAL;

/**
 * @author anechaev
 * @since 15.04.2023
 */
public class RealNumAST implements AST {

    private final LaconToken token;

    public RealNumAST(@Nonnull LaconToken token) {
        this.token = token;
    }

    public LaconToken getToken() {
        return token;
    }

    public double getValue() {
        String text = getToken().getText();
        if (text == null) {
            throw new NullPointerException();
        }
        return Double.parseDouble(removeRedundantCharacters(text));
    }

    @Nonnull
    private String removeRedundantCharacters(@Nonnull String text) {
        return text.replace("_", "");
    }

    @Override @Nonnull
    public LaconProgramState interpret(@Nonnull LaconProgramState state) {
        return state.pushValue(LaconValue.create(getValue(), REAL));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof RealNumAST))
            return false;
        RealNumAST that = (RealNumAST) o;
        return Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }
}
