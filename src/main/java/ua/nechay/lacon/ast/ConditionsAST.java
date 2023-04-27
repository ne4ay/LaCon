package ua.nechay.lacon.ast;

import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.utils.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author anechaev
 * @since 26.04.2023
 */
public class ConditionsAST implements AST {

    /**
     *  left - condition AST
     *  right - statement AST
     */
    private final List<Pair<AST, AST>> singleChainedConditions;
    @Nullable private final AST elseStatement;

    public ConditionsAST(@Nonnull List<Pair<AST, AST>> singleChainedConditions, @Nullable AST elseStatement) {
        this.singleChainedConditions = singleChainedConditions;
        this.elseStatement = elseStatement;
    }


    @Nonnull
    @Override
    public LaconProgramState interpret(@Nonnull LaconProgramState state) {
        LaconProgramState cumulativeState = state;
        boolean isAnyChainConditionExecuted = false;
        for (var singleConditionalStatement : singleChainedConditions) {
            AST conditionPart = singleConditionalStatement.getFirst();
            AST statementPart = singleConditionalStatement.getSecond();

            cumulativeState = conditionPart.interpret(state);
            LaconValue<?> conditionValue = cumulativeState.popValue();
            if (!LaconBuiltInType.BOOLEAN.equals(conditionValue.getType())) {
                throw new IllegalStateException("Unable to put non-boolean value to condition: " + conditionValue);
            }
            boolean condition = (boolean) conditionValue.getValue();
            if (condition) {
                isAnyChainConditionExecuted = true;
                cumulativeState = statementPart.interpret(cumulativeState);
                break;
            }
        }
        if (!isAnyChainConditionExecuted && elseStatement != null) {
            cumulativeState = elseStatement.interpret(cumulativeState);
        }
        return cumulativeState;
    }
}
