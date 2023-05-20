package ua.nechay.lacon.core.function.built.regex;

import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconType;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.function.FunctionLaconValue;
import ua.nechay.lacon.core.function.LaconFunctionArgument;
import ua.nechay.lacon.core.val.ListLaconValue;
import ua.nechay.lacon.core.val.StringLaconValue;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ua.nechay.lacon.core.function.LaconFunctionUtils.getMandatoryArgument;

/**
 * @author anechaev
 * @since 18.05.2023
 */
public class LaconBuiltInFindAllFunction extends FunctionLaconValue {
    private static final String REGEX_ARGUMENT = "regex";
    private static final String TEXT_ARGUMENT = "text";

    private static final LaconBuiltInFindAllFunction INSTANCE = new LaconBuiltInFindAllFunction(List.of(
        LaconFunctionArgument.create(REGEX_ARGUMENT, LaconBuiltInType.STRING),
        LaconFunctionArgument.create(TEXT_ARGUMENT, LaconBuiltInType.STRING)
    ),
        state -> {
            LaconValue<?> regexVal = getMandatoryArgument(state, REGEX_ARGUMENT, LaconBuiltInType.STRING);
            String regex = (String) regexVal.getValue();
            LaconValue<?> textVal = getMandatoryArgument(state, TEXT_ARGUMENT, LaconBuiltInType.STRING);
            String text = (String) textVal.getValue();

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);
            List<LaconValue<?>> result = new ArrayList<>();
            while (matcher.find()) {
                result.add(new StringLaconValue(matcher.group()));
            }
            return new ListLaconValue(result);
        },
        LaconBuiltInType.LIST
    );


    public LaconBuiltInFindAllFunction(@Nonnull List<LaconFunctionArgument> args,
        @Nonnull Function<LaconProgramState, LaconValue<?>> function,
        @Nonnull LaconType returnType)
    {
        super(args, function, returnType);
    }

    public static LaconBuiltInFindAllFunction getInstance() {
        return INSTANCE;
    }
}
