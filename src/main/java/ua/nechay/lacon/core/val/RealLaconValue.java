package ua.nechay.lacon.core.val;

import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconOperation;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.touch.SimpleTypeTouch;
import ua.nechay.lacon.core.touch.TypeTouch;

import javax.annotation.Nonnull;

import static ua.nechay.lacon.core.LaconOperation.DIV;
import static ua.nechay.lacon.core.LaconOperation.MINUS;
import static ua.nechay.lacon.core.LaconOperation.MODULUS;
import static ua.nechay.lacon.core.LaconOperation.MUL;
import static ua.nechay.lacon.core.LaconOperation.PLUS;
import static ua.nechay.lacon.exception.LaconUnsupportedOperationException.unsupportedOperation;

/**
 * @author anechaev
 * @since 15.04.2023
 */
public class RealLaconValue extends LaconValue<Double> {
    public RealLaconValue(double value) {
        super(value, LaconBuiltInType.REAL);
    }

    @Nonnull @Override
    public LaconValue<?> plus(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> new RealLaconValue(getValue() + IntLaconValue.castToReal((long)value.getValue())),
            () -> new RealLaconValue(getValue() + (double) value.getValue()),
            () -> new StringLaconValue(getValue() + (String) value.getValue()),
            () -> unsupported(PLUS, LaconBuiltInType.BOOLEAN),
            () -> ListLaconValue.addElementAtTheStart((ListLaconValue)value.getValue(),this),
            () -> unsupported(PLUS, LaconBuiltInType.FUNCTION)
        ));
    }

    @Nonnull @Override
    public LaconValue<?> minus(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> new RealLaconValue(getValue() - IntLaconValue.castToReal((long)value.getValue())),
            () -> new RealLaconValue(getValue() - (double) value.getValue()),
            () -> unsupported(MINUS, LaconBuiltInType.STRING),
            () -> unsupported(MINUS, LaconBuiltInType.BOOLEAN),
            () -> ListLaconValue.addElement((ListLaconValue)value.getValue(), this),
            () -> unsupported(MINUS, LaconBuiltInType.FUNCTION)
        ));
    }

    @Nonnull @Override
    public LaconValue<?> mul(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> new RealLaconValue(getValue() * IntLaconValue.castToReal((long)value.getValue())),
            () -> new RealLaconValue(getValue() * (double) value.getValue()),
            () -> unsupported(MUL, LaconBuiltInType.STRING),
            () -> unsupported(MUL, LaconBuiltInType.BOOLEAN),
            () -> unsupported(MUL, LaconBuiltInType.LIST),
            () -> unsupported(MUL, LaconBuiltInType.FUNCTION)
        ));
    }

    @Nonnull @Override
    public LaconValue<?> div(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> new RealLaconValue(getValue() / IntLaconValue.castToReal((long)value.getValue())),
            () -> new RealLaconValue(getValue() / (double) value.getValue()),
            () -> unsupported(DIV, LaconBuiltInType.STRING),
            () -> unsupported(DIV, LaconBuiltInType.BOOLEAN),
            () -> unsupported(DIV, LaconBuiltInType.LIST),
            () -> unsupported(DIV, LaconBuiltInType.FUNCTION)
        ));
    }

    @Nonnull
    @Override
    public LaconValue<?> modulus(@Nonnull LaconValue<?> value) {
        return TypeTouch.touch(value.getType(), SimpleTypeTouch.create(
            () -> new RealLaconValue(getValue() % IntLaconValue.castToReal((long)value.getValue())),
            () -> new RealLaconValue(getValue() % (double) value.getValue()),
            () -> unsupported(MODULUS, LaconBuiltInType.STRING),
            () -> unsupported(MODULUS, LaconBuiltInType.BOOLEAN),
            () -> unsupported(MODULUS, LaconBuiltInType.LIST),
            () -> unsupported(MODULUS, LaconBuiltInType.FUNCTION)
        ));
    }

    @Override
    protected int compare(Double obj1, Double obj2) {
        return Double.compare(obj1, obj2);
    }

    @Nonnull @Override
    public LaconValue<?> unaryPlus() {
        return new RealLaconValue(+getValue());
    }

    @Nonnull @Override
    public LaconValue<?> unaryMinus() {
        return new RealLaconValue(-getValue());
    }

    @Nonnull
    @Override
    public LaconValue<?> unaryNot() {
        return unsupportedOperation("!", getType().getRepresentation());
    }

    public static long castToInt(double value) {
        return (long) value;
    }
}
