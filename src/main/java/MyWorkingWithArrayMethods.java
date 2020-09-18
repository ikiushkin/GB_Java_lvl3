import java.util.Arrays;

public class MyWorkingWithArrayMethods {

    public int[] cropArrayAfterFour(int[] arr) throws RuntimeException {
        int lastIndex = 0;
        String str = Arrays.toString(arr);
        if (str.contains(String.valueOf(4))) {
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] == 4) {
                    lastIndex = i;
                }
            }
            int[] resultArray = new int[arr.length - lastIndex - 1];
            System.arraycopy(arr, lastIndex + 1, resultArray, 0, resultArray.length);
            return resultArray;
        } else {
            throw new RuntimeException("В массиве отсутствует цифра 4");
        }
    }

    public boolean arrayOfOneOrFour(int[] arr) {
        if (arr.length > 2) {
            boolean one = false;
            boolean four = false;
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] == 1) {
                    one = true;
                } else if (arr[i] == 4) {
                    four = true;
                }
            }
            return one && four;
        } else  {
            throw new RuntimeException("В массиве менее 2 чисел");
        }
    }
}
