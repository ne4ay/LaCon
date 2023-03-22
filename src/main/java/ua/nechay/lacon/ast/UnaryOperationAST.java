package ua.nechay.lacon.ast;

import ua.nechay.lacon.LaconToken;
import ua.nechay.lacon.LaconTokenType;

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

    @Override
    public int interpret() {
        LaconTokenType type = operation.getType();
        switch (type) {
        case PLUS:
            return + expression.interpret();
        case MINUS:
            return - expression.interpret();
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
