package ua.nechay.lacon.ast.call;

import ua.nechay.lacon.LaconToken;
import ua.nechay.lacon.ast.AST;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.val.DictLaconValue;
import ua.nechay.lacon.core.val.StringLaconValue;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * @author anechaev
 * @since 04.05.2023z
 */
public class NamedCallArgsAST implements AST {

    private final Map<LaconToken, AST> args;

    public NamedCallArgsAST(@Nonnull Map<LaconToken, AST> args) {
        this.args = args;
    }

    @Nonnull
    @Override
    public LaconProgramState interpret(@Nonnull LaconProgramState state) {
        Map<LaconValue<?>, LaconValue<?>> argsDictionary = new HashMap<>();
        LaconProgramState iterState = state;
        for (var entry : args.entrySet()) {
            String identifier = entry.getKey().getText();
            AST expression = entry.getValue();
            iterState = expression.interpret(iterState);
            argsDictionary.put(new StringLaconValue(identifier), iterState.popValue());
        }

        return iterState.pushValue(new DictLaconValue(argsDictionary));
    }

}
