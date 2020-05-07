package com.ivan.main;

import com.ivan.matrix.Matrix;
import com.ivan.utils.AtmoicBigInteger;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.IntStream;


public class Main {

    public static void main(String[] args) {
        int SIZE = 256;
        int THREADS = 16;
        String outputFile = "outputFile.txt";
        String inputFile = "inputFile.txt";
        int[][] m = null;
        boolean ge = true;
        boolean quiet = false;


        Options options = new Options();
        options.addOption("n", true, "Random matrix with size n. Size is 256 by default.")
                .addOption("i",  "inputFile", true, "Sets input file.")
                .addOption("t", "tasks", true, "Max no. of threads to execute at a time. Required parameter.")
                .addOption("q", "quiet",false,"Opens app in quiet mode. Disabled by default.")
                .addOption("o", "outputFile", true, "Sets output file.")
                .addOption("alg","algorithm",true,"Choose algorithm for calculation determinant:\n" +
                        "Possible options: Gaussian Elimination set by default (use argument ge) and Laplace Expansion (use argument le)");



        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            if(!cmd.hasOption("t")) {
                printUsage(options);
                return;
            } else {
                THREADS = Integer.parseInt(cmd.getOptionValue("t"));
            }
            if (cmd.hasOption("alg") && cmd.getOptionValue("alg").equals("le")) {
                ge = false;
            }
            if(cmd.hasOption("n")) {
                SIZE = Integer.parseInt(cmd.getOptionValue("n"));
                m = generateRandomMatrix(SIZE);
            } else if(cmd.hasOption("i")) {
                inputFile = cmd.getOptionValue("i");
                m = loadMatrixFromFile(inputFile);
            }
            if (cmd.hasOption("q")) {
                quiet = true;
            }
        } catch (ParseException e) {
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

        System.out.println("Params: \nTreads: " + THREADS + "   \nSize: " + SIZE + " \nOutput file: " + outputFile + "\nAlgorithm: " +
                (ge ? "Gaussian Elimination\n" : "Laplace expansion\n"));

        if(!ge) {
            try {
                for (int i : IntStream.range(1, THREADS + 1).toArray()) {
                    Instant start = Instant.now();
                    Matrix.calculateDeterminantAsync(m, i);
                    Instant end = Instant.now();
                    if (!quiet) {
                        System.out.println("Determinant: " + AtmoicBigInteger.getInstance().getValue());
                    }
                    System.out.println("Milliseconds : " + Duration.between(start, end).toMillis() + "\nTHREADS: " + i);
                }
            } catch (InterruptedException e) {
                System.out.println("Error! " + e.getMessage());
                e.printStackTrace();
            }
        } else {

            for (int i : IntStream.range(1, THREADS + 1).toArray()) {
                try {
                    Instant start = Instant.now();
                    BigDecimal determinant = Matrix.calculateDeterminantGaussianEliminationAsync(m, i);
                    Instant end = Instant.now();

                    System.out.println("Milliseconds : " + Duration.between(start, end).toMillis() + "\nTHREADS: " + i);
                    if (!quiet) {
                        System.out.println("Determinant: " + determinant);
                    }
                } catch (InterruptedException e) {
                    System.out.println("Error! " + e.getMessage());
                    e.printStackTrace();
                }
            }
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
