package GUI;

import RandomMapGenerator.RandomMap;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * User: AndreasRydingLund
 * Date: 07-04-2015
 */
public class PixelMap extends JPanel {
    private BufferedImage canvas;
    private short[][] map;

    public PixelMap(short[][] altitudeMap) {
        map = altitudeMap;
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
        short maxHeight = RandomMap.FindMaxHeight(altitudeMap);
        for(int row = 0; row < canvas.getHeight(); row++) {
            for(int col = 0; col < canvas.getWidth(); col++) {
                int green = altitudeMap[row][col] > 0 ? (altitudeMap[row][col] * 255 / maxHeight) : 0;
                int blue = altitudeMap[row][col] < 0 ? (((altitudeMap[row][col] * -1) * 255) / maxHeight) : 0;

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

    public short[][] getMap() {
        return map;
    }

    public void setMap(short[][] map) {
        this.map = map;
    }
}