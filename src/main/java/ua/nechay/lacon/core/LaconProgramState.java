package ua.nechay.lacon.core;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * @author anechaev
 * @since 11.04.2023
 */
public class LaconProgramState {

    private final Deque<LaconValue> valueStack = new ArrayDeque<>();
    private final Map<String, LaconVariable> variables = new HashMap<>();
}
