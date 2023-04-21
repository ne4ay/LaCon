package ua.nechay.lacon.interpreter;

import org.junit.Test;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.var.LaconVariable;
import ua.nechay.lacon.utils.LaconUtils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

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
}
