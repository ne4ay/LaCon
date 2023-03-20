package ua.nechay.lacon.ast;

import ua.nechay.lacon.LaconToken;

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

    @Override
    public int interpret() {
        switch (getOperation().getType()) {
        case PLUS:
            return getLeft().interpret() + getRight().interpret();
        case MINUS:
            return getLeft().interpret() - getRight().interpret();
        case MUL:
            return getLeft().interpret() * getRight().interpret();
        case DIV:
            return getLeft().interpret() / getRight().interpret();
        default:
            throw new IllegalStateException("Unknown operation type: " + getOperation());
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
