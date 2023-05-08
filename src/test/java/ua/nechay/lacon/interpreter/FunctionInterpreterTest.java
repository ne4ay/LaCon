package ua.nechay.lacon.interpreter;

import org.junit.Test;
import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.utils.LaconUtils;

import java.io.IOException;

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

        assertVariable(result, "a", LaconBuiltInType.INT, 2L);
        assertVariable(result, "result", LaconBuiltInType.INT, -39L);

    }
}
