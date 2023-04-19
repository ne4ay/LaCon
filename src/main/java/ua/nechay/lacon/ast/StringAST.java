package ua.nechay.lacon.ast;

import ua.nechay.lacon.LaconToken;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconType;
import ua.nechay.lacon.core.LaconValue;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * @author anechaev
 * @since 19.04.2023
 */
public class StringAST implements AST {

    private final LaconToken token;

    public StringAST(@Nonnull LaconToken token) {
        this.token = token;
    }

    @Nonnull
    public LaconToken getToken() {
        return token;
    }

    public String getValue() {
        return getToken().getText();
    }

    @Nonnull
    @Override
    public LaconProgramState interpret(@Nonnull LaconProgramState state) {
        return state.pushValue(LaconValue.create(getValue(), LaconType.STRING));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof StringAST))
            return false;
        StringAST stringAST = (StringAST) o;
        return Objects.equals(token, stringAST.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }
}
