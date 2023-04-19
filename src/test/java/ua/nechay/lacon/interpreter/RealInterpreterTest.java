package ua.nechay.lacon.interpreter;

import org.junit.Test;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.var.LaconVariable;
import ua.nechay.lacon.utils.LaconUtils;
import ua.nechay.lacon.TestUtils;
import ua.nechay.lacon.core.LaconProgramState;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertNotNull;

/**
 * @author anechaev
 * @since 16.04.2023
 */
public class RealInterpreterTest {

    @Test
    public void testUnaryOperation() {
        LaconProgramState result = LaconUtils.exec("{"
            //             14.31   15.54     3.7
            + "lol:real=-1.23 + 4.2 * (2.5 + 1.2);"
            + "}");
        LaconVariable variable = result.getVar("lol");
        assertNotNull(variable);
        assertThat((double)variable.getValue(), closeTo(14.31, TestUtils.EPSILON));
    }

}
