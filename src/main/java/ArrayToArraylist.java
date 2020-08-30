import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayToArraylist {

    public static void main(String[] args) {

        Integer[] arr1 = {1, 2, 3, 4, 5, 6, 7};
        String[] arr2 = {"A", "B", "C"} ;

        arrayToArraylist(arr1);
        arrayToArraylist(arr2);
    }

    private static <T> void arrayToArraylist(T [] arr) {

        List<T> list = new ArrayList<>(Arrays.asList(arr));
        System.out.println("Arraylist: " + list.toString() + ", " + list.getClass());
    }
}
