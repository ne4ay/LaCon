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
import static ua.nechay.lacon.TestUtils.assertVariable;
import static ua.nechay.lacon.TestUtils.extractList;
import static ua.nechay.lacon.TestUtils.getProgram;

/**
 * @author anechaev
 * @since 16.05.2023
 */
public class MethodInterpreterTest {

    @Test
    public void testStringMethods() throws IOException {
        String program = getProgram();
        LaconProgramState result = LaconUtils.exec(program);

        assertVariable(result, "abc", LaconBuiltInType.STRING, "abc");
        assertVariable(result, "length1", LaconBuiltInType.INT, 3L);
        assertVariable(result, "length2", LaconBuiltInType.INT, 3L);
        assertVariable(result, "sub", LaconBuiltInType.STRING, "kek");
        assertVariable(result, "names_str", LaconBuiltInType.STRING, "Masha,Ivan,Abdulasir,Shrek");

        List<LaconValue<?>> list = extractList(result,"names");
        assertThat(list.size(), equalTo(4));

        assertValue(list.get(0), LaconBuiltInType.STRING, "Masha");
        assertValue(list.get(1), LaconBuiltInType.STRING, "Ivan");
        assertValue(list.get(2), LaconBuiltInType.STRING, "Abdulasir");
        assertValue(list.get(3), LaconBuiltInType.STRING, "Shrek");
    }
}
