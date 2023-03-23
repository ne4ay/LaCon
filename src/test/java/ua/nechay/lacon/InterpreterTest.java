package ua.nechay.lacon;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author anechaev
 * @since 17.03.2023
 */
public class InterpreterTest {

    @Test
    public void testUnaryOperation() {
        int result = LaconUtils.exec("-1 + 4 * (2 + 1)");
        assertThat(result, equalTo(11));
    }

    @Test
    public void testIntegers() {
        int result = LaconUtils.exec("-1_000 + 4 * (20 + 1)");
        assertThat(result, equalTo(-916));
    }
}
