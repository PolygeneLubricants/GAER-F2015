package TestSuite.RandomMapGenerator;

import Preprocessor.Parser;

/**
 * Created by patrikk on 30/03/2015.
 */
public class RandomMap {
    Parser p = new Parser();
    short[][] matrix = new short[][]{
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
    };

    public void setMatrix(short[][] matrix) {
        this.matrix = matrix;
    }



    public short[][] getMatrix() {
        return matrix;
    }


}
