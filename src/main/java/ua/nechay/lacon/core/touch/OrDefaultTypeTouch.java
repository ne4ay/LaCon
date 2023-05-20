package ua.nechay.lacon.core.touch;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author anechaev
 * @since 19.05.2023
 */
public class OrDefaultTypeTouch<T> implements TypeTouch<T> {

    private final Supplier<T> defaultSupplier;
    @Nullable private final Supplier<T> integerSupplier;
    @Nullable private final Supplier<T> realSupplier;
    @Nullable private final Supplier<T> stringSupplier;
    @Nullable private final Supplier<T> boolSupplier;
    @Nullable private final Supplier<T> listSupplier;
    @Nullable private final Supplier<T> functionSupplier;
    @Nullable private final Supplier<T> dictSupplier;
    @Nullable private final Supplier<T> notBuiltInSupplier;

    OrDefaultTypeTouch(@Nonnull Supplier<T> defaultSupplier, @Nullable Supplier<T> integerSupplier, @Nullable Supplier<T> realSupplier,
        @Nullable Supplier<T> stringSupplier, @Nullable Supplier<T> boolSupplier, @Nullable Supplier<T> listSupplier,
        @Nullable Supplier<T> functionSupplier, @Nullable Supplier<T> dictSupplier, @Nullable Supplier<T> notBuiltInSupplier) {
        this.defaultSupplier = defaultSupplier;
        this.integerSupplier = integerSupplier;
        this.realSupplier = realSupplier;
        this.stringSupplier = stringSupplier;
        this.boolSupplier = boolSupplier;
        this.listSupplier = listSupplier;
        this.functionSupplier = functionSupplier;
        this.dictSupplier = dictSupplier;
        this.notBuiltInSupplier = notBuiltInSupplier;
    }

    private T getOrDefault(@Nullable Supplier<T> supplier) {
        return Optional.ofNullable(supplier)
            .orElse(defaultSupplier)
            .get();
    }

    @Override
    public T integer() {
        return getOrDefault(integerSupplier);
    }

    @Override
    public T real() {
        return getOrDefault(realSupplier);
    }

    @Override
    public T string() {
        return getOrDefault(stringSupplier);
    }

    @Override
    public T bool() {
        return getOrDefault(boolSupplier);
    }

    @Override
    public T list() {
        return getOrDefault(listSupplier);
    }

    @Override
    public T function() {
        return getOrDefault(functionSupplier);
    }

    @Override
    public T dict() {
        return getOrDefault(dictSupplier);
    }

    @Override
    public T notBuiltIn() {
        return getOrDefault(notBuiltInSupplier);
    }
}
