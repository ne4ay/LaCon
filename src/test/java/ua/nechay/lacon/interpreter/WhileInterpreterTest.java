package ua.nechay.lacon.interpreter;

import org.junit.Test;
import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.utils.LaconUtils;

import java.io.IOException;

import static ua.nechay.lacon.TestUtils.assertVariable;
import static ua.nechay.lacon.TestUtils.getProgram;

/**
 * @author anechaev
 * @since 30.04.2023
 */
public class WhileInterpreterTest {

    @Test
    public void testSimpleWhile() throws IOException {
        String program = getProgram();
        LaconProgramState result = LaconUtils.exec(program);

        assertVariable(result, "a", LaconBuiltInType.INT, 6L);
        assertVariable(result, "b", LaconBuiltInType.INT, 120L);
        assertVariable(result, "c", LaconBuiltInType.INT, -2L);
        assertVariable(result, "d", LaconBuiltInType.INT, -3L);
    }
}
