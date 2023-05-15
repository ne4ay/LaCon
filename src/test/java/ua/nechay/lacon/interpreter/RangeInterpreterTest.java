package ua.nechay.lacon.interpreter;

import org.junit.Test;
import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.val.ListLaconValue;
import ua.nechay.lacon.core.var.LaconVariable;
import ua.nechay.lacon.utils.LaconUtils;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static ua.nechay.lacon.TestUtils.assertVariable;
import static ua.nechay.lacon.TestUtils.extractList;
import static ua.nechay.lacon.TestUtils.getProgram;

/**
 * @author anechaev
 * @since 13.05.2023
 */
public class RangeInterpreterTest {

    @Test
    public void testTrivialRanges() throws IOException {
        String program = getProgram();
        LaconProgramState result = LaconUtils.exec(program);

        assertVariable(result, "ten", LaconBuiltInType.INT, 10L);

        List<LaconValue<?>> ran1List = extractList(result, "ran1");

        assertThat(ran1List.size(), equalTo(10));
        assertThat(ran1List.get(0).getValue(), equalTo(0L));
        assertThat(ran1List.get(1).getValue(), equalTo(1L));
        assertThat(ran1List.get(9).getValue(), equalTo(9L));

        List<LaconValue<?>> ran2List = extractList(result, "ran2");

        assertThat(ran2List.size(), equalTo(11));
        assertThat(ran2List.get(0).getValue(), equalTo(-1L));
        assertThat(ran2List.get(1).getValue(), equalTo(0L));
        assertThat(ran2List.get(10).getValue(), equalTo(9L));

        List<LaconValue<?>> ran3List = extractList(result, "ran3");

        assertThat(ran3List.size(), equalTo(11));
        assertThat(ran3List.get(0).getValue(), equalTo(-10L));
        assertThat(ran3List.get(1).getValue(), equalTo(-8L));
        assertThat(ran3List.get(10).getValue(), equalTo(10L));
    }
}
