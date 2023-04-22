package ua.nechay.lacon.ast;

import ua.nechay.lacon.LaconToken;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconType;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.touch.SimpleTypeTouch;
import ua.nechay.lacon.core.touch.TypeTouch;
import ua.nechay.lacon.core.val.BooleanLaconValue;
import ua.nechay.lacon.core.val.IntLaconValue;
import ua.nechay.lacon.core.val.RealLaconValue;
import ua.nechay.lacon.core.val.StringLaconValue;

import javax.annotation.Nonnull;

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

        LaconType fromType = value.getType();
        String castOperation = getCastOperation().getText();
        LaconType toType = LaconType.getForRepresentation(castOperation);
        if (toType == null) {
            throw new IllegalStateException("Unknown type: " + castOperation);
        }
        LaconValue<?> castedValue = TypeTouch.touch(toType, SimpleTypeTouch.create(
            () -> TypeTouch.touch(fromType, SimpleTypeTouch.create(
                () -> value,
                () -> new IntLaconValue(RealLaconValue.castToInt((double)value.getValue())),
                () -> new IntLaconValue(Long.parseLong((String)value.getValue())),
                () -> new IntLaconValue(BooleanLaconValue.castToInt((boolean)value.getValue()))
            )),
            () -> TypeTouch.touch(fromType, SimpleTypeTouch.create(
                () -> new RealLaconValue(IntLaconValue.castToReal((long)value.getValue())),
                () -> value,
                () -> new RealLaconValue(Double.parseDouble((String)value.getValue())),
                () -> unsupportedOperation("cast", LaconType.BOOLEAN, LaconType.REAL)
            )),
            () -> TypeTouch.touch(fromType, SimpleTypeTouch.create(
                () -> new StringLaconValue(String.valueOf((long)value.getValue())),
                () -> new StringLaconValue(String.valueOf((double)value.getValue())),
                () -> value,
                () -> new StringLaconValue(String.valueOf((boolean) value.getValue()))
            )),
            () -> TypeTouch.touch(fromType, SimpleTypeTouch.create(
                () -> new BooleanLaconValue(IntLaconValue.castToBool((long)value.getValue())),
                () -> unsupportedOperation("cast", LaconType.REAL, LaconType.BOOLEAN),
                () -> new BooleanLaconValue(Boolean.valueOf((String) value.getValue())),
                () -> value
            ))
        ));
        return state.pushValue(castedValue);
    }

    public LaconToken getCastOperation() {
        return castOperation;
    }

    public AST getExpression() {
        return expression;
    }
}
