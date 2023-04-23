package ua.nechay.lacon.interpreter;

import org.junit.Test;
import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.var.LaconVariable;
import ua.nechay.lacon.utils.LaconUtils;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

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
    }
}
