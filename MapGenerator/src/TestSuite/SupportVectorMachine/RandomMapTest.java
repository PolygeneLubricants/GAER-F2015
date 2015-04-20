package TestSuite.SupportVectorMachine;

import Preprocessor.Parser;
import RandomMapGenerator.RandomMap;
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
        short[][] matrix = new short[][]{
                {40, 20, 40, 20, 10},
                {10, 20, 50, 10, 10},
                {40, 10, 400, 10, 40},
                {10, 20, 10, 10, 10},
                {10, 60, 40, 60, 10}
        };

        /*short[][] matrix = new short[][]{
                {-10, 0, 0, 0, -10},
                {0, 0, 0, 0, 0},
                {0, 0, 100, 0, 0},
                {0, 0, 0, 0, 0},
                {-10, 0, 0, 0, -10}
        };*/


        System.out.println("INITIAL RANDOM MAP: ");
        for(short[] index : matrix){
            for(short s : index){
                System.out.print(s + " ");
            }
            System.out.println();
        }

        int max = RandomMap.FindMaxHeight(matrix);



        //Blur the map
        for(int i = 0; i<5; i++) {
            matrix = RandomMap.blurMap(matrix, 1);
        }

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
        RandomMap.createMountainRange(matrix);

    }
}
