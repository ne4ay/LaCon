package ua.nechay.lacon.core.iteration;

import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconValue;

import javax.annotation.Nonnull;
import java.util.Iterator;

/**
 * @author anechaev
 * @since 13.05.2023
 */
public class LazyIterableRangeLaconValue extends LaconValue<RangeLazyIterator> {
    public LazyIterableRangeLaconValue(@Nonnull RangeLazyIterator value) {
        super(value, LaconBuiltInType.RANGE);
    }

    public static LazyIterableRangeLaconValue create(double start, double increment, double end, @Nonnull LaconBuiltInType type) {
        return new LazyIterableRangeLaconValue(new RangeLazyIterator(start, increment, end, type));
    }

    public static LazyIterableRangeLaconValue create(@Nonnull RangeParams rangeParams) {
        LaconBuiltInType type = rangeParams.getType();
        return LazyIterableRangeLaconValue.create(rangeParams.getStart(), rangeParams.getIncrement(), rangeParams.getEnd(), type);
    }

    @Override
    public Iterator<LaconValue<?>> iterator() {
        return getValue();
    }
}
