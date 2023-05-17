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
}
