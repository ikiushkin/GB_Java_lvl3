public class LambdaMaximum {

    static int maximum(Integer[] x) {
        int max = 0;
        for (int i = 1; i < x.length; i++) {
            max = Math.max(max, x[i]);
        }
        return max;
    }
}
