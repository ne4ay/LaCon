package ua.nechay.lacon.core;

import ua.nechay.lacon.core.function.FunctionLaconValue;
import ua.nechay.lacon.core.function.built.BuiltInFunction;
import ua.nechay.lacon.core.function.built.external.LaconBuiltInStubExternalCallFunction;
import ua.nechay.lacon.core.type.RegexType;
import ua.nechay.lacon.core.var.LaconVariable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * @author anechaev
 * @since 11.04.2023
 */
public class LaconProgramState {

    private final FunctionLaconValue externalCallFunction;
    private final Deque<LaconValue<?>> valueStack = new ArrayDeque<>();
    private final Map<String, LaconVariable> variables = new HashMap<>();
    private final Map<String, FunctionLaconValue> functions = new HashMap<>();
    private final Map<String, LaconType> types = new HashMap<>();
    private final Map<String, FunctionLaconValue> functionsForExport = new HashMap<>();

    private LaconProgramState(@Nonnull FunctionLaconValue externalCallFunction) {
        this.externalCallFunction = requireNonNull(externalCallFunction, "externalCallFunction");
    }

    protected static List<LaconType> getBuiltInTypes() {
        List<LaconType> result = new ArrayList<>();
        result.addAll(Arrays.asList(LaconBuiltInType.values()));
        result.addAll(Arrays.asList(RegexType.values()));
        return result;
    }

    @Nonnull
    public static LaconProgramState create() {
        return create(LaconBuiltInStubExternalCallFunction.getInstance());
    }

    @Nonnull
    public static LaconProgramState create(@Nonnull FunctionLaconValue externalCallFunction) {
        return create(externalCallFunction, new HashMap<>());

    }

    @Nonnull
    public static LaconProgramState create(@Nonnull FunctionLaconValue externalCallFunction, @Nonnull Map<String, FunctionLaconValue> importedFunctions) {
        return new LaconProgramState(externalCallFunction)
            .addFunctions(importedFunctions)
            .addFunctions(Arrays.stream(BuiltInFunction.values())
                .collect(Collectors.toMap(
                    BuiltInFunction::getName,
                    BuiltInFunction::getFunction
                )))
            .addTypes(getBuiltInTypes());
    }

    @Nonnull
    public static LaconProgramState createFromOuterScope(@Nonnull LaconProgramState state) {
        return new LaconProgramState(state.getExternalCallFunction())
            .addFunctions(state.getAllFunctions())
            .addTypes(state.getAllTypes());
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

    @Nullable
    public LaconVariable removeVar(@Nonnull String name) {
        return variables.remove(name);
    }

    @Nonnull
    public LaconProgramState putFunction(@Nonnull String name, @Nonnull FunctionLaconValue function) {
        functions.put(name, function);
        return this;
    }

    @Nullable
    public FunctionLaconValue getFunction(@Nonnull String name) {
        return functions.get(name);
    }

    public Map<String, FunctionLaconValue> getAllFunctions() {
        return functions;
    }

    public LaconProgramState addFunctions(@Nonnull Map<String, FunctionLaconValue> functions) {
        getAllFunctions().putAll(functions);
        return this;
    }

    @Nonnull
    public LaconProgramState pushValue(@Nonnull LaconValue<?> value) {
        valueStack.push(value);
        return this;
    }

    @Nonnull
    public LaconProgramState pushValues(@Nonnull List<LaconValue<?>> values) {
        for (var value : values) {
            pushValue(value);
        }
        return this;
    }

    @Nonnull
    public LaconValue<?> popValue() {
        return valueStack.pop();
    }

    @Nonnull
    public LaconProgramState addType(@Nonnull LaconType type) {
        types.put(type.getRepresentation(), type);
        return this;
    }

    @Nonnull
    public LaconProgramState addTypes(@Nonnull Collection<LaconType> typeCollection) {
        return addTypes(typeCollection.stream()
            .collect(Collectors.toMap(LaconType::getRepresentation, Function.identity())));
    }

    public LaconProgramState addTypes(@Nonnull Map<String, LaconType> typeCollection) {
        types.putAll(typeCollection);
        return this;
    }

    @Nonnull
    public Map<String, LaconType> getAllTypes() {
        return types;
    }

    @Nullable
    public LaconType getType(@Nonnull String typeRepresentation) {
        return types.get(typeRepresentation);
    }

    @Nonnull
    public FunctionLaconValue getExternalCallFunction() {
        return externalCallFunction;
    }

    @Nonnull
    public Map<String, LaconVariable> getVariables() {
        return variables;
    }

    @Nonnull
    public Map<String, FunctionLaconValue> getFunctionsForExport() {
        return functionsForExport;
    }

    @Nonnull
    public LaconProgramState putFunctionForExport(@Nonnull String name, @Nonnull FunctionLaconValue function) {
        functionsForExport.put(name, function);
        return this;
    }
}
