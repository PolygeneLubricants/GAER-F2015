package RandomMapGenerator;

/**
 * Created by patrikk on 30/03/2015.
 */
public class RandomMap {
    short[][] matrix;

    public static short[][] CreateRandomMap(int width, int height){
        short[][] randomMap = new short[height][width];
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
