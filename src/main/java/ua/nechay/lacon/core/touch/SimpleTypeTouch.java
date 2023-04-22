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

    private SimpleTypeTouch(
        @Nonnull Supplier<T> integerSupplier,
        @Nonnull Supplier<T> realSupplier,
        @Nonnull Supplier<T> stringSupplier,
        @Nonnull Supplier<T> boolSupplier)
    {
        this.integerSupplier = integerSupplier;
        this.realSupplier = realSupplier;
        this.stringSupplier = stringSupplier;
        this.boolSupplier = boolSupplier;
    }

    public static <T> SimpleTypeTouch<T> create(
        @Nonnull Supplier<T> integerSupplier,
        @Nonnull Supplier<T> doubleSupplier,
        @Nonnull Supplier<T> stringSupplier,
        @Nonnull Supplier<T> boolSupplier)
    {
        return new SimpleTypeTouch<>(
            integerSupplier,
            doubleSupplier,
            stringSupplier,
            boolSupplier
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
}
