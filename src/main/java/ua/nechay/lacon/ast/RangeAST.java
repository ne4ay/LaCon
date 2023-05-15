package ua.nechay.lacon.ast;

import ua.nechay.lacon.ast.auxiliary.IntStubAST;
import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.iteration.RangeParams;
import ua.nechay.lacon.core.val.ListLaconValue;
import ua.nechay.lacon.utils.Pair;

import javax.annotation.Nonnull;
import java.util.EnumSet;

/**
 * @author anechaev
 * @since 13.05.2023
 */
public class RangeAST implements AST {
    private static final EnumSet<LaconBuiltInType> PARAM_ACCEPTABLE_TYPES = EnumSet.of(LaconBuiltInType.INT, LaconBuiltInType.REAL);

    private final AST begin;
    private final AST increment;
    private final AST end;

    public RangeAST(@Nonnull AST begin, @Nonnull AST increment, @Nonnull AST end) {
        this.begin = begin;
        this.increment = increment;
        this.end = end;
    }

    @Nonnull
    public static RangeAST createDefault(@Nonnull AST end) {
        return createDefault(new IntStubAST(0L), end);
    }

    @Nonnull
    public static RangeAST createDefault(@Nonnull AST begin, @Nonnull AST end) {
        return new RangeAST(begin, new IntStubAST(1L), end);
    }

    @Nonnull
    @Override
    public LaconProgramState interpret(@Nonnull LaconProgramState state) {
        Pair<LaconProgramState, RangeParams> stateAndParams = getParams(state);
        return stateAndParams.merge((newState, params) -> newState.pushValue(ListLaconValue.createFromRangeParams(params)));
    }

    @Nonnull
    public LazyRangeAST asLazy() {
        return new LazyRangeAST(getBegin(), getIncrement(), getEnd());
    }

    protected Pair<LaconProgramState, RangeParams> getParams(@Nonnull LaconProgramState state) {
        Pair<LaconProgramState, LaconValue<?>> start = getValue(state, getBegin());
        Pair<LaconProgramState, LaconValue<?>> increment = getValue(start.getFirst(), getIncrement());
        Pair<LaconProgramState, LaconValue<?>> end = getValue(increment.getFirst(), getEnd());
        return new Pair<>(end.getFirst(), RangeParams.create(
            start.getSecond(),
            increment.getSecond(),
            end.getSecond()));
    }

    @Nonnull
    private Pair<LaconProgramState, LaconValue<?>> getValue(@Nonnull LaconProgramState state, @Nonnull AST ast) {
        LaconProgramState afterInterpretationState = ast.interpret(state);
        LaconValue<?> val = afterInterpretationState.popValue();
        if (!PARAM_ACCEPTABLE_TYPES.contains(val.getType())) {
            throw new IllegalStateException("The range param should have INT or REAL type: " + val);
        }
        return new Pair<>(afterInterpretationState, val);
    }

    @Nonnull
    public AST getBegin() {
        return begin;
    }

    @Nonnull
    public AST getIncrement() {
        return increment;
    }

    @Nonnull
    public AST getEnd() {
        return end;
    }
}
