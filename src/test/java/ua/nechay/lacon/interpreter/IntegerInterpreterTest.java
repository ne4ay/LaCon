package ua.nechay.lacon.interpreter;

import org.junit.Test;
import ua.nechay.lacon.LaconLexer;
import ua.nechay.lacon.LaconToken;
import ua.nechay.lacon.LaconTokenType;
import ua.nechay.lacon.LaconUtils;
import ua.nechay.lacon.core.LaconProgramState;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author anechaev
 * @since 17.03.2023
 */
public class IntegerInterpreterTest {

    @Test
    public void testUnaryOperation() {
        LaconProgramState result = LaconUtils.exec("-1 + 4 * (2 + 1)");
        assertThat(result.popValue().getValue(), equalTo(11L));
    }

    @Test
    public void testIntegers() {
        LaconProgramState result = LaconUtils.exec("-1_000 + 4 * (20 + 1)");
        assertThat(result.popValue().getValue(), equalTo(-916L));
    }
}
