package ua.nechay.lacon.interpreter;

import org.junit.Test;
import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.var.LaconVariable;
import ua.nechay.lacon.utils.LaconUtils;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static ua.nechay.lacon.TestUtils.EPSILON;
import static ua.nechay.lacon.TestUtils.assertVariable;
import static ua.nechay.lacon.TestUtils.getProgram;

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

        Object el0 = ((LaconValue<?>)varContent.get(0)).getValue();
        assertTrue(el0 instanceof Long);
        assertThat(el0, equalTo(9L));

        Object el1 = ((LaconValue<?>)varContent.get(1)).getValue();
        assertTrue(el1 instanceof Double);
        assertThat((double)el1, closeTo(42.5, EPSILON));

        Object el2 = ((LaconValue<?>)varContent.get(2)).getValue();
        assertTrue(el2 instanceof String);
        assertThat(el2, equalTo("ke,kke,k"));

        Object el3 = ((LaconValue<?>)varContent.get(3)).getValue();
        assertTrue(el3 instanceof Boolean);
        assertFalse((boolean) el3);
    }

    @Test
    public void addingElementsToList() {
        LaconProgramState result = LaconUtils.exec("{"
            + "a : list = 1 + [2, 3];"
            + "b : list = [1, 2] + 3;"
            + "}");
        LaconVariable aVariable = result.getVar("a");
        assertNotNull(aVariable);
        assertThat(aVariable.getType(), equalTo(LaconBuiltInType.LIST));

        List<Object> varContent = (List<Object>) aVariable.getValue();
        assertThat(varContent.size(), equalTo(3));

        LaconValue<?> aEl0 = (LaconValue<?>) varContent.get(0);
        assertThat(aEl0.getValue(), equalTo(1L));

        LaconValue<?> aEl1 = (LaconValue<?>) varContent.get(1);
        assertThat(aEl1.getValue(), equalTo(2L));

        LaconValue<?> aEl2 = (LaconValue<?>) varContent.get(2);
        assertThat(aEl2.getValue(), equalTo(3L));

        LaconValue<?> bEl0 = (LaconValue<?>) varContent.get(0);
        assertThat(bEl0.getValue(), equalTo(1L));

        LaconValue<?> bEl1 = (LaconValue<?>) varContent.get(1);
        assertThat(bEl1.getValue(), equalTo(2L));

        LaconValue<?> bEl2 = (LaconValue<?>) varContent.get(2);
        assertThat(bEl2.getValue(), equalTo(3L));
    }

    @Test
    public void testSimpleGetByIndex() throws IOException {
        String program = getProgram();
        LaconProgramState result = LaconUtils.exec(program);

        assertVariable(result, "a", LaconBuiltInType.INT, 1L);
        assertVariable(result, "b", LaconBuiltInType.INT, 2L);
        assertVariable(result, "c", LaconBuiltInType.BOOLEAN, true);
        assertVariable(result, "d", LaconBuiltInType.REAL, 3.14);
        assertVariable(result, "e", LaconBuiltInType.STRING, "kek");
        assertVariable(result, "f0", LaconBuiltInType.BOOLEAN, false);
        assertVariable(result, "f1", LaconBuiltInType.BOOLEAN, false);
    }
}
