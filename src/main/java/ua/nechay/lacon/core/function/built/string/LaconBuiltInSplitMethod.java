package ua.nechay.lacon.core.function.built.string;

import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconType;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.function.FunctionLaconValue;
import ua.nechay.lacon.core.function.LaconFunctionArgument;
import ua.nechay.lacon.core.val.ListLaconValue;
import ua.nechay.lacon.core.val.StringLaconValue;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ua.nechay.lacon.core.function.LaconFunctionUtils.getMandatoryArgument;

/**
 * @author anechaev
 * @since 16.05.2023
 */
public class LaconBuiltInSplitMethod extends FunctionLaconValue {
    private static final String REGEX_ARGUMENT = "regex";

    private static final LaconBuiltInSplitMethod INSTANCE = new LaconBuiltInSplitMethod(
        List.of(
            LaconFunctionArgument.create(REGEX_ARGUMENT, LaconBuiltInType.STRING)
        ),
        state -> {
            StringLaconValue value = (StringLaconValue)state.popValue();
            LaconValue<?> regexVal = getMandatoryArgument(state, REGEX_ARGUMENT, LaconBuiltInType.STRING);
            String regex = (String)regexVal.getValue();
            return new ListLaconValue(splitString(value.getValue(), regex));
        }, LaconBuiltInType.LIST
    );

    private static List<LaconValue<?>> splitString(@Nonnull String str, @Nonnull String regex) {
        return Arrays.stream(str.split(regex))
            .map(StringLaconValue::new)
            .collect(Collectors.toList());
    }

    public LaconBuiltInSplitMethod(@Nonnull List<LaconFunctionArgument> args,
        @Nonnull Function<LaconProgramState, LaconValue<?>> function,
        @Nonnull LaconType returnType)
    {
        super(args, function, returnType);
    }

    public static LaconBuiltInSplitMethod getInstance() {
        return INSTANCE;
    }
}
