package ua.nechay.lacon.ast.call;

import ua.nechay.lacon.ast.AST;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.val.ListLaconValue;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author anechaev
 * @since 04.05.2023
 */
public class PositionCallArgsAST implements AST {

    private final List<AST> expressions;

    public PositionCallArgsAST(@Nonnull List<AST> expressions) {
        this.expressions = expressions;
    }

    @Nonnull
    @Override
    public LaconProgramState interpret(@Nonnull LaconProgramState state) {
        List<LaconValue<?>> argsList = new ArrayList<>();
        LaconProgramState iterState = state;
        for (var ast : expressions) {
            iterState = ast.interpret(iterState);
            argsList.add(iterState.popValue());
        }
        return iterState.pushValue(new ListLaconValue(argsList));
    }
}
