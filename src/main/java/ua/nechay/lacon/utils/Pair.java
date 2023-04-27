package ua.nechay.lacon.utils;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
}