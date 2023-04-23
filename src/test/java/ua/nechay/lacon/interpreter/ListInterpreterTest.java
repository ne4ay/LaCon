package ua.nechay.lacon.interpreter;

import org.junit.Test;
import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.var.LaconVariable;
import ua.nechay.lacon.utils.LaconUtils;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static ua.nechay.lacon.TestUtils.EPSILON;

/**
 * @author anechaev
 * @since 23.04.2023
 */
public class ListInterpreterTest {

    @Test
    public void testSimpleExample() {
        LaconProgramState result = LaconUtils.exec("{"
            + "a : int = -1 + 4;"
            + "some_var : list = [a + 6, 2 * 21.25, \"ke,k\" * 2] + false;"
            + "}");

        LaconVariable some_varVariable = result.getVar("some_var");
        assertNotNull(some_varVariable);
        assertThat(some_varVariable.getType(), equalTo(LaconBuiltInType.LIST));

        List<Object> varContent = (List<Object>) some_varVariable.getValue();
        assertThat(varContent.size(), equalTo(4));

        Object el0 = varContent.get(0);
        assertTrue(el0 instanceof Long);
        assertThat(el0, equalTo(9L));

        Object el1 = varContent.get(1);
        assertTrue(el1 instanceof Double);
        assertThat((double)el1, closeTo(42.5, EPSILON));

        Object el2 = varContent.get(2);
        assertTrue(el2 instanceof String);
        assertThat(el2, equalTo("ke,kke,k"));

        Object el3 = varContent.get(3);
        assertTrue(el3 instanceof Boolean);
        assertFalse((boolean) el3);
    }
}
