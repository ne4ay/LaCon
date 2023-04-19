package ua.nechay.lacon.interpreter;

import org.junit.Test;
import ua.nechay.lacon.core.var.LaconInitializedVariable;
import ua.nechay.lacon.core.var.LaconVariable;
import ua.nechay.lacon.utils.LaconUtils;
import ua.nechay.lacon.core.LaconProgramState;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

/**
 * @author anechaev
 * @since 17.03.2023
 */
public class IntegerInterpreterTest {

    @Test
    public void testUnaryOperation() {
        LaconProgramState result = LaconUtils.exec("{"
            + "a : int = -1 + 4 * (2 + 1);"
            + "some_var : int = -(2 + 4) * 5 / 6 + 1;"
            + "}");
        LaconVariable aVariable = result.getVar("a");
        assertNotNull(aVariable);
        assertThat(aVariable.getValue(), equalTo(11L));

        LaconVariable some_varVariable = result.getVar("some_var");
        assertNotNull(some_varVariable);
        assertThat(some_varVariable.getValue(), equalTo(-4L));
    }

    @Test
    public void testIntegers() {
        LaconProgramState result = LaconUtils.exec("{"
            + "a : int = -1_000 + 4 * (20 + 1);}"
            + "");
        assertThat(result.popValue().getValue(), equalTo(-916L));
    }

    @Test
    public void testDeclarationWithoutAssignment() {
        LaconProgramState result = LaconUtils.exec("{"
            + "a : int = -1 + 4 * (2 + 1);"
            + "some_var : int = -(2 + 4) * 5 / 6 + 1;"
            + "kek : int = a * some_var;"
            + "}");
        LaconVariable aVariable = result.getVar("a");
        assertNotNull(aVariable);
        assertThat(aVariable.getValue(), equalTo(11L));

        LaconVariable some_varVariable = result.getVar("some_var");
        assertNotNull(some_varVariable);
        assertThat(some_varVariable.getValue(), equalTo(-4L));

        LaconVariable kekVariable = result.getVar("kek");
        assertNotNull(kekVariable);
        assertThat(kekVariable.getValue(), equalTo(-44L));
    }
}
