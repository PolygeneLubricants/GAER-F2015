package SupportVectorMachine.Model;

import libsvm.svm_node;

/**
 * Created by Andreas on 26/3/2015.
 */
public class SvmNodeMatrix {
    private svm_node[][] _matrix;
    private double[] _classification;
    private int _length;

    public SvmNodeMatrix(svm_node[][] matrix, double[] classification, int length) {
        _matrix = matrix;
        _classification = classification;
        _length = length;
    }

    public svm_node[][] get_matrix() {
        return _matrix;
    }

    public double[] get_classification() {
        return _classification;
    }

    public int get_length() {
        return _length;
    }
}
