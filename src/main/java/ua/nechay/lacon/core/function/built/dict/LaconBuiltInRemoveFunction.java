package ua.nechay.lacon.core.function.built.dict;

import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconType;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.function.FunctionLaconValue;
import ua.nechay.lacon.core.function.LaconFunctionArgument;
import ua.nechay.lacon.core.val.DictLaconValue;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Function;

import static ua.nechay.lacon.core.function.LaconFunctionUtils.getMandatoryArgument;

/**
 * @author anechaev
 * @since 18.05.2023
 */
public class LaconBuiltInRemoveFunction extends FunctionLaconValue {
    private static final String KEY_ARGUMENT = "key";

    private static final LaconBuiltInRemoveFunction INSTANCE = new LaconBuiltInRemoveFunction(List.of(
        LaconFunctionArgument.create(KEY_ARGUMENT, LaconBuiltInType.ANY)
    ), state -> {
        DictLaconValue value = (DictLaconValue)state.popValue();
        LaconValue<?> keyVal = getMandatoryArgument(state, KEY_ARGUMENT, LaconBuiltInType.ANY);
        LaconValue<?> removedValue = value.getValue().remove(keyVal);
        if (removedValue == null) {
            throw new IllegalStateException("Unable to remove non-existing value from dict! Key:" + keyVal);
        }
        return removedValue;
    }, LaconBuiltInType.ANY);

    public LaconBuiltInRemoveFunction(@Nonnull List<LaconFunctionArgument> args,
        @Nonnull Function<LaconProgramState, LaconValue<?>> function,
        @Nonnull LaconType returnType)
    {
        super(args, function, returnType);
    }

    public static LaconBuiltInRemoveFunction getInstance() {
        return INSTANCE;
    }
}
