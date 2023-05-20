package ua.nechay.lacon.ast;

import ua.nechay.lacon.LaconToken;
import ua.nechay.lacon.core.LaconOperation;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.LaconValueUtils;
import ua.nechay.lacon.core.function.FunctionLaconValue;
import ua.nechay.lacon.core.touch.SimpleTypeTouch;
import ua.nechay.lacon.core.touch.TypeTouch;
import ua.nechay.lacon.core.val.BooleanLaconValue;
import ua.nechay.lacon.core.val.DictLaconValue;
import ua.nechay.lacon.core.val.IntLaconValue;
import ua.nechay.lacon.core.val.ListLaconValue;
import ua.nechay.lacon.core.val.RealLaconValue;
import ua.nechay.lacon.core.val.StringLaconValue;

import javax.annotation.Nonnull;

import static ua.nechay.lacon.core.LaconOperation.CAST;
import static ua.nechay.lacon.exception.LaconUnsupportedOperationException.unsupportedOperation;

/**
 * @author anechaev
 * @since 22.04.2023
 */
public class CastAST implements AST {

    private final LaconToken castOperation;
    private final AST expression;

    public CastAST(@Nonnull LaconToken castOperation, @Nonnull AST expression) {
        this.castOperation = castOperation;
        this.expression = expression;
    }

    @Nonnull
    @Override
    public LaconProgramState interpret(@Nonnull LaconProgramState state) {
        var newState = getExpression().interpret(state);
        var value = newState.popValue();

        String castOperation = getCastOperation().getText();
        LaconBuiltInType toType = LaconBuiltInType.getForRepresentation(castOperation);
        if (toType == null) {
            throw new IllegalStateException("Unknown type: " + castOperation);
        }
        LaconValue<?> castedValue = value.castTo(toType);
        return state.pushValue(castedValue);
    }

    public LaconToken getCastOperation() {
        return castOperation;
    }

    public AST getExpression() {
        return expression;
    }
}
