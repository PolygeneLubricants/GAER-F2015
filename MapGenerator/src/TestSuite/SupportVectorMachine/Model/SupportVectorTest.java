package TestSuite.SupportVectorMachine.Model;

import SupportVectorMachine.Model.SupportVector;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Andreas on 23/3/2015.
 */
public class SupportVectorTest {
    @Test
    public void testGet() {
        short[][] matrix = new short[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        short[] expected = new short[] {1, 2, 3, 4, 5, 6, 7, 8, 9};
        SupportVector vector = new SupportVector(matrix);
        short testValue = vector.getVector(1, 1);
        Assert.assertEquals(expected[4], testValue);
        testValue = vector.getVector(0, 0);
        Assert.assertEquals(expected[0], testValue);
        testValue = vector.getVector(2, 2);
        Assert.assertEquals(expected[8], testValue);

        vector.setVector(1, 2, (short)42);
        Assert.assertEquals(42, vector.getVector(1, 2));
    }
}
