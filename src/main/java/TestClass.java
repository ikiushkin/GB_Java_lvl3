public class TestClass {

    @Test(priority = 3)
    void test1() {
        System.out.println("Test 1");
    };

    @Test(priority = 5)
    void test2() {
        System.out.println("Test 2");
    };

    @Test(priority = 8)
    void test3() {
        System.out.println("Test 3");
    };

    @BeforeSuite
    void BeforeSuiteMethod() {
        System.out.println("BeforeSuiteMethod");
    };

    @AfterSuite
    void AfterSuiteMethod() {
        System.out.println("AfterSuiteMethod");
    };
}
