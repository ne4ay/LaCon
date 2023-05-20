package ua.nechay.lacon.core.val.regex;

import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.function.FunctionLaconValue;
import ua.nechay.lacon.core.function.LaconMethodName;
import ua.nechay.lacon.core.function.built.regex.LaconBuiltInMatcherEndMethod;
import ua.nechay.lacon.core.function.built.regex.LaconBuiltInMatcherGroupMethod;
import ua.nechay.lacon.core.function.built.regex.LaconBuiltInMatcherStartMethod;
import ua.nechay.lacon.core.type.RegexType;
import ua.nechay.lacon.utils.Pair;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.regex.Matcher;

import static ua.nechay.lacon.core.function.MethodName.toMap;

/**
 * @author anechaev
 * @since 19.05.2023
 */
public class MatchLaconValue extends LaconValue<Matcher> {
    private static final Map<String, FunctionLaconValue> METHODS = toMap(
        new Pair<>(LaconMethodName.START, LaconBuiltInMatcherStartMethod.getInstance()),
        new Pair<>(LaconMethodName.END, LaconBuiltInMatcherEndMethod.getInstance()),
        new Pair<>(LaconMethodName.NEXT_GROUP, LaconBuiltInMatcherGroupMethod.getInstance())
    );

    public MatchLaconValue(@Nonnull Matcher value) {
        super(value, RegexType.MATCH);
    }

    @Override
    public Map<String, FunctionLaconValue> getMethods() {
        return METHODS;
    }
}
