package ua.nechay.lacon.interpreter;

import org.junit.Test;
import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.utils.LaconUtils;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static ua.nechay.lacon.TestUtils.assertValue;
import static ua.nechay.lacon.TestUtils.extractList;
import static ua.nechay.lacon.TestUtils.getProgram;

/**
 * @author anechaev
 * @since 19.05.2023
 */
public class RegexInterpreterTest {

    @Test
    public void testSimpleRegexExamples() throws IOException {
        String program = getProgram();
        LaconProgramState result = LaconUtils.exec(program);

        List<LaconValue<?>> result1 = extractList(result, "result1");
        assertThat(result1.size(), equalTo(2));

        assertValue(result1.get(0), LaconBuiltInType.STRING, "EUR/USD - 1.09");
        assertValue(result1.get(1), LaconBuiltInType.STRING, "AUD/NZD - 23.043");

        List<LaconValue<?>> groups = extractList(result, "groups");
        assertThat(groups.size(), equalTo(2));

        assertValue(groups.get(0), LaconBuiltInType.STRING, "EUR/USD");
        assertValue(groups.get(1), LaconBuiltInType.STRING, "AUD/NZD");
    }
}
