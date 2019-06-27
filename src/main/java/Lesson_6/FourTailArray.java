package Lesson_6;

public class FourTailArray {

    public static int[] getFourTailArray(int[] in) throws RuntimeException {
        if (in.length == 0)
            throw new RuntimeException("empty array");
        for (int i = in.length-1; i >= 0 ; i--) {
            if (in[i] == 4) {

                if (i == in.length-1)
                    return new int[]{};

                int[] newarr = new int[in.length-i-1];
                System.arraycopy(in, i+1, newarr, 0, in.length-i-1);
                return newarr;
            }
        }
        throw new RuntimeException("couldn't find any digit 4 in input (or digit 4 is the last in array)");
    }
}
