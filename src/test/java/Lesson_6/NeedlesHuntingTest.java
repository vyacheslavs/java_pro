package Lesson_6;

import org.junit.Assert;
import org.junit.Test;

public class NeedlesHuntingTest {
    @Test
    public void hunt_1() {
        int [] in = new int[] {2,2,2,2,3,3,3,3,3,7,7,7,7,7};
        Assert.assertFalse( NeedlesHunting.hasNeedles(in));
    }

    @Test
    public void hunt_2() {
        int [] in = new int[] {};
        Assert.assertFalse(NeedlesHunting.hasNeedles(in));
    }

    @Test
    public void hunt_3() {
        int [] in = new int[] {0,0,0,0,0,0,1,2,2,2,2,2,2,2};
        Assert.assertTrue(NeedlesHunting.hasNeedles(in));
    }

    @Test
    public void hunt_4() {
        int [] in = new int[] {0,0,0,0,0,0,9,2,2,2,2,2,2,2,4};
        Assert.assertTrue(NeedlesHunting.hasNeedles(in));
    }
}
