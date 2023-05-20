package ua.nechay.lacon.core.val;

import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconType;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.LaconValueUtils;
import ua.nechay.lacon.core.function.FunctionLaconValue;
import ua.nechay.lacon.core.function.LaconMethodName;
import ua.nechay.lacon.core.function.built.LaconBuiltInSizeMethod;
import ua.nechay.lacon.core.iteration.RangeParams;
import ua.nechay.lacon.core.touch.TypeTouch;
import ua.nechay.lacon.core.touch.TypeTouchBuilder;
import ua.nechay.lacon.core.touch.UnsupportedOperationTypeTouch;
import ua.nechay.lacon.exception.LaconOutOfBoundsException;
import ua.nechay.lacon.utils.Pair;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ua.nechay.lacon.core.LaconOperation.CAST;
import static ua.nechay.lacon.core.LaconOperation.GET_BY_INDEX;
import static ua.nechay.lacon.core.LaconOperation.MUL;
import static ua.nechay.lacon.core.LaconValueUtils.determineMapper;
import static ua.nechay.lacon.core.function.MethodName.toMap;

/**
 * @author anechaev
 * @since 23.04.2023
 */
public class ListLaconValue extends LaconValue<List<LaconValue<?>>> {
    private static final Map<String, FunctionLaconValue> METHODS = toMap(
        new Pair<>(LaconMethodName.SIZE, LaconBuiltInSizeMethod.getInstance())
    );

    public ListLaconValue(@Nonnull List<LaconValue<?>> value) {
        super(value, LaconBuiltInType.LIST);
    }

    public static ListLaconValue create(@Nonnull LaconValue<?>... values) {
        return new ListLaconValue(new ArrayList<>(Arrays.asList(values)));
    }

    @Nonnull
    @Override
    public LaconValue<?> plus(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), TypeTouchBuilder
            .create(() -> addElement(this, value))
            .setList(() -> concat(this, (ListLaconValue) value))
            .build());
    }

    public static ListLaconValue concat(@Nonnull ListLaconValue list1, @Nonnull ListLaconValue list2) {
        List<LaconValue<?>> res = list1.getValue();
        res.addAll(list2.getValue());
        return new ListLaconValue(res);
    }

    @Nonnull
    @Override
    public LaconValue<?> minus(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), TypeTouchBuilder
            .create(() -> removeElement(this, value))
            .setList(() -> removeAll(this, (ListLaconValue) value))
            .build());
    }

    public static ListLaconValue removeAll(@Nonnull ListLaconValue list1, @Nonnull ListLaconValue list2) {
        List<LaconValue<?>> res = list1.getValue();
        res.removeAll(list2.getValue());
        return new ListLaconValue(res);
    }

    @Nonnull
    @Override
    public LaconValue<?> mul(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), getDefaultTypeTouchBuilder(MUL, value)
            .setInteger(() -> multiplyList(this, (long) value.getValue()))
            .build());
    }

    @Nonnull
    @Override
    public LaconValue<?> div(@Nonnull LaconValue<?> value) {
        return UnsupportedOperationTypeTouch.unsupported("/", value, getType());
    }

    @Nonnull
    @Override
    public LaconValue<?> getByIndex(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), getDefaultTypeTouchBuilder(GET_BY_INDEX, value)
            .setInteger(() -> getByIndex((int) (long) value.getValue()))
            .build());
    }

    @Override
    public Iterator<LaconValue<?>> iterator() {
        return getValue().iterator();
    }

    private LaconValue<?> getByIndex(int index) {
        List<LaconValue<?>> list = getValue();
        if (Math.abs(index) >= list.size()) {
            throw new LaconOutOfBoundsException("Unable to get element at index " + index + " in list '" + getValue() + "'");
        }
        int ind = index;
        if (index < 0) {
            ind = list.size() + index;
        }
        return list.get(ind);
    }

    @Nonnull
    @Override
    public LaconValue<?> contains(@Nonnull LaconValue<?> value) {
        return new BooleanLaconValue(getValue().stream()
            .anyMatch(val -> val.equals(value)));
    }

    @Nonnull
    @Override
    public LaconValue<?> unaryPlus() {
        return this;
    }

    @Nonnull
    @Override
    public LaconValue<?> unaryMinus() {
        return new ListLaconValue(LaconValueUtils.revert(getValue()));
    }

    @Nonnull
    @Override
    public LaconValue<?> unaryNot() {
        return new BooleanLaconValue(!castToBoolValue(getValue()));
    }

    @Nonnull
    @Override
    public LaconValue<?> castTo(@Nonnull LaconType type) {
        return TypeTouch.touch(type, TypeTouchBuilder.<LaconValue<?>>create(() -> unsupported(CAST, type))
            .setString(() -> new StringLaconValue(castToStrValue(this)))
            .setBool(() -> new BooleanLaconValue(castToBoolValue(this)))
            .setList(() -> this)
            .setFunction(() -> FunctionLaconValue.createSupplier(this))
            .setDict(() -> DictLaconValue.fromList(this))
            .build());
    }

    public static String castToStrValue(@Nonnull LaconValue<?> laconValue) {
        return laconValue.getValue().toString();
    }

    public static boolean castToBoolValue(@Nonnull LaconValue<?> laconValue) {
        if (laconValue.getType() != LaconBuiltInType.LIST) {
            throw new IllegalStateException("Unexpected type: " + laconValue.getType() + ". Expected: " + LaconBuiltInType.LIST);
        }
        return castToBoolValue((List<?>) laconValue.getValue());
    }

    public static boolean castToBoolValue(@Nonnull List<?> objects) {
        return objects.isEmpty();
    }

    public static LaconValue<?> multiplyList(@Nonnull ListLaconValue list, long multiplier) {
        List<LaconValue<?>> valueList = list.getValue();
        return new ListLaconValue(LaconValueUtils.multiply(valueList, multiplier));
    }

    @Nonnull
    public static ListLaconValue addElementAtTheStart(@Nonnull ListLaconValue list, @Nonnull LaconValue<?> value) {
        list.getValue().add(0, value);
        return list;
    }

    @Nonnull
    public static ListLaconValue addElement(@Nonnull ListLaconValue list, @Nonnull LaconValue<?> value) {
        list.getValue().add(value);
        return list;
    }

    @Nonnull
    public static ListLaconValue removeElement(@Nonnull ListLaconValue list, @Nonnull LaconValue<?> value) {
        return new ListLaconValue(list.getValue()
            .stream()
            .filter(elem -> !elem.equals(value))
            .collect(Collectors.toList()));
    }

    @Nonnull
    public static ListLaconValue createFromRangeParams(@Nonnull RangeParams rangeParams) {
        LaconBuiltInType type = rangeParams.getType();
        return createListFromRange(rangeParams, determineMapper(type));
    }

    @Nonnull
    private static ListLaconValue createListFromRange(@Nonnull RangeParams rangeParams, @Nonnull Function<Double, LaconValue<?>> valueMapper) {
        double start = rangeParams.getStart();
        double increment = rangeParams.getIncrement();
        double end = rangeParams.getEnd();
        if (start == end) {
            return new ListLaconValue(Collections.emptyList());
        }
        if (increment < 0) {
            throw new IllegalStateException("Increment should be positive!!");
        }
        List<LaconValue<?>> result = new ArrayList<>();
        if (start < end) { // trivial case
            for (double i = start; i < end; i += increment) {
                result.add(valueMapper.apply(i));
            }
            return new ListLaconValue(result);
        }
        for (double i = end; i > start; i -= increment) {
            result.add(valueMapper.apply(i));
        }
        return new ListLaconValue(result);
    }

    @Override
    public Map<String, FunctionLaconValue> getMethods() {
        return METHODS;
    }
}
