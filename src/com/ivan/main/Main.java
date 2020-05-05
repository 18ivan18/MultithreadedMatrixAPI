package com.ivan.main;

import com.ivan.matrix.Matrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        int SIZE = 8;
        if (args[0] != null) {
            SIZE = Integer.parseInt(args[0]);
        }
        PrintStream o = null;
        try {
            o = new PrintStream(new File("outputFile.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // Assign o to output stream
        System.setOut(o);

        final int THREADS = 16;
        int[][] m = new int[SIZE][];
        for (int i = 0; i < SIZE; i++) {
            m[i] = new int[SIZE];
        }
        Random random = new Random();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                m[i][j] = random.nextInt(SIZE);
            }
        }

        for (int i = 0; i < SIZE; i++)
        {
            for (int j = 0; j < SIZE; j++)
            {
                System.out.print(m[i][j] + " ");
            }
            System.out.print("\n");
        }

        Matrix matrix = new Matrix(m);

        System.out.println("Determinant should be: " + matrix.getDeterminant());

        try {
            for (int i : IntStream.range(1, THREADS + 1).toArray()) {
                Instant start = Instant.now();
                matrix.calculateDeterminantAsync(i);
                Instant end = Instant.now();
                System.out.println("Determinant: " + matrix.getDeterminant() + " THREADS: " + i);
                System.out.println("Milliseconds : " + Duration.between(start, end).toMillis() + "\n");
            }
        } catch (InterruptedException e) {
            System.out.println("Error! " + e.getMessage());
            e.printStackTrace();
        }
    }
}
