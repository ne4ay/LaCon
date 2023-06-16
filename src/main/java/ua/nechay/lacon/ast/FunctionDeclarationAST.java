package ua.nechay.lacon.ast;

import ua.nechay.lacon.LaconToken;
import ua.nechay.lacon.LaconTokenType;
import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconType;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.LaconValueUtils;
import ua.nechay.lacon.core.function.FunctionLaconValue;
import ua.nechay.lacon.core.function.LaconFunctionArgument;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author anechaev
 * @since 02.05.2023
 */
public class FunctionDeclarationAST implements AST {

    private final LaconToken identifier;
    private final List<AST> argumentsDeclaration;
    private final LaconToken returnType;
    private final AST functionBody;

    public FunctionDeclarationAST(@Nonnull LaconToken identifier, @Nonnull List<AST> argumentsDeclaration,
        @Nonnull LaconToken returnType, @Nonnull AST functionBody)
    {
        this.identifier = identifier;
        this.argumentsDeclaration = argumentsDeclaration;
        this.returnType = returnType;
        this.functionBody = functionBody;
    }

    @Nonnull
    @Override
    public LaconProgramState interpret(@Nonnull LaconProgramState state) {
        String functionName = getFunctionName();
        FunctionLaconValue existingFunction = state.getFunction(functionName);
        if (existingFunction != null) {
            throw new IllegalStateException("Unable to declare function with non-unique name!!");
        }
        List<LaconFunctionArgument> declaringArguments = new ArrayList<>();
        for (int i = 0; i < argumentsDeclaration.size(); i++) {
            AST nextArgDeclaration = argumentsDeclaration.get(i);
            LaconFunctionArgument nextArgument = mapToArgument(nextArgDeclaration);
            declaringArguments.add(nextArgument);
        }
        LaconType returningType = LaconValueUtils.determineType(state, returnType);
        state.putFunction(functionName, FunctionLaconValue.create(declaringArguments, functionBody, returningType));
        return state;
    }

    private LaconFunctionArgument mapToArgument(@Nonnull AST declaration) {
        LaconValue<?> value = declaration.beginInterpret().popValue();
        Object obj = value.getValue();
        if (!(obj instanceof String)) {
            throw new IllegalStateException("Unable to declare non-string argument name!");
        }
        return new LaconFunctionArgument((String) obj, value.getType());
    }

    @Nonnull
    public String getFunctionName() {
        return identifier.getText();
    }
}
