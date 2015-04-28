package RandomMapGenerator;

import SupportVectorMachine.Model.AltitudeBoundPair;
import javafx.util.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by patrikk on 30/03/2015.
 */
public class RandomMap {

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

    /*
     * For generation random noise for the createDiamondSquareMap() function nodes
     */
    public static double randomNumber(double orgValue, int percent){

        if(percent > 0) {
            double noisedValue = 0;

            double percentValue = (double) (orgValue * percent / 100);

            double boundaries = percentValue * 2;
            double randomNoise = Math.random() * boundaries - percentValue;

            noisedValue = Math.round(orgValue + randomNoise);

            return noisedValue;
        }else{
            return orgValue;
        }
    }

    public static short checkBounds(short height, AltitudeBoundPair bounds){

        //Check if bounds are exceeded
        if(height > bounds.getMax())
            height = bounds.getMax();
        else if(height < bounds.getMin())
            height = bounds.getMin();

        return height;
    }


    public static short[][] createDiamondSquareMap(short[][] randomMap, int noisePercent, AltitudeBoundPair bounds){

        //INITIALISATION
        int n = 1;

        //GO INTO THE LOOP!
        for(int interval = randomMap.length-1; interval >= 2; interval /= 2) {
            //System.out.println("interval: " + interval);
            int halfInterval = interval / 2;

            //DIAMOND STEP
            //Find centerpoints
            for (int i = 0; i < n; i++) {
                for (int g = 0; g < n; g++) {

                    int x = interval * i + halfInterval;
                    int y = interval * g + halfInterval;

                    double cornerAvg = 0;
                    cornerAvg += randomMap[x - halfInterval][y - halfInterval];
                    cornerAvg += randomMap[x + halfInterval][y - halfInterval];
                    cornerAvg += randomMap[x - halfInterval][y + halfInterval];
                    cornerAvg += randomMap[x + halfInterval][y + halfInterval];

                    double nodeValue = cornerAvg / 4;
                    nodeValue = randomNumber(nodeValue, noisePercent);

                    //CENTER CONSTANT: Make a positive or negative constant
                    if(n == 1)
                        if(Math.random() >0.5)
                            nodeValue += Math.sqrt(nodeValue*5);
                        else
                            nodeValue -= Math.sqrt(nodeValue*5);

                    randomMap[x][y] = checkBounds((short) Math.round(nodeValue), bounds);

                }
            }

            //SQUARE STEP
            for (int i = 0; i < n; i++) {
                for (int g = 0; g < n; g++) {

                    //NORTH
                    //Get average from neighbors
                    double neighborAvg = 0;
                    neighborAvg += randomMap[interval * i ][interval * g];
                    neighborAvg += randomMap[interval * i ][interval * g + interval];
                    //Create noise
                    double nodeValue = neighborAvg/2;
                    nodeValue = randomNumber(nodeValue, noisePercent);
                    //Update with noised height
                    randomMap[interval * i ][interval * g + halfInterval] = checkBounds((short) Math.round(nodeValue), bounds);

                    //EAST
                    //Get average from neighbors
                    neighborAvg = 0;
                    neighborAvg += randomMap[interval * i][interval * (g+1)];
                    neighborAvg += randomMap[interval * i + interval][interval * (g+1)];
                    //Create noise
                    nodeValue = 0;
                    nodeValue = neighborAvg/2;
                    nodeValue = randomNumber(nodeValue, noisePercent);
                    //Update with noised height
                    randomMap[interval * i + halfInterval][interval * (g+1)] = checkBounds((short) Math.round(nodeValue), bounds);

                    //SOUTH
                    // Get average from neighbors
                    neighborAvg = 0;
                    neighborAvg += randomMap[interval * (i+1)][interval * g];
                    neighborAvg += randomMap[interval * (i+1)][interval * g + interval];
                    //Create noise
                    nodeValue = 0;
                    nodeValue = neighborAvg/2;
                    nodeValue = randomNumber(nodeValue, noisePercent);
                    //Update with noised height
                    randomMap[interval * (i+1) ][interval * g + halfInterval] = checkBounds((short) Math.round(nodeValue), bounds);

                    //WEST
                    //Get average from neighbors
                    neighborAvg = 0;
                    neighborAvg += randomMap[interval * i][interval * g];
                    neighborAvg += randomMap[interval * i + interval][interval * g];
                    //Create noise
                    nodeValue = 0;
                    nodeValue = neighborAvg/2;
                    nodeValue = randomNumber(nodeValue, noisePercent);
                    //Update with noised height
                    randomMap[interval * i + halfInterval][interval * g] = checkBounds((short) Math.round(nodeValue), bounds);
                }
            }

            //Double n-value
            n *= 2;
        }

        //Return the generated map
        return randomMap;
    }



    public static short[][] CreateRandomMap(int width, int height, short min, short max){
        Random random = new Random();
        short[][] randomMap = new short[height][width];

        for(int i = 0; i < randomMap.length; i++){
            for(int j = 0; j< randomMap[i].length; j++){
                randomMap[i][j] = (short)(random.nextInt(max - min) + min);
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
                    //////System.out.println("New maxHeight: " + maxHeight);
                }

        return maxHeight;
    }

    public static short FindMaxLow(short[][] altitudeMap){
        short maxLow = 1;

        for(int i = 0; i < altitudeMap.length; i++)
            for(int j = 0; j < altitudeMap[i].length; j++)
                if(altitudeMap[i][j] < maxLow) {
                    maxLow = altitudeMap[i][j];
                    //////System.out.println("New maxLow: " + maxLow);
                }
        maxLow = (short) Math.abs(maxLow);

        return maxLow;
    }

    public static short[][] CreateNewRandomVector(short[][] map, int fromRow, int fromCol, int height, int width, AltitudeBoundPair bounds) {
        // If the new vector is on the edge, we ignore it, as it has been modified previously.
        if(fromRow + height >= map.length || fromCol + width >= map[0].length)
            return map;

        short[][] randomVector = RandomMap.CreateRandomMap(width, height, bounds.getMin(), bounds.getMax());
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                map[i + fromRow][j + fromCol] = randomVector[i][j];
            }
        }

        return map;
    }

    public static short[][] evolveMap(short[][] altitudeMap, AltitudeBoundPair bounds) {
        short[][] evolvedMap = new short[altitudeMap.length][altitudeMap[0].length];

        Random random = new Random();

        //For each node in array
        for(int row = 0; row < altitudeMap.length; row++) {
            for (int column = 0; column < altitudeMap[row].length; column++) {

                //If height not approved
                if (random.nextFloat() > 0.98){
                    evolvedMap[row][column] = (short)(random.nextFloat() * 100);

                } else { //If height approved
                    short orgHeight = altitudeMap[row][column];
                    double change = Math.sqrt(Math.abs(orgHeight));

                    //Randomize negative or positive
                    if (random.nextFloat() > 0.5)
                        change = 0 - change;

                    evolvedMap[row][column] = (short) Math.round(orgHeight + change);
                }

                //Check if bounds are exceeded
                if(evolvedMap[row][column] > bounds.getMax())
                    evolvedMap[row][column] = bounds.getMax();
                else if(evolvedMap[row][column] < bounds.getMin())
                    evolvedMap[row][column] = bounds.getMin();
            }
        }

        return evolvedMap;
    }


    public static short[][] blurMap(short[][] altitudeMap, int maxLayer){
        short[][] blurredMap = new short[altitudeMap.length][altitudeMap[0].length];

        //For each node in array
        for(int row = 0; row < altitudeMap.length; row++){
            for(int column = 0; column < altitudeMap[row].length; column++) {

                double newHeight = altitudeMap[row][column]; //Updated height for node

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
                        newHeight += layerValue / (double)(nodeCounter * (currentLayer + 1));
                    }
                }

                //Division for dividing with newHeight
                double division = 1.0f;
                for(int c = 2; c <= (maxLayer + 1); c++) division += 1.0/c;

                //Divide newHeight, based on layer count
                newHeight /= division;

                //Assign new height to blurred map
                long rounded = Math.round(newHeight);
                blurredMap[row][column] = (short)rounded;
            }
        }

        //Return map with blurred values
        return blurredMap;
    }


    public static short[][] createMountainRange(short[][] altitudeMap){
        short[][] alteredMap = new short[altitudeMap.length][altitudeMap[0].length];


        //FIND MAX HEIGHT
        short maxHeight = FindMaxHeight(altitudeMap);
        ////System.out.println("maxH: " + maxHeight);

        int randomRow = 0;
        int randomCol = 0;

        //FIND RANDOM NODE WITH AT LEAST X% of MaxHeight
        for(int i = 0; i <= 10; i++){

            randomRow = (int) (Math.random() * altitudeMap.length);
            randomCol = (int) (Math.random() * altitudeMap[0].length);

            //If node is more than 80% of maxHeight, then break
            if(altitudeMap[randomRow][randomCol] >= maxHeight * 0.8){
                ////System.out.println("i er " + i);
                break;
            }else
                if(i == 10){
                    randomRow = (int) (Math.random() * altitudeMap.length);
                    randomCol = (int) (Math.random() * altitudeMap[0].length);
                    break;
                }
        }


        ////System.out.print("[" + randomRow + "][" + randomCol + "] = " + altitudeMap[randomRow][randomCol] + " --> ");

        //Grow the node (and neighbors?)
        altitudeMap[randomRow][randomCol] += Math.sqrt(altitudeMap[randomRow][randomCol]);

        ////System.out.println(altitudeMap[randomRow][randomCol]);


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
        ////System.out.println();
        ////System.out.println("HEIGHT ALTERED MAP: ");
        for(short[] index : altitudeMap){
            for(short s : index){
                //System.out.print(s + " ");
            }
            //System.out.println();
        }
        //return alteredMap;
        return altitudeMap;
    }

}
