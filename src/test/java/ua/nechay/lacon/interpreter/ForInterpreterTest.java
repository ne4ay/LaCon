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
 * @since 14.05.2023
 */
public class ForInterpreterTest {

    @Test
    public void testSimpleForLoop() throws IOException {
        String program = getProgram();
        LaconProgramState result = LaconUtils.exec(program);

        List<LaconValue<?>> resultList = extractList(result, "result");
        assertThat(resultList.size(), equalTo(3));

        assertValue(resultList.get(0), LaconBuiltInType.STRING, "New YorkNew YorkNew York");
        assertValue(resultList.get(1), LaconBuiltInType.STRING, "LondonLondonLondon");
        assertValue(resultList.get(2), LaconBuiltInType.STRING, "DonetskDonetskDonetsk");
    }
}
