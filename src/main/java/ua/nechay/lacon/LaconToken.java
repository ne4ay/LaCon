package ua.nechay;

import java.util.Objects;

/**
 * @author anechaev
 * @since 03.03.2023
 */
public class LaconToken {

    private final LaconTokenType type;
    private final String text;
    private final int startPos;

    public LaconToken(LaconTokenType type, String text, int startPos) {
        this.type = type;
        this.text = text;
        this.startPos = startPos;
    }

    public LaconTokenType getType() {
        return type;
    }

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
        LaconToken that = (LaconToken) o;
        return startPos == that.startPos && type == that.type && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, text, startPos);
    }


}
