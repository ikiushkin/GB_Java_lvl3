import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class Lesson4_Lambda {

    public static void main(String[] args) {

        Integer[] arr = {2, 3, 3, 5, 555};

        // Задание 1
        MyLambdaClass<Integer> search1 = (x, y) -> {
            for (int i = 0; i < y.length; i++) {
                if (x.equals(y[i])) {
                    return i;
                }
            }
            return -1;
        };

        // Задание 2
        UnaryOperator<String> reverse;
        reverse = x -> new StringBuilder(x).reverse().toString();

        // Задание 3
        Function<Integer[], Integer> maximum;
        maximum = LambdaMaximum::maximum;

        // Задание 4
        Function<List<Integer>, Double> average;
        average = x -> {
            int sum = 0;
            double y = 0;
            Integer[] intArr = (Integer[]) x.toArray();
            for (int i: intArr) {
                sum += i;
            }
            return (double) (sum / intArr.length);
        };

        //Задание 5
        UnaryOperator<List<String>> search2 = x -> {
            List<String> result = new ArrayList<>();
            for (String s: x) {
                if (s.startsWith("a") && s.length() == 3) {
                    result.add(s);
                }
            }
            return result;
        };
    }
}
