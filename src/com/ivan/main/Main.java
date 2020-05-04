package com.ivan.main;

import com.ivan.matrix.Matrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        PrintStream o = null;
        try {
            o = new PrintStream(new File("outputFile.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // Assign o to output stream
        System.setOut(o);

        //works
        int[][] m = {   { 4, 7, 8, 6, 4, 6, 7, 3, 10, 2},
                { 3, 8, 1, 10, 4, 7, 1, 7, 3, 7 },
                { 12, 9, 8, 10, 3, 1, 3, 4, 8, 6 },
                { 10, 3, 3, 9, 10, 8, 4, 7, 2, 3 },
                { 10, 4, 2, 10, 5, 8, 9, 5, 6, 1, },
                { 7, 2, 1, 7, 4, 3, 1, 7, 2, 6 },
                { 5, 8, 7, 6, 7, 10, 4, 8, 5, 6 },
                { 3, 6, 5, 8, 5, 5, 4, 1, 8, 9 },
                { 7, 9, 9, 5, 4, 2, 5, 10, 3, 1 },
                { 7, 9, 10, 3, 7, 7, 5, 10, 6, 4}
        };
        Matrix matrix = new Matrix(m);

        System.out.println("Determinant should be: " + matrix.getDeterminant());

        try {
            for (int i : IntStream.range(1, 11).toArray()) {
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
