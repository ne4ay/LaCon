package ua.nechay.lacon;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author anechaev
 * @since 03.03.2023
 */
public class LaconToken {

    private final LaconTokenType type;
    private final String text;
    private final int startPos;

    public LaconToken(@Nonnull LaconTokenType type, @Nonnull String text, int startPos) {
        this.type = type;
        this.text = text;
        this.startPos = startPos;
    }

    @Nonnull
    public LaconTokenType getType() {
        return type;
    }

    @Nonnull
    public String getText() {
        return text;
    }

    public int getStartPos() {
        return startPos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof LaconToken))
            return false;
        LaconToken token = (LaconToken) o;
        return startPos == token.startPos && type == token.type && Objects.equals(text, token.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, text, startPos);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", LaconToken.class.getSimpleName() + "[", "]")
            .add("type=" + type)
            .add("text='" + text + "'")
            .add("startPos=" + startPos)
            .toString();
    }
}
