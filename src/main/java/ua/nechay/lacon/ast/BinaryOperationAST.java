package ua.nechay.lacon.ast;

import ua.nechay.lacon.LaconToken;
import ua.nechay.lacon.core.LaconProgramState;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * @author anechaev
 * @since 06.03.2023
 */
public class BinaryOperationAST implements AST {

    private final AST left;
    private final LaconToken operation;
    private final AST right;

    public BinaryOperationAST(@Nonnull AST left, @Nonnull LaconToken operation, @Nonnull AST right) {
        this.left = left;
        this.operation = operation;
        this.right = right;
    }

    @Override @Nonnull
    public LaconProgramState interpret(@Nonnull LaconProgramState state) {
        LaconProgramState afterLeftState = getLeft().interpret(state);
        var leftValue = afterLeftState.popValue();

        LaconProgramState afterRightState = getRight().interpret(afterLeftState);
        var rightValue = afterRightState.popValue();
        switch (getOperation().getType()) {
        case PLUS:
            return afterRightState.pushValue(leftValue.plus(rightValue));
        case MINUS:
            return afterRightState.pushValue(leftValue.minus(rightValue));
        case MUL:
            return afterRightState.pushValue(leftValue.mul(rightValue));
        case DIV:
            return afterRightState.pushValue(leftValue.div(rightValue));
        case EQUALS:
            return afterRightState.pushValue(leftValue.equal(rightValue));
        case NOT_EQUALS:
            return afterRightState.pushValue(leftValue.notEqual(rightValue));
        case OR:
            return afterRightState.pushValue(leftValue.or(rightValue));
        case AND:
            return afterRightState.pushValue(leftValue.and(rightValue));
        default:
            throw new IllegalStateException("Unknown operation type: " + getOperation().getText() + " at position: " + getOperation().getStartPos());
        }
    }

    @Nonnull
    public AST getLeft() {
        return left;
    }

    @Nonnull
    public LaconToken getOperation() {
        return operation;
    }

    @Nonnull
    public AST getRight() {
        return right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof BinaryOperationAST))
            return false;
        BinaryOperationAST that = (BinaryOperationAST) o;
        return Objects.equals(left, that.left) && Objects.equals(operation, that.operation) && Objects.equals(
            right, that.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, operation, right);
    }
}
