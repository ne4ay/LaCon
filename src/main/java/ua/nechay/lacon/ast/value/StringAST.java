package ua.nechay.lacon.ast.value;

import ua.nechay.lacon.LaconToken;
import ua.nechay.lacon.ast.AST;
import ua.nechay.lacon.core.LaconBuiltInType;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 19.04.2023
 */
public class StringAST extends AbstractValueAST implements AST {
    public StringAST(@Nonnull LaconToken token) {
        super(token);
    }

    @Override @Nonnull
    public String getValue() {
        return getToken().getText();
    }

    @Nonnull
    @Override
    public LaconBuiltInType getType() {
        return LaconBuiltInType.STRING;
    }
}
