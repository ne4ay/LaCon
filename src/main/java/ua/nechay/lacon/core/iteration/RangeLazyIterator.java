package ua.nechay.lacon.core.iteration;

import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconValue;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.function.Function;

import static ua.nechay.lacon.core.LaconValueUtils.determineMapper;

/**
 * @author anechaev
 * @since 13.05.2023
 */
public class RangeLazyIterator implements Iterator<LaconValue<?>>, Iterable<LaconValue<?>> {

    private final double start;
    private final double increment;
    private final double end;
    private final Function<Double, LaconValue<?>> valueMapper;
    private double caret;

    public RangeLazyIterator(double start, double increment, double end, @Nonnull LaconBuiltInType type) {
        this.start = start;
        this.increment = increment;
        this.end = end;
        this.valueMapper = determineMapper(type);
        this.caret = this.start;
    }

    private boolean isTrivial() {
        return start < end;
    }

    @Override
    public boolean hasNext() {
        return isTrivial() ? caret < end : caret > end;
    }

    @Override
    public LaconValue<?> next() {
        LaconValue<?> result = valueMapper.apply(caret);
        if (isTrivial()) {
            caret += increment;
        } else {
            caret -= increment;
        }
        return result;
    }

    @Override
    public Iterator<LaconValue<?>> iterator() {
        return this;
    }
}
