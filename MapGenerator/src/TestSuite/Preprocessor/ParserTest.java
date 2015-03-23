package TestSuite.Preprocessor;

import Preprocessor.Parser;
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

    @Test
    public void testSerialization() {

    }

    @Test
    public void testDeSerialization() {

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
            for (int x = 0; x < canvas.getWidth(); x++) {
                for (int y = 0; y < canvas.getHeight(); y++) {
                    int green = altitudeMap[x][y] > 0 ? (altitudeMap[x][y] * 255 / Short.MAX_VALUE) : 0;
                    int blue = altitudeMap[x][y] < 0 ? ((altitudeMap[x][y] * -1 * 255) / Short.MAX_VALUE) : 0;

                    Color c = new Color(0, green, blue);
                    canvas.setRGB(x, y, c.getRGB());
                }
            }
            repaint();
        }
    }
}
