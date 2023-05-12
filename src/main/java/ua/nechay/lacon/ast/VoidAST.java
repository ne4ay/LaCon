package ua.nechay.lacon.ast;

import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.val.VoidLaconValue;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 11.05.2023
 */
public class VoidAST implements AST {
    @Nonnull
    @Override
    public LaconProgramState interpret(@Nonnull LaconProgramState state) {
        return state.pushValue(VoidLaconValue.create());
    }
}
