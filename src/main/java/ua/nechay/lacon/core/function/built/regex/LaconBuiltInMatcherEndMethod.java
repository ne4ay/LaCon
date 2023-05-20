package ua.nechay.lacon.core.function.built.regex;

import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconType;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.function.FunctionLaconValue;
import ua.nechay.lacon.core.function.LaconFunctionArgument;
import ua.nechay.lacon.core.val.IntLaconValue;
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
public class LaconBuiltInMatcherEndMethod extends FunctionLaconValue {
    private static final String GROUP_ARGUMENT = "group";

    private static final LaconBuiltInMatcherEndMethod INSTANCE = new LaconBuiltInMatcherEndMethod(List.of(
        LaconFunctionArgument.create(GROUP_ARGUMENT, LaconBuiltInType.INT)
    ), state -> {
        MatchLaconValue value = (MatchLaconValue)state.popValue();
        Matcher matcher = value.getValue();

        LaconValue<?> groupVal = getMandatoryArgument(state, GROUP_ARGUMENT, LaconBuiltInType.INT);
        long group = (Long) groupVal.getValue();
        try {
            return new IntLaconValue(matcher.end((int) group));
        } catch (Exception e) {
            return new IntLaconValue(-1);
        }
    }, LaconBuiltInType.INT);


    public LaconBuiltInMatcherEndMethod(@Nonnull List<LaconFunctionArgument> args,
        @Nonnull Function<LaconProgramState, LaconValue<?>> function,
        @Nonnull LaconType returnType)
    {
        super(args, function, returnType);
    }

    public static LaconBuiltInMatcherEndMethod getInstance() {
        return INSTANCE;
    }
}
