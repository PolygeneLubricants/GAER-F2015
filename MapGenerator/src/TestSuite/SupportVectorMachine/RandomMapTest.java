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
        /*short[][] matrix = new short[][]{
                {40, 20, 40, 20, 10},
                {10, 20, 10, 10, 10},
                {40, 10,  0, 10, 40},
                {10, 20, 10, 10, 10},
                {10, 20, 40, 10, 10}
        };*/

        short[][] matrix = new short[][]{
                {10, -40, 0, 0, 0},
                {10, 10, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}
        };


        System.out.println("INITIAL RANDOM MAP: ");
        for(short[] index : matrix){
            for(short s : index){
                System.out.print(s + " ");
            }
            System.out.println();
        }

        int max = RandomMap.FindMaxHeight(matrix);

        System.out.println("BLURRED MAP: ");

        for(int i = 0; i<1; i++) {
            matrix = RandomMap.blurMap(matrix, 2);
        }

        for(short[] index : matrix){
            for(short s : index){
                System.out.print(s + " ");
            }
            System.out.println();
        }

    }
}
