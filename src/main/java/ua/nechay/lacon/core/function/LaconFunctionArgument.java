package ua.nechay.lacon.core.function;

import ua.nechay.lacon.core.LaconType;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * @author anechaev
 * @since 03.05.2023
 */
public class LaconFunctionArgument {
    private final String name;
    private final LaconType type;

    public LaconFunctionArgument(String name, LaconType type) {
        this.name = name;
        this.type = type;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public LaconType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof LaconFunctionArgument))
            return false;
        LaconFunctionArgument that = (LaconFunctionArgument) o;
        return Objects.equals(name, that.name) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }
}
