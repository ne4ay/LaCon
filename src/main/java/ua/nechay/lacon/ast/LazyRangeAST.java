package ua.nechay.lacon.ast;

import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.iteration.LazyIterableRangeLaconValue;
import ua.nechay.lacon.core.iteration.RangeParams;
import ua.nechay.lacon.utils.Pair;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 13.05.2023
 */
public class LazyRangeAST extends RangeAST implements AST {

    public LazyRangeAST(@Nonnull AST begin, @Nonnull AST increment, @Nonnull AST end) {
        super(begin, increment, end);
    }

    @Nonnull
    @Override
    public LaconProgramState interpret(@Nonnull LaconProgramState state) {
        Pair<LaconProgramState, RangeParams> stateAndParams = getParams(state);
        return stateAndParams.merge((newState, params) -> newState.pushValue(LazyIterableRangeLaconValue.create(params)));
    }
}
