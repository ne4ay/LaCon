package ua.nechay.lacon;

import ua.nechay.lacon.core.LaconBuiltInType;
import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.LaconType;
import ua.nechay.lacon.core.LaconValue;
import ua.nechay.lacon.core.var.LaconVariable;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static ua.nechay.lacon.TestUtils.getPathOfTestResource;

/**
 * @author anechaev
 * @since 16.04.2023
 */
public final class TestUtils {
    public static final double EPSILON = 0.000_000_01;
    private static final String ARTIFACT_ID = "lacon";

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

    private static String[] getPathToProgram() {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];

        String fullMethodName = stackTraceElement.getMethodName();
        String methodNameWithoutTest = fullMethodName.replaceFirst("^test", "");

        String className = stackTraceElement.getClassName();
        String[] classIds = className.split("\\.");
        int lastIndex = classIds.length - 1;
        List<String> paths = new ArrayList<>();
        for (int i = lastIndex; i >= 0; i--) {
            if (ARTIFACT_ID.equals(classIds[i])) {
                break;
            }
            String nextId = classIds[i];
            if (i != lastIndex) {
                paths.add(0, nextId);
                continue;
            }
            StringBuilder builder = new StringBuilder();
            int countOfUpperCases = 0;
            for (var character : nextId.toCharArray()) {
                if (Character.isUpperCase(character)) {
                    countOfUpperCases++;
                }
                if (countOfUpperCases == 2) {
                    break;
                }
                builder.append(character);
            }
            paths.add(0, builder.toString().toLowerCase(Locale.US));
        }
        paths.add(methodNameWithoutTest + "." + ARTIFACT_ID);
        return paths.toArray(new String[0]);
    }

    public static String getProgram() throws IOException {
        return Files.readString(getPathOfTestResource(getPathToProgram()));
    }

    public static void assertVariable(LaconProgramState state, String varName, LaconType type, Object val) {
        LaconVariable var = state.getVar(varName);
        assertNotNull(var);
        assertThat(var.getType(), equalTo(type));
        assertThat(var.getValue(), equalTo(val));
    }

    public static void assertValue(@Nonnull LaconValue<?> value, @Nonnull LaconType expectedType, @Nonnull Object expectedValue) {
        assertNotNull(value);
        assertThat(value.getType(), equalTo(expectedType));
        assertThat(value.getValue(), equalTo(expectedValue));
    }
}
