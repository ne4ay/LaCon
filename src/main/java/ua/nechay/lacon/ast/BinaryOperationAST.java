package ua.nechay.lacon.ast;

import ua.nechay.lacon.LaconToken;

import javax.annotation.Nonnull;

import static ua.nechay.lacon.LaconTokenType.DIV;
import static ua.nechay.lacon.LaconTokenType.MINUS;
import static ua.nechay.lacon.LaconTokenType.MUL;
import static ua.nechay.lacon.LaconTokenType.PLUS;

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
}
