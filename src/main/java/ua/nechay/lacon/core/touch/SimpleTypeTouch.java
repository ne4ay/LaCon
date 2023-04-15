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

    private SimpleTypeTouch(
        @Nonnull Supplier<T> integerSupplier,
        @Nonnull Supplier<T> realSupplier)
    {
        this.integerSupplier = integerSupplier;
        this.realSupplier = realSupplier;
    }

    public static <T> SimpleTypeTouch<T> create(
        @Nonnull Supplier<T> integerSupplier,
        @Nonnull Supplier<T> doubleSupplier)
    {
        return new SimpleTypeTouch<>(
            integerSupplier,
            doubleSupplier
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
}
