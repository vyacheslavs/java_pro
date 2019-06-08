package Lesson_1;
import java.util.ArrayList;
import java.util.Arrays;

class Converter<T> {

    static <T> ArrayList<T> toArrayList(T[] arr) {
        return new ArrayList<>(Arrays.asList(arr));
    }

}

public class ToArrayListConverter {
    public static void main(String[] args) {

        Integer [] arr = new Integer[] {100,200,300};
        ArrayList<Integer> ar = Converter.toArrayList(arr);

        for (Integer i : ar) {
            System.out.println(i);
        }
    }
}
