package Lesson_7.MyUnitTests;

public class Sample {

    @Test
    void test_1() {
        System.out.println("running default priority test");
    }

    @Test
    void test_4() {
        System.out.println("running default priority test 2");
    }

    @Test(priority = 3)
    void test_2() {
        System.out.println("running high priority test");
    }

    @Test(priority = 10)
    void test_3() {
        System.out.println("running low priority test");
    }

    @BeforeSuite
    void pre_test() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> BEGIN >>>>>>>>>>>>>>>>>>>>>>>>");
    }

    @AfterSuite
    void post_test() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>  END  >>>>>>>>>>>>>>>>>>>>>>>>");
    }

}
