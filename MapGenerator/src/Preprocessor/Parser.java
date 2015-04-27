package Preprocessor;

import SupportVectorMachine.Model.SupportVector;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        ShortBuffer sb = bb.order(ByteOrder.BIG_ENDIAN).asShortBuffer();
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

    public short[][] cut(short[][] matrix, int fromCol, int fromRow, int width, int height) {
        short[][] cutMatrix = new short[height][];
        for(int i = 0; i < height; i++) {
            cutMatrix[i] = new short[width];
            for(int j = 0; j < width; j++) {
                cutMatrix[i][j] = matrix[i + fromRow][j + fromCol];
            }
        }

        return cutMatrix;
    }

    /**
     * Cut a matrix of shorts into smaller matrixes.
     * @param matrix input matrix.
     * @param width the width of each smaller matrix.
     * @param height the height of each smaller matrix.
     * @params skip skips amount of cells between each matrix.
     * @return array of vectors, representing each smaller matrix.
     */
    public SupportVector[] parse(short[][] matrix, int width, int height, int skip) {
        if(matrix.length - height < 0 && matrix[0].length - width < 0) {
            throw new IllegalArgumentException("Cannot cut a matrix into pieces larger than it's original size.");
        }

        // We are now operating on indexes, and such starts at 0.
        width = width - 1;
        height = height - 1;

        int heightCount = matrix.length - height;
        int widthCount = matrix[0].length - width;

        ArrayList<SupportVector> vector = new ArrayList<>();

        // Iterate over matrix
        for(int i = 0; i < heightCount; i++) {
            for(int j = 0; j < widthCount; j++) {
                // Create our new partial matrix
                short[][] smallMatrix = new short[height + 1][];

                // Fill partial matrix. NOTE: I know the nested for-loops are insane, but Java's built in array-copy commands copy to the heap, which overloads the GC.
                for(int smallMatrixHeightIndex = 0; smallMatrixHeightIndex < (height + 1); smallMatrixHeightIndex++) {
                    smallMatrix[smallMatrixHeightIndex] = new short[width + 1];
                    for(int smallMatrixWidthIndex = 0; smallMatrixWidthIndex < (width + 1); smallMatrixWidthIndex++) {
                        smallMatrix[smallMatrixHeightIndex][smallMatrixWidthIndex] = matrix[i + smallMatrixHeightIndex][j + smallMatrixWidthIndex];
                    }
                }

                vector.add(new SupportVector(smallMatrix));
                if(j < widthCount + skip)
                    j = j + skip;
            }

            if(i < heightCount + skip)
                i = i + skip;
        }

        return vector.toArray(new SupportVector[vector.size()]);
    }

    public SupportVector parseSingle(short[][] matrix, int fromCol, int fromRow, int width, int height, int skip) {
        if(fromCol + width > matrix[0].length) {
            fromCol = matrix[0].length - width;
        }

        if(fromRow + height > matrix.length) {
            fromRow = matrix.length - height;
        }

        short[][] shortMatrix = cut(matrix, fromCol, fromRow, width, height);
        return parse(shortMatrix, width, height, skip)[0];
    }
}