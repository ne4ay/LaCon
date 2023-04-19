package ua.nechay.lacon.core;

import ua.nechay.lacon.core.var.LaconVariable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * @author anechaev
 * @since 11.04.2023
 */
public class LaconProgramState {

    private final Deque<LaconValue<?>> valueStack = new ArrayDeque<>();
    private final Map<String, LaconVariable> variables = new HashMap<>();

    private LaconProgramState() {

    }

    public static LaconProgramState create() {
        return new LaconProgramState();
    }

    @Nonnull
    public LaconProgramState clearValueStack() {
        valueStack.clear();
        return this;
    }

    @Nonnull
    public LaconProgramState putVar(@Nonnull String varName, @Nonnull LaconVariable var) {
        variables.put(varName, var);
        return this;
    }

    @Nullable
    public LaconVariable getVar(@Nonnull String name) {
        return variables.get(name);
    }

    @Nonnull
    public LaconProgramState pushValue(@Nonnull LaconValue<?> value) {
        valueStack.push(value);
        return this;
    }

    @Nonnull
    public LaconValue<?> popValue() {
        return valueStack.pop();
    }
}
