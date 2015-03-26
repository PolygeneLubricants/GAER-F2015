package TestSuite.SupportVectorMachine;

import Preprocessor.Parser;
import SupportVectorMachine.Model.SupportVector;
import SupportVectorMachine.Model.SvmNodeMatrix;
import SupportVectorMachine.Trainer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by Andreas on 24/3/2015.
 */
public class TrainerTest {
    @Test
    public void testSvmNodeMatrixConversion() {
        Parser p = new Parser();
        short[][] matrix = new short[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        SupportVector[] result = p.parse(matrix, 1, 1);
        Trainer t = new Trainer();
        SvmNodeMatrix nodeMatrix = t.toSvmNodeMatrix(result);
        Assert.assertEquals(9, nodeMatrix.get_length());
        Assert.assertEquals(9, nodeMatrix.get_classification().length);
        Assert.assertEquals(9, nodeMatrix.get_matrix().length);
        Assert.assertEquals(0, nodeMatrix.get_matrix()[0][0].index);
        Assert.assertEquals(1.0, nodeMatrix.get_matrix()[0][0].value, 0.0);

        result = p.parse(matrix, 3,3);
        nodeMatrix = t.toSvmNodeMatrix(result);
        Assert.assertEquals(1, nodeMatrix.get_length());
        Assert.assertEquals(1, nodeMatrix.get_classification().length);
        Assert.assertEquals(1, nodeMatrix.get_matrix().length);
        Assert.assertEquals(10, nodeMatrix.get_matrix()[0].length);
        Assert.assertEquals(7, nodeMatrix.get_matrix()[0][7].index);
        Assert.assertEquals(8, nodeMatrix.get_matrix()[0][7].value, 0);
        Assert.assertEquals(-1, nodeMatrix.get_matrix()[0][9].index);
    }

    @Test
    public void testRun() {
        Parser p = new Parser();
        short[][] altitudeMap = new short[0][];
        try {
            altitudeMap = p.read("./data/raw/N32/N52E007.hgt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        SupportVector[] vectors = p.parse(altitudeMap, 10, 10);
        Trainer t = new Trainer();
        try {
            t.run(vectors, "testRun.model");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCrossValidate() {

    }
}
