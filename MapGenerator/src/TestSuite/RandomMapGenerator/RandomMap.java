package TestSuite.RandomMapGenerator;

import Preprocessor.Parser;

/**
 * Created by patrikk on 30/03/2015.
 */
public class RandomMap {
    Parser p = new Parser();
    short[][] matrix;

    public static short[][] CreateRandomMap(){
        short[][] randomMap = new short[3000][3000];
        short initialMaxHeight = Short.MAX_VALUE;

        for(int i = 0; i<randomMap.length; i++){
            for(int j = 0; j< randomMap[i].length; j++){
                randomMap[i][j] = (short) ((Math.random()-0.5)*initialMaxHeight);
            }
        }


        return randomMap;
    }

    public static short FindMaxHeight(short[][] altitudeMap){
        short maxHeight = 0;

        for(int i = 0; i<altitudeMap.length; i++)
            for(int j = 0; j< altitudeMap[i].length; j++)
                if(altitudeMap[i][j] > maxHeight)
                    maxHeight = altitudeMap[i][j];

        return maxHeight;
    }

    public void setMatrix(short[][] matrix) {
        this.matrix = matrix;
    }

    public short[][] getMatrix() {
        return matrix;
    }


}
