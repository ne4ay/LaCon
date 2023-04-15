package ua.nechay.lacon.ast;

import ua.nechay.lacon.LaconToken;
import ua.nechay.lacon.core.LaconProgramState;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 11.04.2023
 */
public class AssignmentAST implements AST {

    private final AST left;
    private final LaconToken assignment;
    private final AST right;

    public AssignmentAST(@Nonnull AST left, @Nonnull LaconToken assignment, @Nonnull AST right) {
        this.left = left;
        this.assignment = assignment;
        this.right = right;
    }

    @Nonnull
    @Override
    public LaconProgramState interpret(@Nonnull LaconProgramState state) {
        return null;
    }
}
