package ua.nechay.lacon.ast;

import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.utils.Pair;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 30.04.2023
 */
public class WhileCycleAST implements AST {

    private final AST condition;
    private final AST statementBlock;

    public WhileCycleAST(@Nonnull AST condition, @Nonnull AST statementBlock) {
        this.condition = condition;
        this.statementBlock = statementBlock;
    }


    @Nonnull
    @Override
    public LaconProgramState interpret(@Nonnull LaconProgramState state) {
        Pair<Boolean, LaconProgramState> pair = new Pair<>(/*ignored*/ true, state);
        while ((pair = isNeedToPerformNextCycle(pair.getSecond())).getFirst()) {
            var afterConditionState = pair.getSecond();
            var afterStatementState = statementBlock.interpret(afterConditionState);
            pair = new Pair<>(pair.getFirst(), afterStatementState);
        }
        return pair.getSecond();
    }

    private Pair<Boolean, LaconProgramState> isNeedToPerformNextCycle(LaconProgramState state) {
        LaconProgramState afterConditionState = condition.interpret(state);
        LaconValue<?> value = afterConditionState.popValue();
        if (value.getType() != LaconBuiltInType.BOOLEAN) {
            value = value.castTo(LaconBuiltInType.BOOLEAN);
        }
        return new Pair<>((boolean)value.getValue(), afterConditionState);
    }


}
