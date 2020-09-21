import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Lesson7 {

    public static void main(String[] args) throws Exception {

        Class aClass = TestClass.class;;
        start(aClass);
    }

    static void start(Class c) throws Exception {

        Object testClass = c.newInstance();
        Method[] methods = c.getDeclaredMethods();
        List<Method> methodList = new ArrayList<>();
        Method beforeMethod = null;
        Method afterMethod = null;

        for (Method m : methods) {
            if (m.isAnnotationPresent(Test.class)) {
                methodList.add(m);
            }
            if (m.isAnnotationPresent(BeforeSuite.class)) {
                if (beforeMethod == null) {
                    beforeMethod = m;
                }
                else {
                    throw new RuntimeException("Больше одного метода с аннотацией BeforeSuite");
                }
            }
            if (m.isAnnotationPresent(AfterSuite.class)) {
                if (afterMethod == null) {
                    afterMethod = m;
                }
                else {
                    throw new RuntimeException("Больше одного метода с аннотацией AfterSuite");
                }
            }
            methodList.sort(new Comparator<Method>() {
                @Override
                public int compare(Method m1, Method m2) {
                    return m2.getAnnotation(Test.class).priority() - m1.getAnnotation(Test.class).priority();
                }
            });
        }

        if (beforeMethod != null) {
            beforeMethod.invoke(testClass, null);
        }
        for (Method o : methodList) {
            o.invoke(testClass, null);
        }
        if (afterMethod != null) {
            afterMethod.invoke(testClass, null);
        }
    }
}
