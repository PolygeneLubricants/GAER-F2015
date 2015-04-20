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
                    //System.out.println("New maxHeight: " + maxHeight);
                }

        return maxHeight;
    }

    public static short FindMaxLow(short[][] altitudeMap){
        short maxLow = 1;

        for(int i = 0; i < altitudeMap.length; i++)
            for(int j = 0; j < altitudeMap[i].length; j++)
                if(altitudeMap[i][j] < maxLow) {
                    maxLow = altitudeMap[i][j];
                    //System.out.println("New maxLow: " + maxLow);
                }
        maxLow = (short) Math.abs(maxLow);

        return maxLow;
    }

    public static short[][] blurMap(short[][] altitudeMap, int maxLayer){
        short[][] blurredMap = new short[altitudeMap.length][altitudeMap[0].length];

        //For each node in array
        for(int row = 0; row < altitudeMap.length; row++){
            for(int column = 0; column < altitudeMap[row].length; column++) {

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
                        }

                        //SOUTH
                        //Check if within array boundaries
                        if (k >= 0 && k < kMax && row+currentLayer < altitudeMap.length) {

                            //Add value
                            layerValue += altitudeMap[row + currentLayer][k];
                            nodeCounter++;
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
                        }

                        //WEST
                        //Check if within array boundaries
                        if (k >= 0 && k < kMax && column+currentLayer < altitudeMap[row].length) {

                            //Add value
                            layerValue += altitudeMap[k][column+currentLayer];
                            nodeCounter++;
                        }

                    }


                    //If more than zero nodes in this layer
                    if (nodeCounter != 0) {
                        //Add the average for this layer divided by the layer count+1
                        newHeight += layerValue / nodeCounter / (currentLayer + 1);
                    }
                }

                //Division for dividing with newHeight
                float division = 1.0f;
                for(int c = 2; c <= (maxLayer + 1); c++) division += 1.0/c;

                //Divide newHeight, based on layer count
                newHeight /= division;

                //Assign new height to blurred map
                blurredMap[row][column] = (short)Math.round(newHeight);
            }
        }

        //Return map with blurred values
        return blurredMap;
    }


    public static short[][] createMountainRange(short[][] altitudeMap){
        short[][] alteredMap = new short[altitudeMap.length][altitudeMap[0].length];


        //FIND MAX HEIGHT
        short maxHeight = FindMaxHeight(altitudeMap);
        System.out.println("maxH: " + maxHeight);

        int randomRow = 0;
        int randomCol = 0;

        //FIND RANDOM NODE WITH AT LEAST X% of MaxHeight
        for(int i = 0; i <= 10; i++){

            randomRow = (int) (Math.random() * altitudeMap.length);
            randomCol = (int) (Math.random() * altitudeMap[0].length);

            //If node is more than 80% of maxHeight, then break
            if(altitudeMap[randomRow][randomCol] >= maxHeight * 0.8){
                System.out.println("i er " + i);
                break;
            }else
                if(i == 10){
                    randomRow = (int) (Math.random() * altitudeMap.length);
                    randomCol = (int) (Math.random() * altitudeMap[0].length);
                    break;
                }
        }


        System.out.print("[" + randomRow + "][" + randomCol + "] = " + altitudeMap[randomRow][randomCol] + " --> ");

        //Grow the node (and neighbors?)
        altitudeMap[randomRow][randomCol] += Math.sqrt(altitudeMap[randomRow][randomCol]);

        System.out.println(altitudeMap[randomRow][randomCol]);


        //For each layer, the neighboring nodes should be lifted
        int maxLayer = 1;
        for(int currentLayer = 1; currentLayer <= maxLayer; currentLayer++) {

            for (int k = randomCol - currentLayer; k <= randomCol + currentLayer; k++) {

                //Max value on j dimension (aka y)
                int kMax = altitudeMap[randomRow].length;

                //NORTH
                //Check if within array boundaries
                if (k >= 0 && k < kMax && randomRow - currentLayer >= 0) {
                    altitudeMap[randomRow - currentLayer][k] += Math.sqrt(altitudeMap[randomRow - currentLayer][k]);
                }

                //SOUTH
                //Check if within array boundaries
                if (k >= 0 && k < kMax && randomRow+currentLayer < altitudeMap.length) {
                    altitudeMap[randomRow + currentLayer][k] += Math.sqrt(altitudeMap[randomRow + currentLayer][k]);
                }
            }

            //EAST- AND WESTSIDE
            for (int k = randomRow - (currentLayer-1); k <= randomRow + (currentLayer-1); k++) {

                //Max value on i dimension (aka y)
                int kMax = altitudeMap.length;

                //EAST
                //Check if within array boundaries
                if (k >= 0 && k < kMax && randomCol - currentLayer >= 0) {

                    altitudeMap[k][randomCol - currentLayer] += Math.sqrt(altitudeMap[k][randomCol - currentLayer]);
                }

                //WEST
                //Check if within array boundaries
                if (k >= 0 && k < kMax && randomCol + currentLayer < altitudeMap[randomRow].length) {

                    altitudeMap[k][randomCol + currentLayer] += Math.sqrt(altitudeMap[k][randomCol + currentLayer]);
                }
            }
        }

        //PRINT OUT ALTERED MAP
        System.out.println();
        System.out.println("HEIGHT ALTERED MAP: ");
        for(short[] index : altitudeMap){
            for(short s : index){
                System.out.print(s + " ");
            }
            System.out.println();
        }
        //return alteredMap;
        return altitudeMap;
    }

}
