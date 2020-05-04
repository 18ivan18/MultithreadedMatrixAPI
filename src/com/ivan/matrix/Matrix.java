package com.ivan.matrix;


import com.ivan.threads.DeterminantCalculator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Matrix {
    private int size;
    private int determinant;
    private int[][] matrix;

    public void incrementDeterminant(int value) {
        determinant += value;
    }

    public Matrix(int[][] matrix) {
        this.setMatrix(matrix);
        determinant = calculateDeterminant(matrix);
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
        this.size = matrix.length;
    }

    @Override
    public String toString() {
        return "Matrix{" +
                ", size=" + size +
                ", determinant=" + determinant +
                ", matrix=" + display() +
                '}';
    }

    public void calculateDeterminantAsync(int numberOfThreads) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        determinant = 0;
        for (int i = 0; i < numberOfThreads; i++) {
            executor.execute(new DeterminantCalculator(this, numberOfThreads, i));
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
    }

    public int getSize() {
        return size;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public static int[][] getMinor(int[][] matrix, int size, int row, int col) {
        int newSize = size - 1;
        int[][] minor = new int[newSize][newSize];
        int k, t = 0;
        for (int i = 0; i < size; i++)
        {
            if (i == row)
            {
                continue;
            }
            k = 0;
            for (int j = 0; j < size; j++)
            {
                if (j != col)
                {
                    minor[t][k] = matrix[i][j];
                    k++;
                }
            }
            t++;
        }
        return minor;
    }

    public String display() {
        StringBuffer result = new StringBuffer("");
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                result.append(matrix[i][j]).append(" ");
            }
            result.append("\n");
        }
        return result.toString();
    }

    public static String display(int[][] matrix) {
        StringBuffer result = new StringBuffer("");
        for (int i = 0; i < matrix.length; i++)
        {
            for (int j = 0; j < matrix.length; j++)
            {
                result.append(matrix[i][j]).append(" ");
            }
            result.append("\n");
        }
        return result.toString();
    }

    public int getDeterminant() {
        return determinant;
    }


    public static int calculateDeterminant(int[][] matrix) {
        int size = matrix.length;
        if (size == 2) {
            return matrix[0][0] * matrix[1][1] -
                    matrix[0][1] * matrix[1][0];
        }
        else {
            int det = 0, cofactor = -1;
            for (int i = 0; i < size; i++)
            {
                cofactor *= -1;
                int[][] minorMatrix = getMinor(matrix, size, 0, i);
                det += cofactor * matrix[0][i] * calculateDeterminant(minorMatrix);
            }
            return det;
        }
    }
}
