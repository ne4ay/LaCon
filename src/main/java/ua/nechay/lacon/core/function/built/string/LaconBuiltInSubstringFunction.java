package ua.nechay.lacon.core.function.built.string;

import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconType;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.function.FunctionLaconValue;
import ua.nechay.lacon.core.function.LaconFunctionArgument;
import ua.nechay.lacon.core.function.built.LaconBuiltInPrintlnFunction;
import ua.nechay.lacon.core.function.built.LaconBuiltInSizeFunction;
import ua.nechay.lacon.core.val.IntLaconValue;
import ua.nechay.lacon.core.val.StringLaconValue;
import ua.nechay.lacon.core.var.LaconVariable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Function;

import static ua.nechay.lacon.core.function.LaconFunctionUtils.getMandatoryArgument;

/**
 * @author anechaev
 * @since 16.05.2023
 */
public class LaconBuiltInSubstringFunction extends FunctionLaconValue {
    private static final String BEGIN_ARGUMENT = "begin";
    private static final String END_ARGUMENT = "end";

    private static final LaconBuiltInSubstringFunction INSTANCE = new LaconBuiltInSubstringFunction(
        List.of(
            LaconFunctionArgument.create(BEGIN_ARGUMENT, LaconBuiltInType.INT),
            LaconFunctionArgument.create(END_ARGUMENT, LaconBuiltInType.INT)
        ),
        state -> {
            StringLaconValue value = (StringLaconValue)state.popValue();
            LaconValue<?> beginVal = getMandatoryArgument(state, BEGIN_ARGUMENT, LaconBuiltInType.INT);
            LaconValue<?> endVal = getMandatoryArgument(state, END_ARGUMENT, LaconBuiltInType.INT);
            int begin = (int)(long)beginVal.getValue();
            int end = (int)(long)endVal.getValue();
            return new StringLaconValue(value.getValue().substring(begin, end));
        },
        LaconBuiltInType.STRING

    );

    public LaconBuiltInSubstringFunction(@Nonnull List<LaconFunctionArgument> args,
        @Nonnull Function<LaconProgramState, LaconValue<?>> function,
        @Nonnull LaconType returnType)
    {
        super(args, function, returnType);
    }

    @Nonnull
    public static LaconBuiltInSubstringFunction getInstance() {
        return INSTANCE;
    }
}
