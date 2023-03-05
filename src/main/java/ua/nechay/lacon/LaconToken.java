package ua.nechay.lacon;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * @author anechaev
 * @since 03.03.2023
 */
public class LaconToken {

    private final LaconTokenType type;
    private final String text;

    public LaconToken(@Nonnull LaconTokenType type, String text) {
        this.type = type;
        this.text = text;
    }

    @Nonnull
    public LaconTokenType getType() {
        return type;
    }

    @Nullable
    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof LaconToken))
            return false;
        LaconToken token = (LaconToken) o;
        return type == token.type && Objects.equals(text, token.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, text);
    }
}
