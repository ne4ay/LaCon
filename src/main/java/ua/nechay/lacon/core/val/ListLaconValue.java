package ua.nechay.lacon.core.val;

import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.touch.SimpleTypeTouch;
import ua.nechay.lacon.core.touch.TypeTouch;
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
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> addElement(this, value.getValue()),
            () -> addElement(this, value.getValue()),
            () -> addElement(this, value.getValue()),
            () -> addElement(this, value.getValue()),
            () -> concat(this, (ListLaconValue) value)
        ));
    }

    public static ListLaconValue concat(@Nonnull ListLaconValue list1, @Nonnull ListLaconValue list2) {
        List<Object> res = list1.getValue();
        res.addAll(list2.getValue());
        return new ListLaconValue(res);
    }

    @Nonnull
    @Override
    public LaconValue<?> minus(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> removeElement(this, value.getValue()),
            () -> removeElement(this, value.getValue()),
            () -> removeElement(this, value.getValue()),
            () -> removeElement(this, value.getValue()),
            () -> removeAll(this, (ListLaconValue) value)
        ));
    }

    public static ListLaconValue removeAll(@Nonnull ListLaconValue list1, @Nonnull ListLaconValue list2) {
        List<Object> res = list1.getValue();
        res.removeAll(list2.getValue());
        return new ListLaconValue(res);
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
