package RandomMapGenerator;

/**
 * Created by patrikk on 30/03/2015.
 */
public class RandomMap {
    short[][] matrix;
    static float landToWaterRatio = 0.5f;

    public static short[][] CreateRandomMap(int width, int height){
        short[][] randomMap = new short[height][width];
        short initialMaxHeight = 100;

        for(int i = 0; i<randomMap.length; i++){
            for(int j = 0; j< randomMap[i].length; j++){
                randomMap[i][j] = (short) ( (Math.random() - landToWaterRatio ) * initialMaxHeight );
            }
        }


        return randomMap;
    }

    public static short FindMaxHeight(short[][] altitudeMap){
        short maxHeight = 1;

        for(int i = 0; i < altitudeMap.length; i++)
            for(int j = 0; j < altitudeMap[i].length; j++)
                if(altitudeMap[i][j] > maxHeight) {
                    maxHeight = altitudeMap[i][j];
                    System.out.println("New maxHeight: " + maxHeight);
                }

        return maxHeight;
    }

    public static short FindMaxLow(short[][] altitudeMap){
        short maxLow = 1;

        for(int i = 0; i < altitudeMap.length; i++)
            for(int j = 0; j < altitudeMap[i].length; j++)
                if(altitudeMap[i][j] < maxLow) {
                    maxLow = altitudeMap[i][j];
                    System.out.println("New maxLow: " + maxLow);
                }
        maxLow = (short) Math.abs(maxLow);

        return maxLow;
    }

    public void setMatrix(short[][] matrix) {
        this.matrix = matrix;
    }

    public short[][] getMatrix() {
        return matrix;
    }


    public static short[][] blurMap(short[][] altitudeMap){
        short[][] blurredMap = new short[altitudeMap.length][altitudeMap[0].length];

        int counter = 0;
        short neighhborValues = 0;

        for(int i = 0; i<altitudeMap.length; i++){
            for(int j = 0; j< altitudeMap[i].length; j++) {
                counter = 0;
                neighhborValues = 0;

                //CHECK IF NEIGHBORS EXIST
                if (i-1 > 0) {
                    neighhborValues += altitudeMap[i - 1][j];
                    counter++;
                }
                if (i+1 < altitudeMap.length) {
                    neighhborValues += altitudeMap[i + 1][j];
                    counter++;
                }

                if (j-1 > 0) {
                    neighhborValues += altitudeMap[i][j - 1];
                    counter++;
                }
                if (j+1 < altitudeMap[i].length) {
                    neighhborValues += altitudeMap[i][j + 1];
                    counter++;
                }

                if(counter == 0){
                    blurredMap[i][j] = altitudeMap[i][j];
                }else{
                    float neighborAvg = neighhborValues / counter;

                    //short newHeight = (short) ( ( neighborAvg + altitudeMap[i][j] ) / 2 );
                    short newHeight = (short)neighborAvg;

                    blurredMap[i][j] = newHeight;
                }


            }
        }

        return blurredMap;

    }


}
