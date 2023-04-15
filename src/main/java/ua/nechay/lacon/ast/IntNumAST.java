package ua.nechay.lacon.ast;

import ua.nechay.lacon.LaconToken;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconValue;

import javax.annotation.Nonnull;
import java.util.Objects;

import static ua.nechay.lacon.core.LaconType.INT;

/**
 * @author anechaev
 * @since 06.03.2023
 */
public class IntNumAST implements AST {

    private final LaconToken token;

    public IntNumAST(@Nonnull LaconToken token) {
        this.token = token;
    }

    public LaconToken getToken() {
        return token;
    }

    public long getValue() {
        if (token.getText() == null) {
            throw new NullPointerException();
        }
        return Long.parseLong(removeRedundantCharacters(token.getText()));
    }

    @Nonnull
    private String removeRedundantCharacters(@Nonnull String text) {
        return text.replace("_", "");
    }

    @Override @Nonnull
    public LaconProgramState interpret(@Nonnull LaconProgramState state) {
        return state.pushValue(LaconValue.create(getValue(), INT));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof IntNumAST))
            return false;
        IntNumAST intNumAST = (IntNumAST) o;
        return Objects.equals(token, intNumAST.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }
}
