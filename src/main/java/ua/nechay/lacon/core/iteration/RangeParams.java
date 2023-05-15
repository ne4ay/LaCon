package ua.nechay.lacon.core.iteration;

import ua.nechay.lacon.LaconTokenType;
import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.val.IntLaconValue;
import ua.nechay.lacon.core.val.RealLaconValue;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * @author anechaev
 * @since 13.05.2023
 */
public class RangeParams {

    private final double start;
    private final double increment;
    private final double end;
    private final LaconBuiltInType type;

    public RangeParams(double start, double increment, double end, @Nonnull LaconBuiltInType type) {
        this.start = start;
        this.increment = increment;
        this.end = end;
        this.type = type;
    }

    @Nonnull
    public static RangeParams create(@Nonnull LaconValue<?> start, @Nonnull LaconValue<?> increment, @Nonnull LaconValue<?> end) {
        LaconBuiltInType rangeType;
        if (start.getType() == LaconBuiltInType.REAL || increment.getType() == LaconBuiltInType.REAL) {
            rangeType = LaconBuiltInType.REAL;
        } else {
            rangeType = LaconBuiltInType.INT;
        }
        return new RangeParams(
            getParam(start),
            getParam(increment),
            getParam(end),
            rangeType);
    }

    private static double getParam(@Nonnull LaconValue<?> value) {
        switch (value.getType()) {
        case INT:
            return IntLaconValue.castToReal((long)value.getValue());
        case REAL:
            return (double) value.getValue();
        default:
            throw new IllegalStateException("Unexpected value for range param: " + value);
        }
    }

    public double getStart() {
        return start;
    }

    public double getIncrement() {
        return increment;
    }

    public double getEnd() {
        return end;
    }

    @Nonnull
    public LaconBuiltInType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof RangeParams))
            return false;
        RangeParams that = (RangeParams) o;
        return Double.compare(that.start, start) == 0 && Double.compare(that.increment, increment) == 0
            && Double.compare(that.end, end) == 0 && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, increment, end, type);
    }
}
