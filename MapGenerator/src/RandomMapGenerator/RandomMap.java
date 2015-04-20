package RandomMapGenerator;

import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by patrikk on 30/03/2015.
 */
public class RandomMap {
    static float landToWaterRatio = 0.5f;

    public static Pair<Integer, Integer>[] toIndexPairs(short[][] matrix) {
        ArrayList<Pair<Integer, Integer>> pairs = new ArrayList<>();
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[0].length; j++) {
                pairs.add(new Pair<>(i, j));
            }
        }

        Pair[] arr = pairs.toArray(new Pair[pairs.size()]);
        return arr;
    }

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

    public static short[][] blurMap(short[][] altitudeMap, int maxLayer){
        short[][] blurredMap = new short[altitudeMap.length][altitudeMap[0].length];

        //For each node in array
        for(int row = 0; row < altitudeMap.length; row++){
            for(int column = 0; column < altitudeMap[row].length; column++) {

                //Print position and height
                System.out.println("[" + row + "][" + column + "]: " + altitudeMap[row][column] );

                float newHeight = altitudeMap[row][column]; //Updated height for node

                //For each layer
                for(int currentLayer = 1; currentLayer <= maxLayer; currentLayer++) {

                    short layerValue = 0; //Total height of layer nodes
                    int nodeCounter = 0; //Count of relevant nodes in layer

                    //NORTH- AND SOUTHSIDE
                    for (int k = column - currentLayer; k <= column + currentLayer; k++) {

                        //Max value on j dimension (aka y)
                        int kMax = altitudeMap[row].length;

                        //NORTH
                        //Check if within array boundaries
                        if (k >= 0 && k < kMax && row-currentLayer >= 0) {

                            //Add value
                            layerValue += altitudeMap[row - currentLayer][k];
                            nodeCounter++;
                            System.out.println("NORTH[" + (row - currentLayer) + "][" + k + "]: " + altitudeMap[row - currentLayer][k]);
                        }

                        //SOUTH
                        //Check if within array boundaries
                        if (k >= 0 && k < kMax && row+currentLayer < altitudeMap.length) {

                            //Add value
                            layerValue += altitudeMap[row + currentLayer][k];
                            nodeCounter++;
                            System.out.println("SOUTH[" + (row + currentLayer) + "][" + k + "]: " + altitudeMap[row + currentLayer][k]);
                        }
                    }


                    //EAST- AND WESTSIDE
                    for (int k = row - (currentLayer-1); k <= row + (currentLayer-1); k++) {

                        //Max value on i dimension (aka y)
                        int kMax = altitudeMap.length;

                        //EAST
                        //Check if within array boundaries
                        if (k >= 0 && k < kMax && column-currentLayer >= 0) {

                            //Add value
                            layerValue += altitudeMap[k][column-currentLayer];
                            nodeCounter++;
                            System.out.println("EAST[" + k + "][" + (column - currentLayer) + "]: " + altitudeMap[k][column - currentLayer]);
                        }

                        //WEST
                        //Check if within array boundaries
                        if (k >= 0 && k < kMax && column+currentLayer < altitudeMap[row].length) {

                            //Add value
                            layerValue += altitudeMap[k][column+currentLayer];
                            nodeCounter++;
                            System.out.println("WEST[" + k + "][" + (column + currentLayer) + "]: " + altitudeMap[k][column + currentLayer]);
                        }

                    }


                    //If more than zero nodes in this layer
                    if (nodeCounter != 0) {
                        //Add the average for this layer divided by the layer count+1
                        newHeight += (short) (layerValue / nodeCounter / (currentLayer + 1));

                        System.out.println("Avg = " + " = " + layerValue + " / " + nodeCounter + " / " + (currentLayer + 1) );
                    }
                }

                //Division for dividing with newHeight
                float division = 1.0f;
                for(int c = 2; c <= (maxLayer + 1); c++) division += 1.0/c;

                //Print out the divider and temporary new height
                System.out.println("division: " + division);
                System.out.println("the new height:" + newHeight);

                //Divide newHeight, based on layer count
                newHeight /= division;

                //Assign new height to blurred map
                blurredMap[row][column] = (short)newHeight;

                //Print position and final updated height
                System.out.println("New height[" + row + "][" + column + "]: " + newHeight);
                System.out.println("------");
            }
        }

        //Return map with blurred values
        return blurredMap;
    }

}
