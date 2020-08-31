import java.util.ArrayList;
import java.util.List;

public class ArrayToArraylist {

    public static void main(String[] args) {

        Integer[] arr1 = {1, 2, 3, 4, 5, 6, 7};
        String[] arr2 = {"A", "B", "C"} ;

        arrayToArraylist(arr1);
        arrayToArraylist(arr2);
    }

    private static <T> List<T> arrayToArraylist(T [] arr) {
        List<T> list = new ArrayList<>();
        for (T obj: arr) {
            list.add(obj);
        }
        System.out.println(list.toString());
        System.out.println(list.getClass());
        return list;
    }
}
