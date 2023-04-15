package ua.nechay.lacon.interpreter;

import org.junit.Test;
import ua.nechay.lacon.LaconUtils;
import ua.nechay.lacon.TestUtils;
import ua.nechay.lacon.core.LaconProgramState;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

/**
 * @author anechaev
 * @since 16.04.2023
 */
public class RealInterpreterTest {

    @Test
    public void testUnaryOperation() {
        //                                                  14.31   15.54     3.7
        LaconProgramState result = LaconUtils.exec("-1.23 + 4.2 * (2.5 + 1.2)");
        assertThat((double)result.popValue().getValue(), closeTo(14.31, TestUtils.EPSILON));
    }

}
