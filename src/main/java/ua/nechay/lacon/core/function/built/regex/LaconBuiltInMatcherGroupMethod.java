package ua.nechay.lacon.core.function.built.regex;

import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconType;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.function.FunctionLaconValue;
import ua.nechay.lacon.core.function.LaconFunctionArgument;
import ua.nechay.lacon.core.val.StringLaconValue;
import ua.nechay.lacon.core.val.regex.MatchLaconValue;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;

import static ua.nechay.lacon.core.function.LaconFunctionUtils.getMandatoryArgument;

/**
 * @author anechaev
 * @since 19.05.2023
 */
public class LaconBuiltInMatcherGroupMethod extends FunctionLaconValue {
    private static final String GROUP_ARGUMENT = "group";

    private static final LaconBuiltInMatcherGroupMethod INSTANCE = new LaconBuiltInMatcherGroupMethod(List.of(
        LaconFunctionArgument.create(GROUP_ARGUMENT, LaconBuiltInType.INT)
    ), state -> {
        MatchLaconValue value = (MatchLaconValue)state.popValue();
        Matcher matcher = value.getValue();

        LaconValue<?> groupVal = getMandatoryArgument(state, GROUP_ARGUMENT, LaconBuiltInType.INT);
        long group = (Long) groupVal.getValue();
        if (!matcher.find()) {
            return new StringLaconValue("");
        }
        try {
            return new StringLaconValue(matcher.group((int) group));
        } catch (Exception e) {
            return new StringLaconValue("");
        }
    }, LaconBuiltInType.STRING);


    public LaconBuiltInMatcherGroupMethod(@Nonnull List<LaconFunctionArgument> args,
        @Nonnull Function<LaconProgramState, LaconValue<?>> function,
        @Nonnull LaconType returnType)
    {
        super(args, function, returnType);
    }

    @Nonnull
    public static LaconBuiltInMatcherGroupMethod getInstance() {
        return INSTANCE;
    }
}
