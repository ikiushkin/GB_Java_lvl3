import java.util.Arrays;

public class SwapTwoElements {

    public static void main(String[] args) {

        Integer[] arr1 = {1, 2, 3, 4, 5, 6, 7};
        String[] arr2 = {"A", "B", "C"} ;
        String[] arr3 = {} ;

        swapTwoElements(arr1,2,88);
        swapTwoElements(arr2,0,2);
        swapTwoElements(arr3,0,2);
    }

    public static <T> void swapTwoElements(T[] arr, int n1, int n2){
        if (arr.length >= 2) {
            if (n1 != n2 && n1 >= 0 && n2 >= 0) {
                if (n1 <= arr.length && n2 <= arr.length) {
                    System.out.println("Исходный массив: " + Arrays.toString(arr));
                    T t = arr[n1];
                    arr[n1] = arr[n2];
                    arr[n2] = t;
                    System.out.println("Итоговый массив: " + Arrays.toString(arr) + "\n");
                } else {
                    System.out.println("Введен индекс, превышающий размер массива" + "\n");
                }
            } else {
                System.out.println("Введен отрицательный индекс" + "\n");
            }
        } else {
            System.out.println("Массив пустой, либо содержит одно значение" + "\n");
        }
    }
}
