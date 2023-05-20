package ua.nechay.lacon.core.function.built;

import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconType;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.function.FunctionLaconValue;
import ua.nechay.lacon.core.function.LaconFunctionArgument;
import ua.nechay.lacon.core.function.LaconMethodName;
import ua.nechay.lacon.core.touch.TypeTouch;
import ua.nechay.lacon.core.touch.TypeTouchBuilder;
import ua.nechay.lacon.core.val.DictLaconValue;
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
public class LaconBuiltInSizeMethod extends FunctionLaconValue {

    private static final LaconBuiltInSizeMethod INSTANCE = new LaconBuiltInSizeMethod(Collections.emptyList(),
        state -> { // just to not rewrite the whole function for each type)))
            LaconValue<?> callableVal = state.popValue();
            LaconType type = callableVal.getType();
            return TypeTouch.touch(type, TypeTouchBuilder.<LaconValue<?>>create(() -> unsupported(type))
                .setString(() -> new IntLaconValue(((StringLaconValue) callableVal).getValue().length()))
                .setList(() -> new IntLaconValue(((ListLaconValue) callableVal).getValue().size()))
                .setDict(() -> new IntLaconValue(((DictLaconValue) callableVal).getValue().size()))
                .build());
        }, LaconBuiltInType.INT);

    private static LaconValue<?> unsupported(LaconType type) {
        return unsupportedOperation(LaconMethodName.SIZE.getRepresentation(), type);
    }

    public LaconBuiltInSizeMethod(@Nonnull List<LaconFunctionArgument> args,
        @Nonnull Function<LaconProgramState, LaconValue<?>> function,
        @Nonnull LaconType returnType)
    {
        super(args, function, returnType);
    }

    @Nonnull
    public static LaconBuiltInSizeMethod getInstance() {
        return INSTANCE;
    }
}
