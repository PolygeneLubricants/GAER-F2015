package TestSuite.SupportVectorMachine;

import Preprocessor.Parser;
import SupportVectorMachine.Model.SupportVector;
import SupportVectorMachine.Model.SvmNodeMatrix;
import SupportVectorMachine.Trainers.KernelTrainer;
import libsvm.svm_node;
import libsvm.svm_problem;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by Andreas on 24/3/2015.
 */
public class KernelTrainerTest {
    @Test
    public void testSvmNodeMatrixConversion() {
        Parser p = new Parser();
        short[][] matrix = new short[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        SupportVector[] result = p.parse(matrix, 1, 1);
        KernelTrainer t = new KernelTrainer();
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
    public void testMemorySettings() {
        System.out.println(System.getProperty("sun.arch.data.model"));
        System.out.println(Runtime.getRuntime().maxMemory());
    }

    @Test
    public void testDataFragment() {
        Parser p = new Parser();
        short[][] altitudeMap = new short[0][];
        try {
            altitudeMap = p.read("./data/raw/N32/N52E007.hgt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        altitudeMap = p.cut(altitudeMap, 0, 0, 50, 50);
        SupportVector[] vectors = p.parse(altitudeMap, 3, 3);
        KernelTrainer t = new KernelTrainer();
        try {
            t.run(vectors, "testKernelRunFragment.model");
        } catch (IOException e) {
            e.printStackTrace();
        }

        short[][] testMap = new short[0][];
        try {
            testMap = p.read("./data/raw/N32/N52E007.hgt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        testMap = p.cut(testMap, 50, 50, 50, 50);
        vectors = p.parse(testMap, 3, 3);
        double[] predictions = t.predict(t.toSvmNodeMatrix(vectors));

        svm_problem problem = t.getProblem();
        int totalCorrect = 0;
        for(int i = 0; i< predictions.length; i++)
            if(predictions[i] == problem.y[i])
                totalCorrect++;

        System.out.print("Prediction Accuracy = " + 100.0 * totalCorrect / predictions.length + "%\n");

        //t.crossValidate();
    }

    @Test
    @Ignore
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

        SupportVector[] vectors = p.parse(matrix, 3, 3);
        KernelTrainer t = new KernelTrainer();
        try {
            t.run(vectors, "testKernelRunSmall.model");
        } catch (IOException e) {
            e.printStackTrace();
        }

        short[][] testSet = new short[][]{
                { 1,  2,  3,  4,  5,  6,  7,  8,  9, 10},
                { 4,  5,  6,  7,  8,  9, 10, 11, 12, 13},
                { 7,  8,  9, 10, 11, 12, 13, 14, 15, 16},
                { 8,  9, 10, 11, 12, 13, 14, 15, 16, 17},
                { 9, 10, 11, 12, 13, 14, 15, 16, 17, 18},
                {10, 11, 12, 13, 14, 115, 16, 17, 18, 19},
                {11, 12, 13, 14, 15, 116, 17, 18, 19, 20},
                {12, 13, 14, 15, 16, 117, 18, 19, 20, 21},
                {13, 14, 15, 16, 17, 18, 19, 20, 21, 22},
                {14, 15, 16, 17, 18, 19, 20, 21, 22, 23},
                {15, 16, 17, 18, 19, 20, 21, 22, 23, 24}
        };

        SupportVector[] predictVectors = p.parse(testSet, 3, 3);

        double[] predictions = t.predict(t.toSvmNodeMatrix(predictVectors));
        int correct = 0;
        for(double d : predictions) {
            if(d == 1) {
                correct++;
            }
        }

        System.out.println("Predictions: " + correct + " out of " + predictions.length);
    }

    @Test
    @Ignore
    public void testRunBig() {
        Parser p = new Parser();
        short[][] altitudeMap = new short[0][];
        try {
            altitudeMap = p.read("./data/raw/N32/N52E007.hgt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        SupportVector[] vectors = p.parse(altitudeMap, 10, 10);
        KernelTrainer t = new KernelTrainer();
        try {
            t.run(vectors, "testKernelRunBig.model");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Ignore
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

        SupportVector[] vectors = p.parse(matrix, 3, 3);
        KernelTrainer t = new KernelTrainer();
        t.run(vectors);

        t.crossValidate();
    }

    @Test
    @Ignore
    public void testQuadraticPrediction() {
        Parser p = new Parser();
        // 1 = quadratic, -1 = non-quadratic
        double[] classification = {1, 1, 1, 1, -1, -1, -1, -1};

        // quadratic
        short[][] matrix = new short[][] {
                {2, 4},
                {4, 8},
                {9, 81},
                {10, 100},
                {5, 6},
                {3, 4},
                {6, 9},
                {4, 2},
        };

        svm_node up11 = new svm_node();
        up11.index = 1;
        up11.value = 32;
        svm_node up12 = new svm_node();
        up11.index = 2;
        up11.value = 32;
        svm_node up13 = new svm_node();
        up11.index = -1;
        svm_node[] up1 = new svm_node[] {
                up11, up12, up13
        };

        svm_node up21 = new svm_node();
        up11.index = 1;
        up11.value = 5;
        svm_node up22 = new svm_node();
        up11.index = 2;
        up11.value = 25;
        svm_node up23 = new svm_node();
        up11.index = -1;
        svm_node[] up2 = new svm_node[] {
                up21, up22, up23
        };

        KernelTrainer t = new KernelTrainer();
        SupportVector[] vectors = p.parse(matrix, 2, 1);
        SvmNodeMatrix nodeMatrix = t.toSvmNodeMatrix(vectors);
        nodeMatrix.setClassification(classification);
        t.run(nodeMatrix);

        double p1 = t.predict(up1);
        double p2 = t.predict(up2);

        Assert.assertEquals(-1, p1, 0.001);
        Assert.assertEquals(1, p2, 0.001);

    }
}
