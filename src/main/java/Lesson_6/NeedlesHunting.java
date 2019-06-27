package Lesson_6;

public class NeedlesHunting {
    public static boolean hasNeedles(int[] in) {
        for (int i = 0; i < in.length; i++) {
            if (in[i] == 1 || in[i]==4)
                return true;
        }
        return false;
    }
}
