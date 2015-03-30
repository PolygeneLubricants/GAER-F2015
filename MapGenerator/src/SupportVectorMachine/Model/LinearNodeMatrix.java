package SupportVectorMachine.Model;

import de.bwaldvogel.liblinear.Feature;

/**
 * Created by Andreas on 29/3/2015.
 */
public class LinearNodeMatrix {
    private Feature[][] _matrix;
    private double[] _classification;
    private int _length;

    public LinearNodeMatrix(Feature[][] matrix, double[] classification, int length) {
        _matrix = matrix;
        _classification = classification;
        _length = length;
    }

    public Feature[][] get_matrix() {
        return _matrix;
    }

    public double[] get_classification() {
        return _classification;
    }

    public int get_length() {
        return _length;
    }
}
