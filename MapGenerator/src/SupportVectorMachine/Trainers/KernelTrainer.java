package SupportVectorMachine.Trainers;

import SupportVectorMachine.Model.SupportVector;
import SupportVectorMachine.Model.SvmNodeMatrix;
import libsvm.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;

/**
 * Created by Andreas on 29/3/2015.
 */
public class KernelTrainer extends BaseTrainer {
    private svm_parameter _param;		// set by setParameters
    private svm_problem _prob;		// set by read
    private svm_model _model;
    private int _nrFold; // Fold number for cross validation

    public KernelTrainer() {
        setParameters();
    }

    public KernelTrainer(String modelFilePath) {
        setParameters();
        loadModel(modelFilePath);
    }

    public void loadModel(String modelFilePath) {
        throw new NotImplementedException();
    }

    public double[] predict(SvmNodeMatrix matrix) {
        double[] predictions = new double[matrix.get_length()];
        int index = 0;
        for(svm_node[] feature : matrix.get_matrix()) {
            predictions[index] = predict(feature);
            index++;
        }

        return predictions;
    }

    public double predict(svm_node[] vector) {
        return svm.svm_predict(_model, vector);
    }

    public void crossValidate()
    {
        int i;
        int total_correct = 0;

        double[] target = new double[_prob.l];
        double total_error = 0;
        double sumV = 0, sumY = 0, sumVV = 0, sumYY = 0, sumVY = 0;

        svm.svm_cross_validation(_prob, _param, _nrFold, target);
        if(_param.svm_type == svm_parameter.EPSILON_SVR ||
                _param.svm_type == svm_parameter.NU_SVR)
        {
            for(i=0;i< _prob.l;i++)
            {
                double y = _prob.y[i];
                double v = target[i];
                total_error += (v-y)*(v-y);
                sumV += v;
                sumY += y;
                sumVV += v*v;
                sumYY += y*y;
                sumVY += v*y;
            }
            System.out.print("Cross Validation Mean squared error = "+total_error/ _prob.l+"\n");
            System.out.print("Cross Validation Squared correlation coefficient = "+
                            ((_prob.l*sumVY-sumV*sumY)*(_prob.l*sumVY-sumV*sumY))/
                                    ((_prob.l*sumVV-sumV*sumV)*(_prob.l*sumYY-sumY*sumY))+"\n"
            );
        }
        else
        {
            for(i=0;i< _prob.l;i++)
                if(target[i] == _prob.y[i])
                    ++total_correct;
            System.out.print("Cross Validation Accuracy = "+100.0*total_correct/ _prob.l+"%\n");
        }
    }

    public void run(SupportVector[] vectors, String fileName) throws IOException
    {
        read(toSvmNodeMatrix(vectors));
        _model = svm.svm_train(_prob, _param);
        svm.svm_save_model(fileName, _model);
    }

    public void run(SupportVector[] vectors)
    {
        read(toSvmNodeMatrix(vectors));
        _model = svm.svm_train(_prob, _param);
    }

    public void run(SvmNodeMatrix matrix) {
        read(matrix);
        _model = svm.svm_train(_prob, _param);
    }

    private void setParameters()
    {
        _param = new svm_parameter();
        // default values
        _param.svm_type = svm_parameter.ONE_CLASS;
        _param.kernel_type = svm_parameter.RBF;
        _param.degree = 3;
        _param.gamma = 10;	// 1/num_features
        _param.coef0 = 0;
        _param.nu = 0.1;
        _param.cache_size = 100;
        _param.C = 1;
        _param.eps = 0.01;
        _param.p = 0.1;
        _param.shrinking = 1;
        _param.probability = 0;
        _param.nr_weight = 0;
        _param.weight_label = new int[0];
        _param.weight = new double[0];
        _nrFold = 10;
    }

    public SvmNodeMatrix toSvmNodeMatrix(SupportVector[] vectors) {
        int length = vectors.length; // Length of training data
        double[] classification = new double[length]; // This is redundant for our one-class SVM.
        svm_node[][] trainingSet = new svm_node[length][]; // The training set.
        for(int i = 0; i < length; i++)
        {
            classification[i] = 1; // Since classifications are redundant in our setup, they all belong to the same class, 1.

            svm_node[] vector = new svm_node[vectors[i].getLength()];

            double[] doubles = vectors[i].toDouble(); // The SVM runs on doubles.
            for(int j = 0; j < doubles.length; j++) {
                svm_node node = new svm_node();
                node.index = j;
                node.value = doubles[j];
                vector[j] = node;
            }

            trainingSet[i] = vector;
        }

        return new SvmNodeMatrix(trainingSet, classification, length);
    }

    // read in a problem (in svmlight format)
    private void read(SvmNodeMatrix nodeMatrix)
    {
        _prob = new svm_problem();
        _prob.l = nodeMatrix.get_length();
        _prob.y = nodeMatrix.get_classification();
        _prob.x = nodeMatrix.get_matrix();
    }
}