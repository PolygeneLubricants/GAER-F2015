package TestSuite.SupportVectorMachine;

import Preprocessor.Parser;
import RandomMapGenerator.RandomMap;
import SupportVectorMachine.Model.AltitudeBoundPair;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by patrikk on 30/03/2015.
 */
public class RandomMapTest {

    @Test
    public void testReading() {
        Parser p = new Parser();
        try {
            p.read("./data/raw/N32/N52E006.hgt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMap(){
        /*short[][] matrix = new short[][]{
                {0, 0, 0},
                {0, 10, 0},
                {0, 0, 0}
        };*/


        /*short[][] matrix = new short[][]{
                {40, 20, 40, 20, 10},
                {10, 20, 50, 10, 10},
                {40, 10, 400, 10, 40},
                {10, 20, 10, 10, 10},
                {10, 60, 40, 60, 10}
        };*/

        int iterations = 2;

        short[][] matrix = new short[][]{
                {100, 0, 0, 0, 100},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {100, 0, 0, 0, 100}
        };


        System.out.println("INITIAL RANDOM MAP: ");
        for(short[] index : matrix){
            for(short s : index){
                System.out.print(s + " ");
            }
            System.out.println();
        }

        //int max = RandomMap.FindMaxHeight(matrix);

        //Blur the map
        //for(int i = 0; i < iterations; i++) matrix = RandomMap.blurMap(matrix, 1);



        AltitudeBoundPair bounds = new AltitudeBoundPair((short)0, (short)30);
        //short[][] randMap = RandomMap.CreateRandomMap(257, 257, bounds.getMin(), bounds.getMax());

        /* Create noise percent and subtract them from the max value
            so maxxed out values won't exceed the max value
         */
        int noisePercent = 10;
        int min = bounds.getMin();
        int max = bounds.getMax() - bounds.getMax()*noisePercent/100;

        short[][] altitudeMap = RandomMap.createDiamondSquareMap(matrix, 10, bounds);


        //PRINT OUT THE MAP
        System.out.println();
        System.out.println("BLURRED MAP: ");
        for(short[] index : matrix){
            for(short s : index){
                System.out.print(s + " ");
            }
            System.out.println();
        }

        //Create mountain range
        //RandomMap.createMountainRange(matrix);

    }
}
