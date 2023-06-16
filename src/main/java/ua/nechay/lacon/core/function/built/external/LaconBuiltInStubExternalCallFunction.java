package ua.nechay.lacon.core.function.built.external;

import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconType;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.function.FunctionLaconValue;
import ua.nechay.lacon.core.function.LaconFunctionArgument;
import ua.nechay.lacon.core.function.LaconFunctionUtils;
import ua.nechay.lacon.core.val.StringLaconValue;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Function;

/**
 * @author anechaev
 * @since 22.05.2023
 */
public class LaconBuiltInStubExternalCallFunction extends FunctionLaconValue {
    private static final String TEXT_ARGUMENT_NAME = "text";
    private static final LaconFunctionArgument TEXT_ARGUMENT = LaconFunctionArgument.create(TEXT_ARGUMENT_NAME, LaconBuiltInType.STRING);

    private static final LaconBuiltInStubExternalCallFunction INSTANCE = new LaconBuiltInStubExternalCallFunction(List.of(
        TEXT_ARGUMENT),
        state -> {
            LaconValue<?> textVal = LaconFunctionUtils.getMandatoryArgument(state, TEXT_ARGUMENT_NAME, LaconBuiltInType.STRING);
            String callText = (String) textVal.getValue();
            return new StringLaconValue(callText);
        }, LaconBuiltInType.STRING
    );

    public LaconBuiltInStubExternalCallFunction(@Nonnull List<LaconFunctionArgument> args,
        @Nonnull Function<LaconProgramState, LaconValue<?>> function,
        @Nonnull LaconType returnType)
    {
        super(args, function, returnType);
    }

    public static LaconBuiltInStubExternalCallFunction getInstance() {
        return INSTANCE;
    }
}