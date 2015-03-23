package Preprocessor;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;

/**
 * User: AndreasRydingLund
 * Date: 23-03-2015
 */
public class Parser {
    public short[][] read(String filePath) throws IOException {
        FileChannel fc;
        fc = new FileInputStream(filePath).getChannel();

        ByteBuffer bb = ByteBuffer.allocateDirect((int) fc.size());
        while (bb.remaining() > 0) fc.read(bb);
        fc.close();
        bb.flip();

        // Choose the right endianness
        ShortBuffer sb = bb.order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
        short[][] matrix = new short[1201][];
        int rowNumber = 0;
        while (sb.remaining() > 0) {
            short[] row = new short[1201];
            for (int i = 0; i < 1201; i++) {
                row[i] = sb.get();
            }

            matrix[rowNumber] = row;
            rowNumber++;
        }

        return matrix;
    }
}