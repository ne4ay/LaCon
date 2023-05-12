package ua.nechay.lacon.core.val;

import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconOperation;
import ua.nechay.lacon.core.LaconValue;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 11.05.2023
 */
public class VoidLaconValue extends LaconValue<Object> {
    private static final VoidLaconValue VOID_VALUE = new VoidLaconValue();

    private VoidLaconValue() {
        super(0L, LaconBuiltInType.VOID);
    }

    public static VoidLaconValue create() {
        return VOID_VALUE;
    }

    @Nonnull
    @Override
    public LaconValue<?> plus(@Nonnull LaconValue<?> value) {
        return unsupported(LaconOperation.PLUS, LaconBuiltInType.VOID);
    }

    @Nonnull
    @Override
    public LaconValue<?> minus(@Nonnull LaconValue<?> value) {
        return unsupported(LaconOperation.MINUS, LaconBuiltInType.VOID);
    }

    @Nonnull
    @Override
    public LaconValue<?> mul(@Nonnull LaconValue<?> value) {
        return unsupported(LaconOperation.MUL, LaconBuiltInType.VOID);
    }

    @Nonnull
    @Override
    public LaconValue<?> div(@Nonnull LaconValue<?> value) {
        return unsupported(LaconOperation.DIV, LaconBuiltInType.VOID);
    }
}
