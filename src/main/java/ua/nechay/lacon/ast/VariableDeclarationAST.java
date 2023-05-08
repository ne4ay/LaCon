package ua.nechay.lacon.ast;

import ua.nechay.lacon.LaconToken;
import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.LaconValueUtils;
import ua.nechay.lacon.core.var.LaconVariable;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 18.04.2023
 */
public class VariableDeclarationAST implements AST, AssignableAST {
    private final LaconToken identifier;
    private final LaconToken type;

    public VariableDeclarationAST(@Nonnull LaconToken identifier, @Nonnull LaconToken type) {
        this.identifier = identifier;
        this.type = type;
    }

    @Nonnull
    @Override
    public LaconProgramState interpret(@Nonnull LaconProgramState state) {
        String variableName = identifier.getText();
        LaconVariable existingVariable = state.getVar(variableName);
        if (existingVariable != null) {
            throw new IllegalStateException("The variable " + variableName + " is already defined");
        }
        LaconBuiltInType type = LaconValueUtils.determineType(this.type);
        LaconVariable variable = new LaconVariable(type);
        return state.putVar(variableName, variable);
    }

    @Nonnull
    @Override
    public LaconProgramState assign(@Nonnull LaconProgramState state, @Nonnull LaconValue<?> value) {
        String variableName = identifier.getText();
        LaconVariable existingVariable = state.getVar(variableName);
        if (existingVariable == null) {
            throw new IllegalStateException("The variable " + variableName + " is already defined");
        }
        return state.putVar(variableName, existingVariable.setValue(value));
    }
}
