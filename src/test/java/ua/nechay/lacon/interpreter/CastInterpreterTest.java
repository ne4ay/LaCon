package ua.nechay.lacon.interpreter;

import org.junit.Test;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconType;
import ua.nechay.lacon.core.var.LaconVariable;
import ua.nechay.lacon.utils.LaconUtils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

/**
 * @author anechaev
 * @since 22.04.2023
 */
public class CastInterpreterTest {

    @Test
    public void testFromIntCasting() {
        {
            LaconProgramState result = LaconUtils.exec("{a : int = int(-4);}");
            LaconVariable aVariable = result.getVar("a");
            assertNotNull(aVariable);
            assertThat(aVariable.getValue(), equalTo(-4L));
            assertThat(aVariable.getType(), equalTo(LaconType.INT));
        }
        {
            LaconProgramState result = LaconUtils.exec("{a : real = real(-4);}");
            LaconVariable aVariable = result.getVar("a");
            assertNotNull(aVariable);
            assertThat(aVariable.getValue(), equalTo(-4.));
            assertThat(aVariable.getType(), equalTo(LaconType.REAL));
        }
        {
            LaconProgramState result = LaconUtils.exec("{a : str = str(-4);}");
            LaconVariable aVariable = result.getVar("a");
            assertNotNull(aVariable);
            assertThat(aVariable.getValue(), equalTo("-4"));
            assertThat(aVariable.getType(), equalTo(LaconType.STRING));
        }
        {
            LaconProgramState result = LaconUtils.exec("{a : bool = bool(-4);}");
            LaconVariable aVariable = result.getVar("a");
            assertNotNull(aVariable);
            assertThat(aVariable.getValue(), equalTo(true));
            assertThat(aVariable.getType(), equalTo(LaconType.BOOLEAN));
        }
    }

    @Test
    public void testFromRealCasting() {
        {
            LaconProgramState result = LaconUtils.exec("{a : int = int(12.34);}");
            LaconVariable aVariable = result.getVar("a");
            assertNotNull(aVariable);
            assertThat(aVariable.getValue(), equalTo(12L));
            assertThat(aVariable.getType(), equalTo(LaconType.INT));
        }
        {
            LaconProgramState result = LaconUtils.exec("{a : real = real(12.34);}");
            LaconVariable aVariable = result.getVar("a");
            assertNotNull(aVariable);
            assertThat(aVariable.getValue(), equalTo(12.34));
            assertThat(aVariable.getType(), equalTo(LaconType.REAL));
        }
        {
            LaconProgramState result = LaconUtils.exec("{a : str = str(12.34);}");
            LaconVariable aVariable = result.getVar("a");
            assertNotNull(aVariable);
            assertThat(aVariable.getValue(), equalTo("12.34"));
            assertThat(aVariable.getType(), equalTo(LaconType.STRING));
        }
        {
            try {
                LaconUtils.exec("{a : bool = bool(12.34);}");
                throw new IllegalStateException("The interpreter should fail");
            } catch (Exception e) {
                assertNotNull(e);
            }
        }
    }
}
