package SupportVectorMachine.Model;

/**
 * Created by Andreas on 23/3/2015.
 */
public class SupportVector {
    private int _width;
    private int _height;
    private short[] _vector;

    public SupportVector(int width, int height) {
        _width = width;
        _height = height;
        _vector = new short[width * height];
    }

    public SupportVector(short[][] matrix) {
        _width = matrix.length;
        _height = matrix[0].length;
        setVector(matrix);
    }

    public SupportVector(int width, int height, short[] vector) {
        _width = width;
        _height = height;
        _vector = vector;
        assert vector.length == width * height;
    }

    public int getWidth() {
        return _width;
    }

    public int getHeight() {
        return _height;
    }

    public short getVector(int x, int y) {
        int row = x * _width;
        return _vector[row + y];
    }

    public void setVector(int x, int y, short value) {
        int row = x * _width;
        _vector[row + y] = value;
    }

    public void setVector(short[][] matrix) {
        _vector = new short[matrix.length * matrix[0].length];
        for(int x = 0; x < matrix.length; x++) {
            for(int y = 0; y < matrix[x].length; y++) {
                if(matrix[x].length != _width)
                    throw new IllegalArgumentException("Jagged arrays are not supported.");

                setVector(x, y, matrix[x][y]);
            }
        }
    }
}
