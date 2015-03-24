package SupportVectorMachine;

import SupportVectorMachine.Model.SupportVector;
import libsvm.*;

import java.io.IOException;

/**
 * Created by Andreas on 24/3/2015.
 */
public class Trainer {
    private svm_parameter param;		// set by setParameters
    private svm_problem prob;		// set by read
    private svm_model model;
    private int nrFold; // Fold number for cross validation

    public Trainer() {
        setParameters();
    }

    public void crossValidate()
    {
        int i;
        int total_correct = 0;
        double total_error = 0;
        double sumV = 0, sumY = 0, sumVV = 0, sumYY = 0, sumVY = 0;
        double[] target = new double[prob.l];

        svm.svm_cross_validation(prob, param, nrFold, target);
        if(param.svm_type == svm_parameter.EPSILON_SVR ||
                param.svm_type == svm_parameter.NU_SVR)
        {
            for(i=0;i<prob.l;i++)
            {
                double y = prob.y[i];
                double v = target[i];
                total_error += (v-y)*(v-y);
                sumV += v;
                sumY += y;
                sumVV += v*v;
                sumYY += y*y;
                sumVY += v*y;
            }
            System.out.print("Cross Validation Mean squared error = "+total_error/prob.l+"\n");
            System.out.print("Cross Validation Squared correlation coefficient = "+
                            ((prob.l*sumVY-sumV*sumY)*(prob.l*sumVY-sumV*sumY))/
                                    ((prob.l*sumVV-sumV*sumV)*(prob.l*sumYY-sumY*sumY))+"\n"
            );
        }
        else
        {
            for(i=0;i<prob.l;i++)
                if(target[i] == prob.y[i])
                    ++total_correct;
            System.out.print("Cross Validation Accuracy = "+100.0*total_correct/prob.l+"%\n");
        }
    }

    public void run(SupportVector[] vectors, String fileName) throws IOException
    {
        read(vectors);
        model = svm.svm_train(prob, param);
        svm.svm_save_model(fileName, model);
    }

    private void setParameters()
    {
        param = new svm_parameter();
        // default values
        param.svm_type = svm_parameter.ONE_CLASS;
        param.kernel_type = svm_parameter.RBF;
        param.degree = 3;
        param.gamma = 0;	// 1/num_features
        param.coef0 = 0;
        param.nu = 0.5;
        param.cache_size = 100;
        param.C = 1;
        param.eps = 1e-3;
        param.p = 0.1;
        param.shrinking = 1;
        param.probability = 0;
        param.nr_weight = 0;
        param.weight_label = new int[0];
        param.weight = new double[0];
        nrFold = 10;
    }

    // read in a problem (in svmlight format)
    private void read(SupportVector[] vectors) throws IOException
    {
        int length = vectors.length; // Length of training data
        double[] classification = new double[length]; // This is redundant for our one-class SVM.
        svm_node[][] trainingSet = new svm_node[length][]; // The training set.
        for(int i = 0; i < length; i++)
        {
            classification[i] = 1; // Since classifications are redundant in our setup, they all belong to the same class, 1.

            // each vector. The vector has to be one index longer than the actual vector,
            // because the implementation needs an empty node in the end with index -1.
            svm_node[] vector = new svm_node[vectors[i].getLength() + 1];

            double[] doubles = vectors[i].toDouble(); // The SVM runs on doubles.
            for(int j = 0; j < vector.length; j++) {
                svm_node node = new svm_node();
                node.index = j;
                node.value = doubles[j];
                vector[j] = node;
            }
            svm_node last = new svm_node();
            last.index = -1;
            vector[vector.length - 1] = last;

            trainingSet[i] = vector;
        }

        svm_problem problem = new svm_problem();
        problem.l = length;
        problem.y = classification;
        problem.x = trainingSet;
    }
}
