import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class MyWorkingWithArrayMethodsTest {

    private MyWorkingWithArrayMethods methodsTest;

    private final int[] BEFORE = new int[]{1, 2, 4, 4, 2, 3, 4, 1, 7};
    private final int[] AFTER = new int[]{1, 7};
    private final int[] WRONG = new int[]{3, 8};
    private final int[] WRONG_EX = new int[]{};

    @BeforeEach
    public void init() {
        methodsTest = new MyWorkingWithArrayMethods();
        Assertions.assertNotNull(methodsTest);
    }

    @Test
    public void cropArrayAfterFourTest() {
        Assertions.assertNotNull(BEFORE);
        Assertions.assertNotNull(AFTER);
        Assertions.assertArrayEquals(AFTER, methodsTest.cropArrayAfterFour(BEFORE));
        Assertions.assertNotEquals(WRONG, methodsTest.cropArrayAfterFour(BEFORE));
        Assertions.assertThrows(RuntimeException.class, () -> {
            methodsTest.cropArrayAfterFour(WRONG_EX);
        });
    }

    @Test
    public void arrayOfOneOrFour() {
        Assertions.assertNotNull(BEFORE);
        Assertions.assertTrue(methodsTest.arrayOfOneOrFour(BEFORE));
        Assertions.assertNotEquals(false, methodsTest.arrayOfOneOrFour(BEFORE));
        Assertions.assertThrows(RuntimeException.class, () -> {
            methodsTest.cropArrayAfterFour(WRONG_EX);
        });
    }
}
