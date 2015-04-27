package SupportVectorMachine.Model;

import libsvm.svm_node;

/**
 * Created by Andreas on 26/3/2015.
 */
public class SvmNodeMatrix {
    private svm_node[][] _matrix;
    private double[] _classification;
    private int _length;
    private AltitudeBoundPair _altitudeBoundPair;

    public SvmNodeMatrix(svm_node[][] matrix, double[] classification, int length, AltitudeBoundPair altitudeBoundPair) {
        _matrix = matrix;
        _classification = classification;
        _length = length;
        _altitudeBoundPair = altitudeBoundPair;
    }

    public svm_node[][] get_matrix() {
        return _matrix;
    }

    public double[] get_classification() {
        return _classification;
    }

    public void setClassification(double[] classification) {
        _classification = classification;
    }

    public int get_length() {
        return _length;
    }

    public AltitudeBoundPair getAltitudeBoundPair() {
        return _altitudeBoundPair;
    }

    public void setAltitudeBoundPair(AltitudeBoundPair _altitudeBoundPair) {
        this._altitudeBoundPair = _altitudeBoundPair;
    }
}
