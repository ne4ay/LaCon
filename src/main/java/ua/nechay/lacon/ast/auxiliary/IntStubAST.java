package ua.nechay.lacon.ast.auxiliary;

import ua.nechay.lacon.ast.AST;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.val.IntLaconValue;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 13.05.2023
 */
public class IntStubAST implements AST {

    private final long providingValue;

    public IntStubAST(long providingValue) {
        this.providingValue = providingValue;
    }

    @Nonnull
    @Override
    public LaconProgramState interpret(@Nonnull LaconProgramState state) {
        return state.pushValue(new IntLaconValue(providingValue));
    }
}
