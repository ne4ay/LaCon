package ua.nechay.lacon.interpreter;

import org.junit.Test;
import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.val.StringLaconValue;
import ua.nechay.lacon.utils.LaconUtils;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertNull;
import static ua.nechay.lacon.TestUtils.assertVariable;
import static ua.nechay.lacon.TestUtils.extractDict;
import static ua.nechay.lacon.TestUtils.getProgram;

/**
 * @author anechaev
 * @since 18.05.2023
 */
public class DictInterpreterTest {

    @Test
    public void testSimpleDictionary() throws IOException {
        String program = getProgram();
        LaconProgramState result = LaconUtils.exec(program);

        assertVariable(result, "york_population", LaconBuiltInType.INT, 15_000L);
        assertVariable(result, "fr_population", LaconBuiltInType.INT, 759L);
        assertVariable(result, "donetsk_new_york_population", LaconBuiltInType.INT, 9L);
        assertVariable(result, "size", LaconBuiltInType.INT, 4L);

        Map<LaconValue<?>, LaconValue<?>> map = extractDict(result, "cities");
        LaconValue<?> tbilisiPop = map.get(new StringLaconValue("Tbilisi"));
        assertNull(tbilisiPop);
    }
}
