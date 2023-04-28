package ua.nechay.lacon.interpreter;

import org.junit.Test;
import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.var.LaconVariable;
import ua.nechay.lacon.utils.LaconUtils;

import java.io.IOException;
import java.nio.file.Files;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static ua.nechay.lacon.TestUtils.assertVariable;
import static ua.nechay.lacon.TestUtils.getPathOfTestResource;
import static ua.nechay.lacon.TestUtils.getProgram;

/**
 * @author anechaev
 * @since 21.04.2023
 */
public class StringInterpreterTest {

    @Test
    public void testStringInterpret() {
        LaconProgramState result = LaconUtils.exec("{"
            + "a : int = -1 + 4 * (2 + 1);"
            + "some_var : int = -(2 + 4) * 5 / 6 + 1;"
            + "her : str = some_var * \"abc\";"
            + "lol : str = -her"
            + "}");
        LaconVariable herVariable = result.getVar("her");
        assertNotNull(herVariable);
        assertThat(herVariable.getValue(), equalTo("cbacbacbacba"));

        LaconVariable lolVariable = result.getVar("lol");
        assertNotNull(lolVariable);
        assertThat(lolVariable.getValue(), equalTo("abcabcabcabc"));
    }

    @Test
    public void testGetByIndex() throws IOException {
        String program = getProgram();
        LaconProgramState result = LaconUtils.exec(program);

        assertVariable(result, "a", LaconBuiltInType.STRING, "0123456789");
        assertVariable(result, "b0", LaconBuiltInType.STRING, "0");
        assertVariable(result, "b1", LaconBuiltInType.STRING, "1");
        assertVariable(result, "c", LaconBuiltInType.STRING, "3");
        assertVariable(result, "d", LaconBuiltInType.STRING, "9");
    }
}
