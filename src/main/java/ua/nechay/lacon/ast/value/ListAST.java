package ua.nechay.lacon.ast.value;

import ua.nechay.lacon.ast.AST;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.val.ListLaconValue;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author anechaev
 * @since 23.04.2023
 */
public class ListAST implements AST {

    private final List<AST> valuesList;

    public ListAST() {
        this.valuesList = new ArrayList<>();
    }

    public ListAST(@Nonnull List<AST> valuesList) {
        this.valuesList = valuesList;
    }

    @Nonnull
    @Override
    public LaconProgramState interpret(@Nonnull LaconProgramState state) {
        List<Object> resultList = valuesList.stream()
            .map(valueAST -> valueAST.interpret(state).popValue())
            .map(LaconValue::getValue)
            .collect(Collectors.toList());
        return state.pushValue(new ListLaconValue(resultList));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ListAST))
            return false;
        ListAST listAST = (ListAST) o;
        return Objects.equals(valuesList, listAST.valuesList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valuesList);
    }
}
