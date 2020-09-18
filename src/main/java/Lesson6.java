import java.util.Arrays;

public class Lesson6 {



    public static void main(String[] args) {

        int[] i = {1, 2, 4, 4, 2, 3, 4, 1, 7};
        int[] i1 = {7};
        int[] i2 = {};

        MyWorkingWithArrayMethods methods = new MyWorkingWithArrayMethods();

        System.out.println(Arrays.toString(methods.cropArrayAfterFour(i)));
        System.out.println(methods.arrayOfOneOrFour(i));

        //С исключениями
        //System.out.println(Arrays.toString(methods.cropArrayAfterFour(i1)));
        //System.out.println(Arrays.toString(methods.cropArrayAfterFour(i2)));
        //System.out.println(methods.arrayOfOneOrFour(i1));
        //System.out.println(methods.arrayOfOneOrFour(i2));
    }
}
