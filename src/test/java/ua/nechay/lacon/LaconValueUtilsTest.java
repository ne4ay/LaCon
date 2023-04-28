package ua.nechay.lacon;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static ua.nechay.lacon.core.LaconValueUtils.multipleStrings;

/**
 * @author anechaev
 * @since 21.04.2023
 */
public class LaconValueUtilsTest {

    @Test
    public void testMultipleStrings() {
        {
            String text = "abc";
            long times = 1;
            String actualString = multipleStrings(text, times);
            assertThat(actualString, equalTo("abc"));
        }
        {
            String text = "abc";
            long times = 3;
            String actualString = multipleStrings(text, times);
            assertThat(actualString, equalTo("abcabcabc"));
        }
        {
            String text = "abc";
            long times = 0;
            String actualString = multipleStrings(text, times);
            assertThat(actualString, equalTo(""));
        }
        {
            String text = "abc";
            long times = -1;
            String actualString = multipleStrings(text, times);
            assertThat(actualString, equalTo("cba"));
        }
        {
            String text = "abc";
            long times = -3;
            String actualString = multipleStrings(text, times);
            assertThat(actualString, equalTo("cbacbacba"));
        }
    }

}
