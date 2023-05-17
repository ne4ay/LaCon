package ua.nechay.lacon.ast.value;

import ua.nechay.lacon.LaconTokenType;
import ua.nechay.lacon.ast.AST;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.val.DictLaconValue;
import ua.nechay.lacon.utils.Pair;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author anechaev
 * @since 17.05.2023
 */
public class DictAST implements AST {

    private final List<Pair<AST, AST>> entries;

    public DictAST(@Nonnull List<Pair<AST, AST>> entries) {
        this.entries = entries;
    }

    @Nonnull
    @Override
    public LaconProgramState interpret(@Nonnull LaconProgramState state) {
        LaconProgramState iterState = state;
        Map<LaconValue<?>, LaconValue<?>> map = new HashMap<>();
        for (var entry : entries) {
            AST keyAST = entry.getFirst();
            AST valAST = entry.getSecond();

            LaconProgramState afterKeyState = keyAST.interpret(iterState);
            LaconValue<?> key = afterKeyState.popValue();

            LaconProgramState afterValueState = valAST.interpret(afterKeyState);
            LaconValue<?> value = afterValueState.popValue();
            map.put(key, value);
            iterState = afterValueState;
        }
        return iterState.pushValue(new DictLaconValue(map));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof DictAST))
            return false;
        DictAST dictAST = (DictAST) o;
        return Objects.equals(entries, dictAST.entries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entries);
    }
}
