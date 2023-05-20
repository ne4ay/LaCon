package ua.nechay.lacon.core.val;

import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconType;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.function.FunctionLaconValue;
import ua.nechay.lacon.core.function.LaconMethodName;
import ua.nechay.lacon.core.function.built.LaconBuiltInSizeMethod;
import ua.nechay.lacon.core.function.built.dict.LaconBuiltInPutMethod;
import ua.nechay.lacon.core.function.built.dict.LaconBuiltInRemoveMethod;
import ua.nechay.lacon.core.touch.TypeTouch;
import ua.nechay.lacon.core.touch.TypeTouchBuilder;
import ua.nechay.lacon.utils.Pair;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ua.nechay.lacon.core.LaconOperation.CAST;
import static ua.nechay.lacon.core.function.MethodName.toMap;

/**
 * @author anechaev
 * @since 07.05.2023
 */
public class DictLaconValue extends LaconValue<Map<LaconValue<?>, LaconValue<?>>> {
    private static final Map<String, FunctionLaconValue> METHODS = toMap(
        new Pair<>(LaconMethodName.SIZE, LaconBuiltInSizeMethod.getInstance()),
        new Pair<>(LaconMethodName.PUT, LaconBuiltInPutMethod.getInstance()),
        new Pair<>(LaconMethodName.REMOVE, LaconBuiltInRemoveMethod.getInstance())
    );

    public DictLaconValue(@Nonnull Map<LaconValue<?>, LaconValue<?>> value) {
        super(value, LaconBuiltInType.DICT);
    }

    @Nonnull
    @Override
    public LaconValue<?> getByIndex(@Nonnull LaconValue<?> value) {
        LaconValue<?> val = getValue().get(value);
        if (val == null) {
            throw new IllegalStateException("Unknown value in map. There is no such key: " + value);
        }
        return val;
    }

    @Nonnull
    @Override
    public LaconValue<?> contains(@Nonnull LaconValue<?> value) {
        if (!value.getType().equals(LaconBuiltInType.LIST)) {
            return new BooleanLaconValue(false);
        }
        List<LaconValue<?>> listValue = ((ListLaconValue)value).getValue();
        if (listValue.size() != 2) {
            return new BooleanLaconValue(false);
        }
        return new BooleanLaconValue(getValue()
            .entrySet()
            .stream()
            .anyMatch(entry -> entry.getKey().equals(listValue.get(0))
                && entry.getValue().equals(listValue.get(1))
            ));
    }

    @Nonnull
    @Override
    public LaconValue<?> castTo(@Nonnull LaconType type) {
        return TypeTouch.touch(type, TypeTouchBuilder.<LaconValue<?>>create(() -> unsupported(CAST, type))
            .setString(() -> new StringLaconValue(getValue().toString()))
            .setList(() -> toList(this))
            .setFunction(() -> FunctionLaconValue.createSupplier(this))
            .setDict(() -> this)
            .build());
    }

    @Override
    public Map<String, FunctionLaconValue> getMethods() {
        return METHODS;
    }

    @Nonnull
    public static ListLaconValue toList(@Nonnull DictLaconValue dictLaconValue) {
        return new ListLaconValue(dictLaconValue.getValue()
            .entrySet()
            .stream()
            .map(entry -> ListLaconValue.create(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList()));
    }

    public static DictLaconValue fromList(@Nonnull ListLaconValue laconValue) {
        List<LaconValue<?>> laconValueList = laconValue.getValue();
        Map<LaconValue<?>, LaconValue<?>> resultMap = new HashMap<>();
        for (var nextValue : laconValueList) {
            if (!nextValue.getType().equals(LaconBuiltInType.LIST)) {
                throw new IllegalStateException("Unable to cast list of non-list elements into map!");
            }
            List<LaconValue<?>> nextList = ((ListLaconValue)nextValue).getValue();
            if (nextList.size() != 2) {
                throw new IllegalStateException("Unable to cast list " + nextList + " to an entry of dictionary!");
            }
            resultMap.put(nextList.get(0), nextList.get(1));
        }
        return new DictLaconValue(resultMap);
    }
}
