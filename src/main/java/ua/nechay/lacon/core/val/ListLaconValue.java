package ua.nechay.lacon.core.val;

import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.touch.UnsupportedOperationTypeTouch;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author anechaev
 * @since 23.04.2023
 */
public class ListLaconValue extends LaconValue<List<Object>> {
    public ListLaconValue(@Nonnull List<Object> value) {
        super(value, LaconBuiltInType.LIST);
    }

    public static ListLaconValue create(@Nonnull Object ... values) {
        return new ListLaconValue(new ArrayList<>(Arrays.asList(values)));
    }

    @Nonnull
    @Override
    public LaconValue<?> plus(@Nonnull LaconValue<?> value) {
        return ListLaconValue.addElement(this, value.getValue());
    }

    @Nonnull
    @Override
    public LaconValue<?> minus(@Nonnull LaconValue<?> value) {
        return ListLaconValue.removeElement(this, value.getValue());
    }

    @Nonnull
    @Override
    public LaconValue<?> mul(@Nonnull LaconValue<?> value) {
        return UnsupportedOperationTypeTouch.unsupported("*", value, getType());
    }

    @Nonnull
    @Override
    public LaconValue<?> div(@Nonnull LaconValue<?> value) {
        return UnsupportedOperationTypeTouch.unsupported("/", value, getType());
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

    public static String castToStrValue(@Nonnull LaconValue<?> laconValue) {
        return laconValue.getValue().toString();
    }

    public static String castToStrValue(@Nonnull List<Object> objects) {
        return objects.toString();
    }

    public static boolean castToBoolValue(@Nonnull LaconValue<?> laconValue) {
        if (laconValue.getType() != LaconBuiltInType.LIST) {
            throw new IllegalStateException("Unexpected type: " + laconValue.getType() + ". Expected: " + LaconBuiltInType.LIST);
        }
        return castToBoolValue((List<Object>) laconValue.getValue());
    }

    public static boolean castToBoolValue(@Nonnull List<Object> objects) {
        return objects.isEmpty();
    }

    public static ListLaconValue addElement(@Nonnull ListLaconValue list, @Nonnull Object object) {
        list.getValue().add(object);
        return list;
    }

    public static ListLaconValue removeElement(@Nonnull ListLaconValue list, @Nonnull Object object) {
        return new ListLaconValue(list.getValue()
            .stream()
            .filter(elem -> !elem.equals(object))
            .collect(Collectors.toList()));
    }
}
