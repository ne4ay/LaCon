package ua.nechay.lacon.ast;

import ua.nechay.lacon.LaconToken;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconValue;

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
        if (!(left instanceof AssignableAST)) {
            throw new IllegalStateException("Unable to assign to non-assignable entity. Assign index: " + assignment.getStartPos());
        }
        AssignableAST assignableLeftPart = (AssignableAST) left;
        var afterRightState = right.interpret(state);
        LaconValue<?> value = afterRightState.popValue();
        var afterLeftState = assignableLeftPart.interpret(afterRightState);
        return assignableLeftPart.assign(afterLeftState, value);
    }
}
