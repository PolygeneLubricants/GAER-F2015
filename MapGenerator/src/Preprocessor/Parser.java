package Preprocessor;

import SupportVectorMachine.Model.SupportVector;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

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

    /**
     * Cut a matrix of shorts into smaller matrixes.
     * @param matrix input matrix.
     * @param width the width of each smaller matrix.
     * @param height the height of each smaller matrix.
     * @return array of vectors, representing each smaller matrix.
     */
    public SupportVector[] parse(short[][] matrix, int width, int height) {
        if(matrix.length - height < 0 && matrix[0].length - width < 0) {
            throw new IllegalArgumentException("Cannot cut a matrix into pieces larger than it's original size.");
        }

        int heightCount = matrix.length - height + 1;
        int widthCount = matrix[0].length - width + 1;

        SupportVector[] vector = new SupportVector[heightCount * widthCount];
        int curIndex = 0;

        for(int i = 0; i < heightCount; i++) {
            for(int j = 0; j < widthCount; j++) {
                short[][] smallMatrix = new short[height][];

                for(int smallMatrixHeightIndex = 0; smallMatrixHeightIndex < height; smallMatrixHeightIndex++) {
                    smallMatrix[smallMatrixHeightIndex] = Arrays.copyOfRange(matrix[i + smallMatrixHeightIndex], j, j + width);
                }

                vector[curIndex++] = new SupportVector(smallMatrix);
            }
        }

        return vector;
    }
}