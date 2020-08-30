import java.util.Arrays;

public class SwapTwoElements {

    public static void main(String[] args) {

        Integer[] arr1 = {1, 2, 3, 4, 5, 6, 7};
        String[] arr2 = {"A", "B", "C"} ;

        swapTwoElements(arr1,2,4);
        swapTwoElements(arr2,0,2);
    }

    public static void swapTwoElements(Object[] arr, int n1, int n2){

        System.out.println("Исходный массив: " + Arrays.toString(arr));
        Object obj = arr[n1];
        arr[n1] = arr[n2];
        arr[n2] = obj;
        System.out.println("Итоговый массив: " + Arrays.toString(arr) + "\n");
    }
}
