package SupportVectorMachine.Trainers;

import SupportVectorMachine.Model.LinearNodeMatrix;
import SupportVectorMachine.Model.SupportVector;
import de.bwaldvogel.liblinear.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.IOException;

/**
 * Created by Andreas on 29/3/2015.
 */
public class LinearTrainer extends BaseTrainer {
    private Parameter _param;		// set by setParameters
    private Problem _prob;		// set by read
    private Model _model;
    private int _nrFold; // Fold number for cross validation

    public Problem getProblem() {
        return _prob;
    }

    public LinearTrainer() {
        setParameters();
    }

    public LinearTrainer(String modelFilePath) {
        setParameters();
        loadModel(modelFilePath);
    }

    public void loadModel(String modelFilePath) {
        throw new NotImplementedException();
    }

    public double[] predict(LinearNodeMatrix matrix) {
        double[] predictions = new double[matrix.get_length()];
        int index = 0;
        for(Feature[] feature : matrix.get_matrix()) {
            predictions[index] = predict(feature);
            index++;
        }

        return predictions;
    }

    public double predict(Feature[] vector) {
        return Linear.predict(_model, vector);
    }

    public void crossValidate()
    {
        int i;
        int total_correct = 0;

        double[] target = new double[_prob.l];
        Linear.crossValidation(_prob, _param, _nrFold, target);
        for(i=0;i< _prob.l;i++)
            if(target[i] == _prob.y[i])
                ++total_correct;
            System.out.print("Cross Validation Accuracy = " + 100.0 * total_correct / _prob.l + "%\n");
    }

    public void run(SupportVector[] vectors, String fileName) throws IOException
    {
        read(toSvmNodeMatrix(vectors));
        _model = Linear.train(_prob, _param);
        Linear.saveModel(new File(fileName), _model);
    }

    public void run(SupportVector[] vectors) {
        read(toSvmNodeMatrix(vectors));
        _model = Linear.train(_prob, _param);
    }

    public void run(LinearNodeMatrix matrix) {
        read(matrix);
        _model = Linear.train(_prob, _param);
    }

    private void setParameters()
    {
        _param = new Parameter(SolverType.L2R_LR, 1, 0.01);
        _nrFold = 10;
    }

    public LinearNodeMatrix toSvmNodeMatrix(SupportVector[] vectors) {
        int length = vectors.length; // Length of training data
        double[] classification = new double[length]; // This is redundant for our one-class SVM.
        Feature[][] trainingSet = new Feature[length][]; // The training set.
        for(int i = 0; i < length; i++)
        {
            classification[i] = 1; // Since classifications are redundant in our setup, they all belong to the same class, 1.

            Feature[] vector = new Feature[vectors[i].getLength()];

            double[] doubles = vectors[i].toDouble(); // The SVM runs on doubles.
            for(int j = 1; j <= doubles.length; j++) {
                Feature node = new FeatureNode(j, doubles[j - 1]);
                vector[j - 1] = node;
            }

            trainingSet[i] = vector;
        }

        return new LinearNodeMatrix(trainingSet, classification, length);
    }

    // read in a problem (in svmlight format)
    private void read(LinearNodeMatrix nodeMatrix)
    {
        _prob = new Problem();
        _prob.l = nodeMatrix.get_length();
        _prob.y = nodeMatrix.get_classification();
        _prob.x = nodeMatrix.get_matrix();
        _prob.n = nodeMatrix.get_matrix()[0].length;
    }
}
