package GUI;

import RandomMapGenerator.RandomMap;
import SupportVectorMachine.Model.AltitudeBoundPair;

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
    private AltitudeBoundPair bounds;

    public PixelMap(short[][] altitudeMap, AltitudeBoundPair b) {
        map = altitudeMap;
        bounds = b;
        canvas = new BufferedImage(altitudeMap[0].length, altitudeMap.length, BufferedImage.TYPE_INT_RGB);
        fillCanvas(altitudeMap);
    }

    public PixelMap(short[][] altitudeMap) {
        bounds = new AltitudeBoundPair((short)5000, (short) -5000);
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
        map = altitudeMap;
        int max = bounds.getMax() == 0 ? 1 : bounds.getMax(); // Max altitude in the data set.
        int min = bounds.getMin() == 0 ? 1 : bounds.getMin(); // Min altitude in the data set

        for (int x = 0; x < canvas.getWidth(); x++) {
            for (int y = 0; y < canvas.getHeight(); y++) {
                int green = altitudeMap[x][y] > 0 ? (altitudeMap[x][y] * 255 / max) : 0;
                int blue = altitudeMap[x][y] < 0 ? ((altitudeMap[x][y] * 255) / min) : 0;

                if(green > 255)
                    throw new RuntimeException(String.valueOf(green));
                if(green < 0)
                    throw new RuntimeException(String.valueOf(green));
                Color c = new Color(0, green, blue);
                canvas.setRGB(x, y, c.getRGB());
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