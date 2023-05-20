package ua.nechay.lacon.ast;

import ua.nechay.lacon.LaconToken;
import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconType;
import ua.nechay.lacon.core.LaconValueUtils;
import ua.nechay.lacon.core.auxiliary.LaconArgumentDeclarationValue;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 03.05.2023
 */
public class FunctionArgumentDeclarationAST implements AST {
    private final LaconToken identifier;
    private final LaconToken type;

    public FunctionArgumentDeclarationAST(@Nonnull LaconToken identifier, @Nonnull LaconToken type) {
        this.identifier = identifier;
        this.type = type;
    }

    @Nonnull
    @Override
    public LaconProgramState interpret(@Nonnull LaconProgramState state) {
        String argumentName = identifier.getText();
        LaconType type = LaconValueUtils.determineType(state, this.type);
        return state.pushValue(new LaconArgumentDeclarationValue(argumentName, type));
    }
}
