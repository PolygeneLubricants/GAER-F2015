package TestSuite.SupportVectorMachine;

import Preprocessor.Parser;
import SupportVectorMachine.Model.LinearNodeMatrix;
import SupportVectorMachine.Model.SupportVector;
import SupportVectorMachine.Trainers.LinearTrainer;
import de.bwaldvogel.liblinear.FeatureNode;
import de.bwaldvogel.liblinear.Problem;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Andreas on 24/3/2015.
 */
public class LinearTrainerTest {
    @Test
    public void testSvmNodeMatrixConversion() {
        Parser p = new Parser();
        short[][] matrix = new short[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        SupportVector[] result = p.parse(matrix, 1, 1, 0);
        LinearTrainer t = new LinearTrainer();
        LinearNodeMatrix nodeMatrix = t.toSvmNodeMatrix(result);
        Assert.assertEquals(9, nodeMatrix.get_length());
        Assert.assertEquals(9, nodeMatrix.get_classification().length);
        Assert.assertEquals(9, nodeMatrix.get_matrix().length);
        Assert.assertEquals(1, nodeMatrix.get_matrix()[0][0].getIndex());
        Assert.assertEquals(1.0, nodeMatrix.get_matrix()[0][0].getValue(), 0.0);

        result = p.parse(matrix, 3, 3, 0);
        nodeMatrix = t.toSvmNodeMatrix(result);
        Assert.assertEquals(1, nodeMatrix.get_length());
        Assert.assertEquals(1, nodeMatrix.get_classification().length);
        Assert.assertEquals(1, nodeMatrix.get_matrix().length);
        Assert.assertEquals(9, nodeMatrix.get_matrix()[0].length);
        Assert.assertEquals(8, nodeMatrix.get_matrix()[0][7].getIndex());
        Assert.assertEquals(8, nodeMatrix.get_matrix()[0][7].getValue(), 0);
    }

    @Test
    public void testMemorySettings() {
        System.out.println(System.getProperty("sun.arch.data.model"));
        System.out.println(Runtime.getRuntime().maxMemory());
    }

    @Test
    public void testPredict() {
        short[][] matrix = new short[][]{
                { 1,  2,  3,  4,  5,  6,  7,  8,  9, 10},
                { 4,  5,  6,  7,  8,  9, 10, 11, 12, 13},
                { 7,  8,  9, 10, 11, 12, 13, 14, 15, 16},
                { 8,  9, 10, 11, 12, 13, 14, 15, 16, 17},
                { 9, 10, 11, 12, 13, 14, 15, 16, 17, 18},
                {10, 11, 12, 13, 14, 15, 16, 17, 18, 19},
                {11, 12, 13, 14, 15, 16, 17, 18, 19, 20},
                {12, 13, 14, 15, 16, 17, 18, 19, 20, 21},
                {13, 14, 15, 16, 17, 18, 19, 20, 21, 22},
                {14, 15, 16, 17, 18, 19, 20, 21, 22, 23},
                {15, 16, 17, 18, 19, 20, 21, 22, 23, 24}
        };
        Parser p = new Parser();

        SupportVector[] vectors = p.parse(matrix, 3, 3, 0);
        LinearTrainer t = new LinearTrainer();
        t.run(vectors);

        short[][] predict = new short[][]{
                { 1,  42,  3,  4,  5,  6,  7,  8,  9, 10},
                { 4,  5,  6,  7,  8,  9, 10, 11, 12, 13},
                { 7,  8,  9, 10, 11, 12, 13, 14, 15, 16},
                { 8,  9, 10, 11, 12, 13, 14, 15, 16, 17},
                { 9, 10, 11, 12, 13, 14, 15, 16, 17, 18},
                {10, 11, 12, 13, 42, 15, 16, 17, 18, 19},
                {11, 12, 13, 14, 15, 16, 17, 18, 19, 20},
                {12, 13, 14, 15, 16, 17, 18, 19, 20, 21},
                {13, 14, 15, 16, 17, 18, 19, 120, 121, 122},
                {14, 15, 16, 17, 18, 19, 42, 121, 122, 123},
                {15, 16, 17, 18, 19, 20, 21, 122, 123, 124}
        };

        LinearNodeMatrix cvMatrix = t.toSvmNodeMatrix(p.parse(predict, 3, 3, 0));

        double[] predictions = t.predict(cvMatrix);
        System.out.println(Arrays.toString(predictions));
        Problem problem = t.getProblem();

        int totalCorrect = 0;
        for(int i = 0; i< predictions.length; i++)
            if(predictions[i] == problem.y[i])
                totalCorrect++;

        System.out.print("Prediction Accuracy = " + 100.0 * totalCorrect/ predictions.length + "%\n");
    }

    @Test
    public void testRunSmall() {
        short[][] matrix = new short[][]{
                { 1,  2,  3,  4,  5,  6,  7,  8,  9, 10},
                { 4,  5,  6,  7,  8,  9, 10, 11, 12, 13},
                { 7,  8,  9, 10, 11, 12, 13, 14, 15, 16},
                { 8,  9, 10, 11, 12, 13, 14, 15, 16, 17},
                { 9, 10, 11, 12, 13, 14, 15, 16, 17, 18},
                {10, 11, 12, 13, 14, 15, 16, 17, 18, 19},
                {11, 12, 13, 14, 15, 16, 17, 18, 19, 20},
                {12, 13, 14, 15, 16, 17, 18, 19, 20, 21},
                {13, 14, 15, 16, 17, 18, 19, 20, 21, 22},
                {14, 15, 16, 17, 18, 19, 20, 21, 22, 23},
                {15, 16, 17, 18, 19, 20, 21, 22, 23, 24}
        };
        Parser p = new Parser();

        SupportVector[] vectors = p.parse(matrix, 3, 3, 0);
        LinearTrainer t = new LinearTrainer();
        try {
            t.run(vectors, "testLinearRunSmall.model");
        } catch (IOException e) {
            e.printStackTrace();
        }

        t.crossValidate();

        short[][] predict = new short[][]{
                { 1,  42,  3,  4,  5,  6,  7,  8,  9, 10},
                { 4,  5,  6,  7,  8,  9, 10, 11, 12, 13},
                { 7,  8,  9, 10, 11, 12, 13, 14, 15, 16},
                { 8,  9, 10, 11, 12, 13, 14, 15, 16, 17},
                { 9, 10, 11, 12, 13, 14, 15, 16, 17, 18},
                {10, 11, 12, 13, 42, 15, 16, 17, 18, 19},
                {11, 12, 13, 14, 15, 16, 17, 18, 19, 20},
                {12, 13, 14, 15, 16, 17, 18, 19, 20, 21},
                {13, 14, 15, 16, 17, 18, 19, 20, 21, 22},
                {14, 15, 16, 17, 18, 19, 42, 21, 22, 23},
                {15, 16, 17, 18, 19, 20, 21, 22, 23, 24}
        };

        SupportVector[] cvVectors = p.parse(predict, 3, 3, 0);
    }

    @Test
    public void testRunBig() {
        Parser p = new Parser();
        short[][] altitudeMap = new short[0][];
        try {
            altitudeMap = p.read("./data/raw/N32/N52E007.hgt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        SupportVector[] vectors = p.parse(altitudeMap, 10, 10, 0);
        LinearTrainer t = new LinearTrainer();
        try {
            t.run(vectors, "testLinearRunBig.model");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCrossValidate() {
        short[][] matrix = new short[][]{
                { 1,  2,  3,  4,  5,  6,  7,  8,  9, 10},
                { 4,  5,  6,  7,  8,  9, 10, 11, 12, 13},
                { 7,  8,  9, 10, 11, 12, 13, 14, 15, 16},
                { 8,  9, 10, 11, 12, 13, 14, 15, 16, 17},
                { 9, 10, 11, 12, 13, 14, 15, 16, 17, 18},
                {10, 11, 12, 13, 14, 15, 16, 17, 18, 19},
                {11, 12, 13, 14, 15, 16, 17, 18, 19, 20},
                {12, 13, 14, 15, 16, 17, 18, 19, 20, 21},
                {13, 14, 15, 16, 17, 18, 19, 20, 21, 22},
                {14, 15, 16, 17, 18, 19, 20, 21, 22, 23},
                {15, 16, 17, 18, 19, 20, 21, 22, 23, 24}
        };
        Parser p = new Parser();

        SupportVector[] vectors = p.parse(matrix, 3, 3, 0);
        LinearTrainer t = new LinearTrainer();
        t.run(vectors);

        t.crossValidate();
    }

    @Test
    public void testQuadraticPrediction() {
        // 1 = quadratic, -1 = non-quadratic
        double[] GROUPS_ARRAY = {1, 1, 1, 1, -1, -1, -1, -1};

        // quadratic
        FeatureNode[] tp1 = {new FeatureNode(1,  2), new FeatureNode(2,   4)};
        FeatureNode[] tp2 = {new FeatureNode(1,  4), new FeatureNode(2,   8)};
        FeatureNode[] tp3 = {new FeatureNode(1,  9), new FeatureNode(2,  81)};
        FeatureNode[] tp4 = {new FeatureNode(1, 10), new FeatureNode(2, 100)};

        // not quadratic
        FeatureNode[] tp5 = {new FeatureNode(1, 5), new FeatureNode(2, 6)};
        FeatureNode[] tp6 = {new FeatureNode(1, 3), new FeatureNode(2, 4)};
        FeatureNode[] tp7 = {new FeatureNode(1, 6), new FeatureNode(2, 9)};
        FeatureNode[] tp8 = {new FeatureNode(1, 4), new FeatureNode(2, 2)};

        // unknown
        FeatureNode[] up1 = {new FeatureNode(1, 32), new FeatureNode(2, 32)};
        FeatureNode[] up2 = {new FeatureNode(1,  5), new FeatureNode(2, 25)};
        FeatureNode[] up3 = {new FeatureNode(1,  4), new FeatureNode(2,  2)};

        FeatureNode[][] trainingSetWithUnknown = {
                tp1, tp2,  tp3, tp4, tp5, tp6, tp7, tp8
        };

        LinearTrainer t = new LinearTrainer();
        LinearNodeMatrix matrix = new LinearNodeMatrix(trainingSetWithUnknown, GROUPS_ARRAY, trainingSetWithUnknown.length);
        t.run(matrix);

        double p1 = t.predict(up1);
        double p2 = t.predict(up2);
        double p3 = t.predict(up3);

        Assert.assertEquals(-1, p1, 0.001);
        Assert.assertEquals(1, p2, 0.001);
        Assert.assertEquals(-1, p3, 0.001);

    }
}
