package ua.nechay.lacon;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author anechaev
 * @since 16.04.2023
 */
public final class TestUtils {
    public static final double EPSILON = 0.000_000_01;

    private TestUtils() { }

    public static Path getPathOfTestResource(String ... paths) {
        String[] arr = new String[2 + paths.length];
        arr[0] = "test";
        arr[1] = "resources";
        for (int i = 0; i < paths.length; i++) {
            arr[i + 2] = paths[i];
        }
        return Paths.get("src", arr);
    }

}
