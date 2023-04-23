package ua.nechay.lacon.ast.value;

import ua.nechay.lacon.LaconToken;
import ua.nechay.lacon.ast.AST;
import ua.nechay.lacon.core.LaconBuiltInType;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 15.04.2023
 */
public class RealNumAST extends AbstractValueAST implements AST {

    public RealNumAST(@Nonnull LaconToken token) {
        super(token);
    }

    @Override @Nonnull
    public Double getValue() {
        String text = getToken().getText();
        return Double.parseDouble(removeRedundantCharacters(text));
    }

    @Nonnull
    @Override
    public LaconBuiltInType getType() {
        return LaconBuiltInType.REAL;
    }

    @Nonnull
    private String removeRedundantCharacters(@Nonnull String text) {
        return text.replace("_", "");
    }
}
