package TestSuite.Preprocessor;

import GUI.PixelMap;
import Preprocessor.Parser;
import SupportVectorMachine.Model.SupportVector;
import RandomMapGenerator.RandomMap;
import org.junit.Assert;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * User: AndreasRydingLund
 * Date: 23-03-2015
 */
public class ParserTest {
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
    public void testParsing() {
        Parser p = new Parser();
        short[][] matrix = new short[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        SupportVector[] result = p.parse(matrix, 1, 1);
        Assert.assertEquals(9, result.length);
        Assert.assertEquals(1, result[0].getVector(0,0));
        Assert.assertEquals(5, result[4].getVector(0,0));
        Assert.assertEquals(9, result[8].getVector(0,0));

        result = p.parse(matrix, 2, 2);
        Assert.assertEquals(4, result.length);
        Assert.assertEquals(1, result[0].getVector(0,0));
        Assert.assertEquals(8, result[3].getVector(1,0));

        result = p.parse(matrix, 2, 3);
        Assert.assertEquals(2, result.length);

        result = p.parse(matrix, 3, 3);
        Assert.assertEquals(1, result.length);
        Assert.assertEquals(1, result[0].getVector(0,0));
        Assert.assertEquals(5, result[0].getVector(1,1));
        Assert.assertEquals(9, result[0].getVector(2,2));
    }

    @org.junit.Ignore
    @Test
    public void testVisualIdentification() {
        Parser p = new Parser();

        short[][] altitudeMap = RandomMap.CreateRandomMap(100, 100);

        //short[][] altitudeMap = null;
        //for(int i = 0; i < 2; i++)
        //    altitudeMap = RandomMap.blurMap(altitudeMap, 2);


        /*try {
            altitudeMap = p.read("./data/raw/N32/N52E007.hgt");
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        JFrame frame = new JFrame("Visual verification of SRTM maps");

        PixelMap panel = new PixelMap(altitudeMap);

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        while(true);
    }
}
