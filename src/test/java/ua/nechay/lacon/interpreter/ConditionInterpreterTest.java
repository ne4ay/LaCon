package ua.nechay.lacon.interpreter;

import org.junit.Test;
import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.var.LaconVariable;
import ua.nechay.lacon.utils.LaconUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static ua.nechay.lacon.TestUtils.assertVariable;
import static ua.nechay.lacon.TestUtils.getPathOfTestResource;
import static ua.nechay.lacon.TestUtils.getProgram;

/**
 * @author anechaev
 * @since 24.04.2023
 */
public class ConditionInterpreterTest {

    @Test
    public void testSimpleIfElse() throws IOException {
        String program = getProgram();
        LaconProgramState result = LaconUtils.exec(program);

        LaconVariable aVariable = result.getVar("a");
        assertNotNull(aVariable);
        assertThat(aVariable.getValue(), equalTo(4L));
        assertThat(aVariable.getType(), equalTo(LaconBuiltInType.INT));

        LaconVariable bVariable = result.getVar("b");
        assertNull(bVariable);

        LaconVariable cVariable = result.getVar("c");
        assertNull(cVariable);

        LaconVariable dVariable = result.getVar("d");
        assertNotNull(dVariable);
        assertThat(dVariable.getValue(), equalTo(2L));
        assertThat(dVariable.getType(), equalTo(LaconBuiltInType.INT));

        assertVariable(result, "after_if", LaconBuiltInType.REAL, 23.1);
    }

    @Test
    public void testIfExpression() throws IOException {
        String program = getProgram();
        LaconProgramState result = LaconUtils.exec(program);

        assertVariable(result, "a", LaconBuiltInType.INT, 4L);
        assertVariable(result, "b", LaconBuiltInType.INT, 3L);
        assertVariable(result, "a_type", LaconBuiltInType.STRING, "even");
        assertVariable(result, "b_type", LaconBuiltInType.STRING, "odd");
    }
}
