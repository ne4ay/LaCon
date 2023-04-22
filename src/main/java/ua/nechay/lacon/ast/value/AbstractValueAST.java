package ua.nechay.lacon.ast.value;

import ua.nechay.lacon.LaconToken;
import ua.nechay.lacon.ast.AST;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconType;
import ua.nechay.lacon.core.LaconValue;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * @author anechaev
 * @since 21.04.2023
 */
public abstract class AbstractValueAST implements AST {

    private final LaconToken token;

    protected AbstractValueAST(@Nonnull LaconToken token) {
        this.token = token;
    }

    @Nonnull
    public LaconToken getToken() {
        return token;
    }

    @Override @Nonnull
    public LaconProgramState interpret(@Nonnull LaconProgramState state) {
        return state.pushValue(LaconValue.create(getValue(), getType()));
    }

    @Nonnull public abstract Object getValue();
    @Nonnull public abstract LaconType getType();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof AbstractValueAST))
            return false;
        AbstractValueAST that = (AbstractValueAST) o;
        return Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }
}
