package ua.nechay.lacon.parser;

import org.junit.Test;
import ua.nechay.lacon.LaconTokenType;
import ua.nechay.lacon.mock.MockScanner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author anechaev
 * @since 15.04.2023
 */
public class RealParserTest {

    @Test
    public void testParsingNumbersWithUnderscore() {
        assertTrue(LaconTokenType.REAL.matches(new MockScanner(".123")));
        assertTrue(LaconTokenType.REAL.matches(new MockScanner("0.321")));
        assertTrue(LaconTokenType.REAL.matches(new MockScanner("10.32")));
        assertTrue(LaconTokenType.REAL.matches(new MockScanner("12_234.123_432")));
        assertTrue(LaconTokenType.REAL.matches(new MockScanner("123_314.")));

        assertFalse(LaconTokenType.REAL.matches(new MockScanner("_0.123")));
        assertFalse(LaconTokenType.REAL.matches(new MockScanner("123_.321")));
        assertFalse(LaconTokenType.REAL.matches(new MockScanner("123._321")));
        assertFalse(LaconTokenType.REAL.matches(new MockScanner("123.321_")));

    }
}
