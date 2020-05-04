package com.ivan.threads;

import com.ivan.matrix.Matrix;

import java.time.Duration;
import java.time.Instant;

public class DeterminantCalculator implements Runnable {

    private Matrix matrix;
    private int offset;
    private int start;
    private int end;

    public DeterminantCalculator(Matrix matrix, int offset, int start) {
        this.matrix = matrix;
        this.offset = offset;
        this.start = start;
        this.end = matrix.getSize();
    }

    @Override
    public String toString() {
        return "DeterminantCalculator{" +
                "matrix=" + matrix +
                ", offset=" + offset +
                ", start=" + start +
                ", end=" + end +
                '}';
    }

    @Override
    public void run() {
        Instant begin = Instant.now();
        if (matrix.getSize() == 2) {
            matrix.incrementDeterminant(matrix.getMatrix()[0][0] * matrix.getMatrix()[1][1] -
                    matrix.getMatrix()[0][1] * matrix.getMatrix()[1][0]);
        }
        else {
            int cofactor;
            for (int i = start; i < end; i+=offset)
            {
                cofactor = (int)Math.pow(-1, i);
                int[][] minorMatrix = Matrix.getMinor(matrix.getMatrix(), matrix.getSize(), 0, i);
                matrix.incrementDeterminant(cofactor * matrix.getMatrix()[0][i] *
                        matrix.calculateDeterminant(minorMatrix));
            }
        }
        Instant end = Instant.now();
        System.out.println(Thread.currentThread().getName() + " execution time was : " +
                Duration.between(begin, end).toMillis() + " ms");
    }
}
