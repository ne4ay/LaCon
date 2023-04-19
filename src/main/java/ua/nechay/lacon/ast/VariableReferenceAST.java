package ua.nechay.lacon.ast;

import ua.nechay.lacon.LaconToken;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.var.LaconVariable;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 18.04.2023
 */
public class VariableReferenceAST implements AST, AssignableAST {

    private final LaconToken identifier;

    public VariableReferenceAST(@Nonnull LaconToken identifier) {
        this.identifier = identifier;
    }

    @Nonnull
    @Override
    public LaconProgramState interpret(@Nonnull LaconProgramState state) {
        String variableName = getIdentifier().getText();
        LaconVariable existingVariable = state.getVar(variableName);
        if (existingVariable == null) {
            throw new IllegalStateException("The variable " + variableName + " is already defined");
        }
        return state.pushValue(existingVariable.getValueObject());
    }

    @Nonnull
    @Override
    public LaconProgramState assign(@Nonnull LaconProgramState state, @Nonnull LaconValue<?> value) {
        LaconVariable variable = getExistingVariable(state);
        return state.putVar(getIdentifier().getText(), variable.setValue(value));
    }

    private LaconVariable getExistingVariable(@Nonnull LaconProgramState state) {
        String variableName = getIdentifier().getText();
        LaconVariable existingVariable = state.getVar(variableName);
        if (existingVariable == null) {
            throw new IllegalStateException("The variable " + variableName + " is already defined");
        }
        return existingVariable;
    }

    public LaconToken getIdentifier() {
        return identifier;
    }
}
