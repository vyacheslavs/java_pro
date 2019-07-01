package Lesson_7.MyUnitTests;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;


public class RunTests {

    public static void start(String className) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> clazz = Class.forName(className);
        start(clazz);
    }

    public static void start(Class<?> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<?> ctor = clazz.getConstructor();
        Object object = ctor.newInstance();
        run(object);
    }

    public static <ClassType> void run(ClassType obj) throws InvocationTargetException, IllegalAccessException {
        Class c = obj.getClass();
        Method pre_test = null;
        Method post_test = null;
        HashMap<Integer, ArrayList<Method> > ordered_methods = new HashMap<>();

        for (Method m : c.getDeclaredMethods()) {
            if (m.isAnnotationPresent(BeforeSuite.class)) {
                pre_test = m;
            } else if (m.isAnnotationPresent(AfterSuite.class)) {
                post_test = m;
            } else if (m.isAnnotationPresent(Test.class)) {

                Integer prio = m.getAnnotation(Test.class).priority();

                ordered_methods.put(prio,
                        ordered_methods.getOrDefault(prio, new ArrayList<Method>()));
                ordered_methods.get(prio).add(m);
            }
        }

            if (pre_test!=null)
                pre_test.invoke(obj);

            for (ArrayList<Method> ms : ordered_methods.values()) {
                for (Method m : ms)
                    m.invoke(obj);
            }

            if (post_test!=null)
                post_test.invoke(obj);
    }
}

