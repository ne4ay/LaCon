package ua.nechay.lacon.stuff;

import org.junit.Test;

import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.val.IntLaconValue;
import ua.nechay.lacon.core.val.DictLaconValue;
import ua.nechay.lacon.core.val.StringLaconValue;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author anechaev
 * @since 07.05.2023
 */
public class MapValueGetByIndexTest {

    @Test
    public void testGetByIndexIfThereIsAnotherObject() {
        Map<LaconValue<?>, LaconValue<?>> map = new HashMap<>();
        LaconValue<?> expectedValue = new IntLaconValue(3L);
        map.put(new StringLaconValue("abc"), expectedValue);
        LaconValue<?> dictValue = new DictLaconValue(map);

        String abc = new String("abc");
        LaconValue<?> realValue = dictValue.getByIndex(new StringLaconValue(abc));
        assertThat(realValue, equalTo(expectedValue));
    }
}
