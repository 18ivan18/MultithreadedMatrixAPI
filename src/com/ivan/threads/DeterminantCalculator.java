package com.ivan.threads;

import com.ivan.matrix.Matrix;
import com.ivan.utils.Pair;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class DeterminantCalculator implements Runnable {

    private Matrix matrix;
    private int offset;
    private int start;
    private int end;
    ArrayList<Pair<Integer, int[][]>> tasks;

    public DeterminantCalculator(Matrix matrix, int offset, int start, ArrayList<Pair<Integer, int[][]>> tasks) {
        this.matrix = matrix;
        this.offset = offset;
        this.start = start;
        this.end = tasks.size();
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        return "DeterminantCalculator{" +
                " offset=" + offset +
                ", start=" + start +
                ", end=" + end +
                ", tasks=" + tasks +
                '}';
    }

    @Override
    public void run() {
        Instant begin = Instant.now();
        for (int i = start; i < end; i+=offset)
        {
            matrix.incrementDeterminant(tasks.get(i).first * Matrix.calculateDeterminant(tasks.get(i).second));
        }

        Instant end = Instant.now();
        System.out.println(Thread.currentThread().getName() + " execution time was : " +
                Duration.between(begin, end).toMillis() + " ms");
    }
}
