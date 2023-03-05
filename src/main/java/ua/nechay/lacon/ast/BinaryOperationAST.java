package ua.nechay.lacon.ast;

import ua.nechay.lacon.LaconToken;

import javax.annotation.Nonnull;

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

    public AST getLeft() {
        return left;
    }

    public LaconToken getOperation() {
        return operation;
    }

    public AST getRight() {
        return right;
    }
}
