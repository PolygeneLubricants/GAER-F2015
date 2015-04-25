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
        map = altitudeMap;
        int max = 5000;
        int min = -5000;

        for (int x = 0; x < canvas.getWidth(); x++) {
            for (int y = 0; y < canvas.getHeight(); y++) {
                int green = altitudeMap[x][y] > 0 ? (altitudeMap[x][y] * 255 / max) : 0;
                int blue = altitudeMap[x][y] < 0 ? ((altitudeMap[x][y] * 255) / min) : 0;

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