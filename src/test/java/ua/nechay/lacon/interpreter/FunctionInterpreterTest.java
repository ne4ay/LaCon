package ua.nechay.lacon.interpreter;

import org.junit.Test;
import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.LaconValueUtils;
import ua.nechay.lacon.core.val.ListLaconValue;
import ua.nechay.lacon.core.var.LaconVariable;
import ua.nechay.lacon.utils.LaconUtils;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static ua.nechay.lacon.TestUtils.assertValue;
import static ua.nechay.lacon.TestUtils.assertVariable;
import static ua.nechay.lacon.TestUtils.getProgram;

/**
 * @author anechaev
 * @since 08.05.2023
 */
public class FunctionInterpreterTest {

    @Test
    public void testSimplePositionFunctionCall() throws IOException {
        String program = getProgram();
        LaconProgramState result = LaconUtils.exec(program);

        assertVariable(result, "first", LaconBuiltInType.INT, 126L);
        assertVariable(result, "second", LaconBuiltInType.INT, 5L);
        assertVariable(result, "third", LaconBuiltInType.INT, 252L);
    }

    @Test
    public void testSimpleNamedFunctionCall() throws IOException {
        String program = getProgram();
        LaconProgramState result = LaconUtils.exec(program);

        assertVariable(result, "a", LaconBuiltInType.INT, 1L);
        assertVariable(result, "result", LaconBuiltInType.INT, -39L);

    }

    @Test
    public void testAddingFunctionsWithOneArgument() throws IOException {
        String program = getProgram();
        LaconProgramState result = LaconUtils.exec(program);

        assertVariable(result, "b", LaconBuiltInType.INT, 10L);
    }

    @Test
    public void testAddingFunctionsWithMultipleArgs() throws IOException {
        String program = getProgram();
        LaconProgramState result = LaconUtils.exec(program);

        assertVariable(result, "result", LaconBuiltInType.STRING, "0 added line is 10hehe + hehe10");
    }

    @Test
    public void testMultiplyingFunctionsOnNumber() throws IOException {
        String program = getProgram();
        LaconUtils.exec(program);
    }

    @Test
    public void testMultiplyingFunctionWithOneArgOnList() throws IOException {
        String program = getProgram();
        LaconProgramState result = LaconUtils.exec(program);

        LaconVariable resultVariable = result.getVar("result");
        assertNotNull(resultVariable);
        assertThat(resultVariable.getType(), equalTo(LaconBuiltInType.LIST));

        LaconValue<?> value = resultVariable.getValueObject();
        assertTrue(value instanceof ListLaconValue);

        ListLaconValue listValue = (ListLaconValue) value;
        List<LaconValue<?>> list = listValue.getValue();

        assertThat(list.size(), equalTo(10));

        assertValue(list.get(0), LaconBuiltInType.INT, 0L);
        assertValue(list.get(1), LaconBuiltInType.INT, 1L);
        assertValue(list.get(2), LaconBuiltInType.INT, 4L);
        assertValue(list.get(3), LaconBuiltInType.INT, 9L);
        assertValue(list.get(4), LaconBuiltInType.INT, 16L);
        assertValue(list.get(5), LaconBuiltInType.INT, 25L);
        assertValue(list.get(6), LaconBuiltInType.INT, 36L);
        assertValue(list.get(7), LaconBuiltInType.INT, 49L);
        assertValue(list.get(8), LaconBuiltInType.INT, 64L);
        assertValue(list.get(9), LaconBuiltInType.INT, 81L);
    }

    @Test
    public void testMultiplyingFunctionWithMultipleArgsOnList() throws IOException {
        String program = getProgram();
        LaconProgramState result = LaconUtils.exec(program);

        LaconVariable resultVariable = result.getVar("result");
        assertNotNull(resultVariable);
        assertThat(resultVariable.getType(), equalTo(LaconBuiltInType.LIST));

        LaconValue<?> value = resultVariable.getValueObject();
        assertTrue(value instanceof ListLaconValue);

        ListLaconValue listValue = (ListLaconValue) value;
        List<LaconValue<?>> list = listValue.getValue();
        assertThat(list.size(), equalTo(3));

        assertValue(list.get(0), LaconBuiltInType.STRING,
            "[Cairo, Mexico City, Mumbai] -- 33.0 Celsius");
        assertValue(list.get(1), LaconBuiltInType.STRING,
            "[New York, Los Angeles] -- 79.34 Fahrenheit");
        assertValue(list.get(2), LaconBuiltInType.STRING,
            "[Moscow] -- 11.0 Celsius");
    }
}
