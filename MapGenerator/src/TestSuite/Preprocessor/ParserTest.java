package TestSuite.Preprocessor;

import Preprocessor.Parser;
import SupportVectorMachine.Model.SupportVector;
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
        short[][] altitudeMap = new short[0][];
        try {
            altitudeMap = p.read("./data/raw/N32/N52E007.hgt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Visual verification of SRTM maps");

        PixelMap panel = new PixelMap(altitudeMap);

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        while(true);
    }

    private class PixelMap extends JPanel {
        private BufferedImage canvas;

        public PixelMap(short[][] altitudeMap) {
            canvas = new BufferedImage(altitudeMap[0].length, altitudeMap.length, BufferedImage.TYPE_INT_RGB);
            fillCanvas(altitudeMap);
        }

        public Dimension getPreferredSize() {
            return new Dimension(canvas.getWidth(), canvas.getHeight());
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(canvas, null, null);
        }


        public void fillCanvas(short[][] altitudeMap) {
            for(int row = 0; row < canvas.getHeight(); row++) {
                for(int col = 0; col < canvas.getWidth(); col++) {
                    int green = altitudeMap[row][col] > 0 ? (altitudeMap[row][col] * 255 / Short.MAX_VALUE) : 0;
                    int blue = altitudeMap[row][col] < 0 ? (((altitudeMap[row][col] * -1) * 255) / Short.MAX_VALUE) : 0;

                    if(green < 0 || green > 256) {
                        throw new RuntimeException("Green must be between 0 and 255. Green: " + green);
                    }

                    if(blue < 0 || blue > 256) {
                        throw new RuntimeException("Blue must be between 0 and 255. Green: " + blue);
                    }

                    Color c = new Color(0, green, blue);
                    canvas.setRGB(col, row, c.getRGB());
                }
            }

            repaint();
        }
    }
}
