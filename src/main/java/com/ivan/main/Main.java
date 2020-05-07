package com.ivan.main;

import com.ivan.matrix.Matrix;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.IntStream;


public class Main {

    public static void main(String[] args) {
        int SIZE = 512;
        int THREADS = 16;
        String outputFile = "outputFile.txt";
        String inputFile = "inputFile.txt";
        int[][] m = null;


        Options options = new Options();
        options.addOption("n", true, "Random matrix with size n.")
                .addOption("i",  "inputFile", true, "Sets input file.")
                .addOption("t", "tasks", true, "Max no. of threads to execute at a time.")
                .addOption("q", "quiet",false,"Opens app in quiet mode.")
                .addOption("o", "outputFile", true, "Sets output file.");
//                .addOption("a","algorithm",true,"Choose algorithm for calculation determinant:\n" +
//                        "Possible options: Gaussian Elimination (use argument ge) and Laplace Expansion (use argument le");



        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            if(!cmd.hasOption("t")) {
                printUsage(options);
                return;
            } else {
                THREADS = Integer.parseInt(cmd.getOptionValue("t"));
            }
            if(cmd.hasOption("n")) {
                SIZE = Integer.parseInt(cmd.getOptionValue("n"));
                m = generateRandomMatrix(SIZE);
            } else if(cmd.hasOption("i")) {
                inputFile = cmd.getOptionValue("i");
                m = loadMatrixFromFile(inputFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        PrintStream o = null;
        try {
            o = new PrintStream(new File(outputFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // Assign o to output stream
        System.setOut(o);

        System.out.println("Params: \nTreads: " + THREADS + "   \nSize: " + SIZE + " \nOutput file: " + outputFile);

//        Matrix matrix = new Matrix(m);

//        System.out.println("Determinant should be: " + Matrix.calculateDeterminantGaussianElimination(m));
//        System.out.println("Determinant should be: " + Matrix.calculateDeterminant(m));
//
//        try {
//            for (int i : IntStream.range(1, THREADS + 1).toArray()) {
//                Instant start = Instant.now();
//                matrix.calculateDeterminantAsync(i);
//                Instant end = Instant.now();
//                System.out.println("Determinant: " + matrix.getDeterminant() + " THREADS: " + i);
//                System.out.println("Milliseconds : " + Duration.between(start, end).toMillis() + "\n");
//            }
//        } catch (InterruptedException e) {
//            System.out.println("Error! " + e.getMessage());
//            e.printStackTrace();
//        }

       for (int i : IntStream.range(1, THREADS + 1).toArray()) {
            Instant start = Instant.now();
            try {
                System.out.println("Determinant: " + Matrix.calculateDeterminantGaussianEliminationAsync(m, i)
                        + " \nTHREADS: " + i);
            } catch (InterruptedException e) {
                System.out.println("Error! " + e.getMessage());
                e.printStackTrace();
            }
            Instant end = Instant.now();
            System.out.println("Milliseconds : " + Duration.between(start, end).toMillis() + "\n");
        }


    }

    private static void printUsage(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Multithreaded Matrix API", options);
    }

    private static int[][] loadMatrixFromFile(String inputFile) {
        int[][] m;
        Scanner input = null;
        try {
            input = new Scanner (new File(inputFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int size = input.nextInt();

        m = new int[size][size];
        for(int i = 0; i < size; ++i)
        {
            for(int j = 0; j < size; ++j)
            {
                if(input.hasNextInt())
                {
                    m[i][j] = input.nextInt();
                }
            }
        }
        return m;
    }

    private static int[][] generateRandomMatrix(int size) {
        int[][] m = new int[size][size];
                Random random = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                m[i][j] = random.nextInt(size);
            }
        }

        return m;
    }
}
