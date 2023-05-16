package ua.nechay.lacon.utils;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author anechaev
 * @since 07.05.2022
 */
public class Pair<FirstT, SecondT> {
    private final FirstT first;
    private final SecondT second;

    public Pair(@Nonnull FirstT first, @Nonnull SecondT second) {
        this.first = first;
        this.second = second;
    }

    @Nonnull
    public static <FirstT, SecondT> Pair<FirstT, SecondT> of(FirstT first, SecondT second) {
        return new Pair<>(first, second);
    }

    @Nonnull
    public Pair<SecondT, FirstT> swap() {
        return new Pair<>(second, first);
    }

    @Nonnull
    public FirstT getFirst() {
        return first;
    }

    @Nonnull
    public SecondT getSecond() {
        return second;
    }

    @Nonnull
    public <ResultT> Pair<ResultT, SecondT> mapFirst(Function<FirstT, ResultT> firstFunction) {
        return Pair.of(firstFunction.apply(first), second);
    }

    @Nonnull
    public <ResultT> Pair<FirstT, ResultT> mapSecond(Function<SecondT, ResultT> secondFunction) {
        return Pair.of(first, secondFunction.apply(second));
    }

    @Nonnull
    public <ResultT> ResultT merge(BiFunction<FirstT, SecondT, ResultT> mergeFunction) {
        return mergeFunction.apply(first, second);
    }

    @SafeVarargs
    @Nonnull
    public static <L, R> Map<L, R> toMap(@Nonnull Pair<L, R> ... pairs) {
        return toMap(Arrays.asList(pairs));
    }

    @Nonnull
    public static <L, R> Map<L, R> toMap(@Nonnull List<Pair<L, R>> pairs) {
        return pairs.stream()
            .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
    }


    @Override
    public String toString() {
        return "(" + getFirst() + "," + getSecond() + ")";
    }
}