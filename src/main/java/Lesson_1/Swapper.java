package Lesson_1;

class SwapException extends RuntimeException {
    SwapException(Throwable e) {
        super("Failed to swap", e);
    }
}

class Swap<T> {

    void swap(T[] arr, int element1, int element2) throws SwapException {
        try {
            T tmp = arr[element1];
            arr[element1] = arr[element2];
            arr[element2] = tmp;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new SwapException(e);
        }
    }

}

public class Swapper {

    public static void main(String[] args) {

        String[] arr_s = new String[] {"1","2","3"};

        Swap<String> swaper = new Swap<>();
        swaper.swap(arr_s,0, 2);
        for (String s : arr_s)
            System.out.println(s);

        Integer [] arr_i = new Integer[] {10,20,2020};

        Swap<Integer> int_swaper = new Swap<Integer>();
        int_swaper.swap(arr_i, 1,2);

        for (Integer i : arr_i) {
            System.out.println(i);
        }
    }

}
