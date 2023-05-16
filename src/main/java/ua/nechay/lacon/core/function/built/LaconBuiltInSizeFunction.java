package ua.nechay.lacon.core.function.built;

import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconType;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.function.FunctionLaconValue;
import ua.nechay.lacon.core.function.LaconFunctionArgument;
import ua.nechay.lacon.core.function.LaconMethodName;
import ua.nechay.lacon.core.function.built.string.LaconBuiltInSubstringFunction;
import ua.nechay.lacon.core.touch.SimpleTypeTouch;
import ua.nechay.lacon.core.touch.TypeTouch;
import ua.nechay.lacon.core.val.IntLaconValue;
import ua.nechay.lacon.core.val.ListLaconValue;
import ua.nechay.lacon.core.val.StringLaconValue;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static ua.nechay.lacon.exception.LaconUnsupportedOperationException.unsupportedOperation;

/**
 * @author anechaev
 * @since 16.05.2023
 */
public class LaconBuiltInSizeFunction extends FunctionLaconValue {

    private static final LaconBuiltInSizeFunction INSTANCE = new LaconBuiltInSizeFunction(Collections.emptyList(),
        state -> {
            LaconValue<?> callableVal = state.popValue();
            return TypeTouch.touch(callableVal.getType(), SimpleTypeTouch.create(
                () -> unsupported(LaconBuiltInType.INT),
                () -> unsupported(LaconBuiltInType.REAL),
                () -> new IntLaconValue(((StringLaconValue) callableVal).getValue().length()),
                () -> unsupported(LaconBuiltInType.BOOLEAN),
                () -> new IntLaconValue(((ListLaconValue) callableVal).getValue().size()),
                () -> unsupported(LaconBuiltInType.FUNCTION)
            ));
        }, LaconBuiltInType.INT);

    private static LaconValue<?> unsupported(LaconType type) {
        return unsupportedOperation(LaconMethodName.SIZE.getRepresentation(), type);
    }

    public LaconBuiltInSizeFunction(@Nonnull List<LaconFunctionArgument> args,
        @Nonnull Function<LaconProgramState, LaconValue<?>> function,
        @Nonnull LaconType returnType)
    {
        super(args, function, returnType);
    }

    @Nonnull
    public static LaconBuiltInSizeFunction getInstance() {
        return INSTANCE;
    }
}