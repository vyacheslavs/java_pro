package Lesson_6;

import org.junit.Assert;
import org.junit.Test;

public class FourTailArrayTest {
    @Test
    public void testFourTailArray_1() {

        int[] in = new int[]{1, 2, 3, 4, 5, 6};
        int[] out = new int[]{5, 6};

        Assert.assertArrayEquals(out, FourTailArray.getFourTailArray(in));
    }

    @Test
    public void testFourTailArray_2() {
        int[] in = new int[]{1, 4, 3, 0, 5, 6};
        int[] out = new int[]{3, 0, 5, 6};

        Assert.assertArrayEquals(out, FourTailArray.getFourTailArray(in));
    }

    @Test(expected = RuntimeException.class)
    public void testFourTailArray_3() {
        int[] in = new int[]{};
        FourTailArray.getFourTailArray(in);
    }

    @Test
    public void testFourTailArray_4() {
        int[] in = new int[]{1, 2, 3, 4};
        int[] out = new int[]{};
        Assert.assertArrayEquals(out,FourTailArray.getFourTailArray(in));
    }

    @Test(expected = RuntimeException.class)
    public void testFourTailArray_5() {
        int[] in = new int[]{1, 2, 3, 1};
        FourTailArray.getFourTailArray(in);
    }

}
