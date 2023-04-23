package ua.nechay.lacon.ast.value;

import ua.nechay.lacon.LaconToken;
import ua.nechay.lacon.ast.AST;
import ua.nechay.lacon.core.LaconBuiltInType;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 06.03.2023
 */
public class IntNumAST extends AbstractValueAST implements AST {

    public IntNumAST(@Nonnull LaconToken token) {
        super(token);
    }

    @Override @Nonnull
    public Long getValue() {
        String text = getToken().getText();
        return Long.parseLong(removeRedundantCharacters(text));
    }

    @Nonnull
    @Override
    public LaconBuiltInType getType() {
        return LaconBuiltInType.INT;
    }

    @Nonnull
    private String removeRedundantCharacters(@Nonnull String text) {
        return text.replace("_", "");
    }
}
