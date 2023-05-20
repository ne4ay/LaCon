package ua.nechay.lacon.core.function;

import ua.nechay.lacon.utils.Pair;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author anechaev
 * @since 19.05.2023
 */
public interface MethodName {
    @SafeVarargs
    static Map<String, FunctionLaconValue> toMap(@Nonnull Pair<MethodName, FunctionLaconValue>... pairs) {
        return Arrays.stream(pairs)
            .map(pair -> pair.mapFirst(MethodName::getRepresentation))
            .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
    }

    @Nonnull
    String getRepresentation();
}
