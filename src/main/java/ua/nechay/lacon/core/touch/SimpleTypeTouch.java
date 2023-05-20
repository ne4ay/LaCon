package ua.nechay.lacon.core.touch;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/**
 * @author anechaev
 * @since 16.04.2023
 */
public class SimpleTypeTouch<T> implements TypeTouch<T> {

    private final Supplier<T> integerSupplier;
    private final Supplier<T> realSupplier;
    private final Supplier<T> stringSupplier;
    private final Supplier<T> boolSupplier;
    private final Supplier<T> listSupplier;
    private final Supplier<T> functionSupplier;
    private final Supplier<T> dictSupplier;
    private final Supplier<T> notBuiltInSupplier;

    private SimpleTypeTouch(
        @Nonnull Supplier<T> integerSupplier,
        @Nonnull Supplier<T> realSupplier,
        @Nonnull Supplier<T> stringSupplier,
        @Nonnull Supplier<T> boolSupplier,
        @Nonnull Supplier<T> listSupplier,
        @Nonnull Supplier<T> functionSupplier,
        @Nonnull Supplier<T> dictSupplier,
        @Nonnull Supplier<T> notBuiltInSupplier)
    {
        this.integerSupplier = integerSupplier;
        this.realSupplier = realSupplier;
        this.stringSupplier = stringSupplier;
        this.boolSupplier = boolSupplier;
        this.listSupplier = listSupplier;
        this.functionSupplier = functionSupplier;
        this.dictSupplier = dictSupplier;
        this.notBuiltInSupplier = notBuiltInSupplier;
    }

    public static <T> SimpleTypeTouch<T> create(
        @Nonnull Supplier<T> integerSupplier,
        @Nonnull Supplier<T> doubleSupplier,
        @Nonnull Supplier<T> stringSupplier,
        @Nonnull Supplier<T> boolSupplier,
        @Nonnull Supplier<T> listSupplier,
        @Nonnull Supplier<T> functionSupplier,
        @Nonnull Supplier<T> dictSupplier,
        @Nonnull Supplier<T> notBuiltInSupplier)
    {
        return new SimpleTypeTouch<>(
            integerSupplier,
            doubleSupplier,
            stringSupplier,
            boolSupplier,
            listSupplier,
            functionSupplier,
            dictSupplier,
            notBuiltInSupplier
        );
    }

    @Override
    public T integer() {
        return integerSupplier.get();
    }

    @Override
    public T real() {
        return realSupplier.get();
    }

    @Override
    public T string() {
        return stringSupplier.get();
    }

    @Override
    public T bool() {
        return boolSupplier.get();
    }

    @Override
    public T list() {
        return listSupplier.get();
    }

    @Override
    public T function() {
        return functionSupplier.get();
    }

    @Override
    public T dict() {
        return dictSupplier.get();
    }

    @Override
    public T notBuiltIn() {
        return notBuiltInSupplier.get();
    }
}
