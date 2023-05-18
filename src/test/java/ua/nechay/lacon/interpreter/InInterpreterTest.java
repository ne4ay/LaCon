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
 * @since 18.05.2023
 */
public class InInterpreterTest {

    @Test
    public void testBasicIn() throws IOException {
        String program = getProgram();
        LaconProgramState result = LaconUtils.exec(program);

        List<LaconValue<?>> resultList = extractList(result, "result");
        assertThat(resultList.size(), equalTo(6));

        assertValue(resultList.get(0), LaconBuiltInType.INT, -2L);
        assertValue(resultList.get(1), LaconBuiltInType.INT, 9L);
        assertValue(resultList.get(2), LaconBuiltInType.INT, 0L);
        assertValue(resultList.get(3), LaconBuiltInType.INT, 1L);
        assertValue(resultList.get(4), LaconBuiltInType.INT, 3L);
        assertValue(resultList.get(5), LaconBuiltInType.INT, 5L);
    }
}
