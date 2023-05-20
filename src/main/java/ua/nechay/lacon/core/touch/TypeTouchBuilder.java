package ua.nechay.lacon.core.touch;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/**
 * @author anechaev
 * @since 19.05.2023
 */
public class TypeTouchBuilder<T> {
    private final Supplier<T> defaultSupplier;

    private Supplier<T> integer;
    private Supplier<T> real;
    private Supplier<T> string;
    private Supplier<T> bool;
    private Supplier<T> list;
    private Supplier<T> function;
    private Supplier<T> dict;
    private Supplier<T> notBuiltIn;

    TypeTouchBuilder(@Nonnull Supplier<T> defaultSupplier) {
        this.defaultSupplier = defaultSupplier;
    }

    @Nonnull
    public static <T> TypeTouchBuilder<T> create(@Nonnull Supplier<T> defaultSupplier) {
        return new TypeTouchBuilder<>(defaultSupplier);
    }

    @Nonnull
    public TypeTouchBuilder<T> setInteger(Supplier<T> integer) {
        this.integer = integer;
        return this;
    }

    @Nonnull
    public TypeTouchBuilder<T> setReal(Supplier<T> real) {
        this.real = real;
        return this;
    }

    @Nonnull
    public TypeTouchBuilder<T> setString(Supplier<T> string) {
        this.string = string;
        return this;
    }

    @Nonnull
    public TypeTouchBuilder<T> setBool(Supplier<T> bool) {
        this.bool = bool;
        return this;
    }

    @Nonnull
    public TypeTouchBuilder<T> setList(Supplier<T> list) {
        this.list = list;
        return this;
    }

    @Nonnull
    public TypeTouchBuilder<T> setFunction(Supplier<T> function) {
        this.function = function;
        return this;
    }

    @Nonnull
    public TypeTouchBuilder<T> setDict(Supplier<T> dict) {
        this.dict = dict;
        return this;
    }

    @Nonnull
    public TypeTouchBuilder<T> setNotBuiltIn(Supplier<T> notBuiltIn) {
        this.notBuiltIn = notBuiltIn;
        return this;
    }

    @Nonnull
    public OrDefaultTypeTouch<T> build() {
        return new OrDefaultTypeTouch<>(defaultSupplier, integer, real, string, bool, list, function, dict, notBuiltIn);
    }
}
