package Lesson_7.HomeWorkChecking;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.StringJoiner;

class Main {

    /**
     * PLEASE READ BEFORE RUN!
     *
     * You need to be sure you have run java classes in HomeWorkFolder in order to generate class files.
     * This application works only for generated classes.
     *
     * Best regards.
     */

    static void checkHomeWork(String folder) throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException {
        File dir = new File(folder);

        String basePath = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String packageName = folder;
        packageName = packageName.replace(basePath, "");


        StringJoiner j = new StringJoiner(".");
        for (String s : packageName.split("/")) {
            j.add(s);
        }
        packageName = j.toString();

        for (File file : dir.listFiles()) {
            if (file.getName().endsWith((".class"))) {

                File base = new File(basePath);

                URL[] urls = new URL[] {base.toURI().toURL()};
                ClassLoader classLoader = new URLClassLoader(urls);

                String className = file.getName().replaceFirst("[.][^.]+$", "");
                Class cls = classLoader.loadClass(packageName+"."+className);
                System.out.println("------------------------------- "+packageName+"."+className+" -------------------------------");

                Object o = cls.newInstance();

                for (Method m : cls.getDeclaredMethods()) {
                    m.setAccessible(true);

                    if (m.getName().equals("calculate") && m.getReturnType() == int.class) {
                        System.out.println("checking method int calculate(int a, int b, int c, int d)");

                        Object rv = m.invoke(o, 1,2,8,2);

                        System.out.println(((Integer)rv == 6)? "check passed" : "check failed");
                    }
                    if (m.getName().equals("checkTwoNumbers") && m.getReturnType() == boolean.class) {
                        System.out.println("checking method boolean checkTwoNumbers(int first, int second)");
                        Object rv = m.invoke(o, 5,5);

                        System.out.println(((Boolean) rv)? "check passed" : "check failed");
                    }

                    if (m.getName().equals("isNegative") && m.getReturnType() == boolean.class) {
                        System.out.println("checking method boolean isNegative(int variable)");
                        Object rv = m.invoke(o, -1);

                        System.out.println(((Boolean) rv)? "check passed" : "check failed");
                    }

                    if (m.getName().equals("isLeapYear") && m.getReturnType() == boolean.class) {
                        System.out.println("checking method boolean isLeapYear(int year)");
                        Object rv = m.invoke(o, 2004);

                        System.out.println(((Boolean) rv)? "check passed" : "check failed");
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            checkHomeWork(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()+"Lesson_7/HomeWorkChecking/HomeWorkFolder");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}