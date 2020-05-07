package com.ivan.matrix;


import com.ivan.threads.DeterminantCalculator;
import com.ivan.threads.GaussianEliminationRowThread;
import com.ivan.utils.AtmoicBigInteger;
import com.ivan.utils.AtomicBigDecimal;
import com.ivan.utils.Pair;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Matrix {
    private int size;
    private long determinant;
    private int[][] matrix;

    public void incrementDeterminant(long value) {
        determinant += value;
    }

    public Matrix(int[][] matrix) {
        this.setMatrix(matrix);
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
        this.size = matrix.length;
    }

    public static void calculateDeterminantAsync(int[][] matrix, int numberOfThreads) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        AtmoicBigInteger.getInstance().setValue(BigInteger.ZERO);
        ArrayList<Pair<Integer, int[][]>> tasks = getTasks(matrix, numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            executor.execute(new DeterminantCalculator(numberOfThreads, i, tasks));
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.MINUTES);
    }

    static private ArrayList<Pair<Integer, int[][]>> getTasks(int[][] matrix, int numberOfThreads) {
        int size = matrix.length;
        ArrayList<Pair<Integer, int[][]>> result = new ArrayList<>();
        int cofactor = -1;
        for (int i = 0; i < size; i++) {
            cofactor *= -1;
            result.add(new Pair<>(cofactor * matrix[0][i], getMinor(matrix, 0, i)));
        }

        if (numberOfThreads > size) {
            ArrayList<Pair<Integer, int[][]>> newResult = new ArrayList<>();
            int factor;
            int[][] minor;
            for (Pair<Integer, int[][]> integerPair : result) {
                factor = integerPair.first;
                minor = integerPair.second;
                cofactor = -1;
                for (int j = 0; j < minor.length; j++) {
                    cofactor *= -1;
                    newResult.add(new Pair<>(cofactor * factor * minor[0][j],
                            getMinor(minor, 0, j)));
                }
            }
            return newResult;
        }
        return result;
    }


    public static int[][] getMinor(int[][] matrix, int row, int col) {
        int size = matrix.length;
        int newSize = size - 1;
        int[][] minor = new int[newSize][];
        for (int i = 0; i < size - 1; i++)
        {
            minor[i] = new int[newSize];
        }
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
        StringBuilder result = new StringBuilder("");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result.append(matrix[i][j]).append(" ");
            }
            result.append("\n");
        }
        return result.toString();
    }

    public static String display(int[][] matrix) {
        StringBuilder result = new StringBuilder("");
        for (int[] ints : matrix) {
            for (int j = 0; j < matrix.length; j++) {
                result.append(ints[j]).append(" ");
            }
            result.append("\n");
        }
        return result.toString();
    }

    public static String display(BigDecimal[][] matrix) {
        StringBuilder result = new StringBuilder("");
        for (BigDecimal[] decs : matrix) {
            for (int j = 0; j < matrix.length; j++) {
                result.append(decs[j]).append(" ");
            }
            result.append("\n");
        }
        return result.toString();
    }

    public long getDeterminant() {
        return determinant;
    }


    public static long calculateDeterminant(int[][] matrix) {
        int size = matrix.length;
        if (size == 2) {
            return matrix[0][0] * matrix[1][1] -
                    matrix[0][1] * matrix[1][0];
        }
        long det = 0;
        int cofactor = -1;
        for (int i = 0; i < size; i++)
        {
            cofactor *= -1;
            int[][] minorMatrix = getMinor(matrix, 0, i);
            det += cofactor * matrix[0][i] * calculateDeterminant(minorMatrix);
        }
        return det;
    }

    private static void swap(double[][] array, int index1, int index2) {
        double[] temp = array[index1];

        array[index1] = array[index2];
        array[index2] = temp;
    }

    private static void swap(BigDecimal[][] array, int index1, int index2) {
        BigDecimal[] temp = array[index1];

        array[index1] = array[index2];
        array[index2] = temp;
    }

    public static BigDecimal calculateDeterminantGaussianElimination(int[][] input) {
            int N = input.length;
            BigDecimal det = BigDecimal.ONE;
            BigDecimal[][] matrix = new BigDecimal[N][N];
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    matrix[i][j] = BigDecimal.valueOf(input[i][j]);
                }
            }

            for (int i = 0; i < N; ++i) {

                BigDecimal pivotElement = matrix[i][i];
                int pivotRow = i;
                for (int row = i + 1; row < N; ++row) {
                    if (matrix[row][i].compareTo(pivotElement) > 0) {
                        pivotElement = matrix[row][i];
                        pivotRow = row;
                    }
                }
                if (pivotElement.equals(BigDecimal.ZERO)) {
                    return BigDecimal.ZERO;
                }
                if (pivotRow != i) {
                    swap(matrix, i, pivotRow);
                    det = det.multiply(BigDecimal.valueOf(-1));
                }
                det = det.multiply(pivotElement);


                for (int row = i + 1; row < N; ++row) {
                    for (int col = i + 1; col < N; ++col) {
                        matrix[row][col] = matrix[row][col].subtract(matrix[row][i].multiply(matrix[i][col])
                                .divide(pivotElement,20 , RoundingMode.HALF_UP));
                    }
                }
//                System.out.println(Matrix.display(matrix)+ "\n");
            }

            return det;
        }


    public static BigDecimal calculateDeterminantGaussianEliminationAsync(
            int[][] input, int numberOfThreads)
            throws InterruptedException {
        Thread[] threads = new Thread[numberOfThreads];



        int size = input.length;
        BigDecimal det = BigDecimal.ONE;
        BigDecimal[][] matrix = new BigDecimal[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = BigDecimal.valueOf(input[i][j]);
            }
        }

        for (int i = 0; i < size; ++i) {

            BigDecimal pivotElement = matrix[i][i];
            int pivotRow = i;
            for (int row = i + 1; row < size; ++row) {
                if (matrix[row][i].compareTo(pivotElement) > 0) {
                    pivotElement = matrix[row][i];
                    pivotRow = row;
                }
            }
            if (pivotElement.equals(BigDecimal.ZERO)) {
                return BigDecimal.ZERO;
            }
            if (pivotRow != i) {
                swap(matrix, i, pivotRow);
                det = det.multiply(BigDecimal.valueOf(-1));
            }
            det = det.multiply(pivotElement);


            for (int threadNumber = 0; threadNumber < numberOfThreads; threadNumber++) {
                threads[threadNumber] = new GaussianEliminationRowThread(matrix, i, pivotElement,
                        threadNumber, numberOfThreads);
                threads[threadNumber].start();
            }
            for (int threadNumber = 0; threadNumber < numberOfThreads; threadNumber++) {
                threads[threadNumber].join();
            }
        }

        return det;
    }
}
