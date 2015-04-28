package TestSuite.Preprocessor;

import GUI.PixelMap;
import Preprocessor.Parser;
import SupportVectorMachine.Model.AltitudeBoundPair;
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
        SupportVector[] result = p.parse(matrix, 1, 1, 0);
        Assert.assertEquals(9, result.length);
        Assert.assertEquals(1, result[0].getVector(0,0));
        Assert.assertEquals(5, result[4].getVector(0,0));
        Assert.assertEquals(9, result[8].getVector(0,0));

        result = p.parse(matrix, 2, 2, 0);
        Assert.assertEquals(4, result.length);
        Assert.assertEquals(1, result[0].getVector(0,0));
        Assert.assertEquals(8, result[3].getVector(1,0));

        result = p.parse(matrix, 2, 3, 0);
        Assert.assertEquals(2, result.length);

        result = p.parse(matrix, 3, 3, 0);
        Assert.assertEquals(1, result.length);
        Assert.assertEquals(1, result[0].getVector(0,0));
        Assert.assertEquals(5, result[0].getVector(1,1));
        Assert.assertEquals(9, result[0].getVector(2,2));
    }

    @Test
    public void testSkip() {
        Parser p = new Parser();
        short[][] matrix = new short[][]{
                { 1,  2,  3,  4,  5},
                { 6,  7,  8,  9, 10},
                {11, 12, 13, 14, 15},
                {12, 13, 14, 15, 16},
                {17, 18, 19, 20, 21},

        };

        SupportVector[] result = p.parse(matrix, 3, 3, 1);
        Assert.assertEquals(4, result.length);
    }

    @org.junit.Ignore
    @Test
    public void testVisualIdentification() {
        Parser p = new Parser();

        //short[][] altitudeMap = RandomMap.CreateRandomMap(200, 200, Short.MIN_VALUE, Short.MAX_VALUE);
        AltitudeBoundPair bounds = new AltitudeBoundPair((short)0, (short)60);
        //short[][] randMap = RandomMap.CreateRandomMap(257, 257, bounds.getMin(), bounds.getMax());

        /* Create noise percent and subtract them from the max value
            so maxxed out values won't exceed the max value
         */
        int noisePercent = 10;

        short[][] randMap = RandomMap.CreateRandomMap(257, 257, bounds.getMin(), bounds.getMax());
        short[][] altitudeMap = RandomMap.createDiamondSquareMap(randMap, noisePercent, bounds);

        //Evolve map
        //for(int i = 0; i < 1; i++) altitudeMap = RandomMap.evolveMap(altitudeMap, bounds);

        //Blur map
        int neighbors = 3;
        for(int i = 0; i < 1; i++) altitudeMap = RandomMap.blurMap(altitudeMap, neighbors);

        JFrame frame = new JFrame("Visual verification of SRTM maps");

        PixelMap panel = new PixelMap(altitudeMap, bounds);

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        while(true);
    }
}
