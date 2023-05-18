package ua.nechay.lacon.ast;

import ua.nechay.lacon.LaconToken;
import ua.nechay.lacon.LaconTokenType;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconValue;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * @author anechaev
 * @since 17.03.2023
 */
public class UnaryOperationAST implements AST {

    private final LaconToken operation;
    private final AST expression;

    public UnaryOperationAST(@Nonnull LaconToken operation, @Nonnull AST expression) {
        this.operation = operation;
        this.expression = expression;
    }

    @Nonnull
    @Override
    public LaconProgramState interpret(@Nonnull LaconProgramState state) {
        var newState = getExpression().interpret(state);
        var value = newState.popValue();
        LaconTokenType type = getOperation().getType();
        switch (type) {
        case PLUS:
            return newState.pushValue(value.unaryPlus());
        case MINUS:
            return newState.pushValue(value.unaryMinus());
        case NOT:
            return newState.pushValue(value.unaryNot());
        default:
            throw new IllegalStateException("Unknown type: " + type + " at position " + getOperation().getStartPos());
        }
    }

    @Nonnull
    public LaconToken getOperation() {
        return operation;
    }

    @Nonnull
    public AST getExpression() {
        return expression;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UnaryOperationAST))
            return false;
        UnaryOperationAST that = (UnaryOperationAST) o;
        return Objects.equals(operation, that.operation) && Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operation, expression);
    }
}
