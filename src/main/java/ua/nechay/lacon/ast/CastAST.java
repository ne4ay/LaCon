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

        LaconBuiltInType fromType = value.getType();
        String castOperation = getCastOperation().getText();
        LaconBuiltInType toType = LaconBuiltInType.getForRepresentation(castOperation);
        if (toType == null) {
            throw new IllegalStateException("Unknown type: " + castOperation);
        }
        LaconValue<?> castedValue = TypeTouch.touch(toType, SimpleTypeTouch.create(
            () -> TypeTouch.touch(fromType, SimpleTypeTouch.create(
                () -> value,
                () -> new IntLaconValue(RealLaconValue.castToInt((double)value.getValue())),
                () -> new IntLaconValue(Long.parseLong((String)value.getValue())),
                () -> new IntLaconValue(BooleanLaconValue.castToIntValue(value)),
                () -> unsupportedOperation(CAST, LaconBuiltInType.LIST, LaconBuiltInType.INT),
                () -> unsupportedOperation(CAST, LaconBuiltInType.FUNCTION, LaconBuiltInType.INT)
            )),
            () -> TypeTouch.touch(fromType, SimpleTypeTouch.create(
                () -> new RealLaconValue(IntLaconValue.castToReal((long)value.getValue())),
                () -> value,
                () -> new RealLaconValue(Double.parseDouble((String)value.getValue())),
                () -> unsupportedOperation(CAST, LaconBuiltInType.BOOLEAN, LaconBuiltInType.REAL),
                () -> unsupportedOperation(CAST, LaconBuiltInType.LIST, LaconBuiltInType.REAL),
                () -> unsupportedOperation(CAST, LaconBuiltInType.FUNCTION, LaconBuiltInType.REAL)
            )),
            () -> LaconValueUtils.castToStr(value),
            () -> TypeTouch.touch(fromType, SimpleTypeTouch.create(
                () -> new BooleanLaconValue(IntLaconValue.castToBoolValue((long)value.getValue())),
                () -> unsupportedOperation(CAST, LaconBuiltInType.REAL, LaconBuiltInType.BOOLEAN),
                () -> new BooleanLaconValue(Boolean.valueOf((String) value.getValue())),
                () -> value,
                () -> new BooleanLaconValue(ListLaconValue.castToBoolValue(value)),
                () -> unsupportedOperation(CAST, LaconBuiltInType.FUNCTION, LaconBuiltInType.BOOLEAN)
            )),
            () -> TypeTouch.touch(fromType, SimpleTypeTouch.create(
                () -> ListLaconValue.create(value),
                () -> ListLaconValue.create(value),
                () -> ListLaconValue.create(value),
                () -> ListLaconValue.create(value),
                () -> value,
                () -> ListLaconValue.create(value)
            )),
            () -> TypeTouch.touch(fromType, SimpleTypeTouch.create(
                () -> FunctionLaconValue.createSupplier(value),
                () -> FunctionLaconValue.createSupplier(value),
                () -> FunctionLaconValue.createSupplier(value),
                () -> FunctionLaconValue.createSupplier(value),
                () -> FunctionLaconValue.createSupplier(value),
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
