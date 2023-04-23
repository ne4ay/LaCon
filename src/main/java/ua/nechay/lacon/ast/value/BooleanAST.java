package ua.nechay.lacon.ast.value;

import ua.nechay.lacon.LaconToken;
import ua.nechay.lacon.ast.AST;
import ua.nechay.lacon.core.LaconBuiltInType;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 21.04.2023
 */
public class BooleanAST extends AbstractValueAST implements AST {

    public BooleanAST(@Nonnull LaconToken token) {
        super(token);
    }

    @Nonnull
    @Override
    public Boolean getValue() {
        String text = getToken().getText();
        return Boolean.valueOf(text);
    }

    @Nonnull
    @Override
    public LaconBuiltInType getType() {
        return LaconBuiltInType.BOOLEAN;
    }
}
