package com.ivan.threads;

import com.ivan.matrix.Matrix;
import com.ivan.utils.AtmoicBigInteger;
import com.ivan.utils.AtomicBigDecimal;
import com.ivan.utils.Pair;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class DeterminantCalculator implements Runnable {
    private int offset;
    private int start;
    private int end;
    private ArrayList<Pair<Integer, int[][]>> tasks;

    public DeterminantCalculator(int offset, int start, ArrayList<Pair<Integer, int[][]>> tasks) {
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
            AtmoicBigInteger.getInstance().addAndGet(BigInteger.valueOf(tasks.get(i).first * Matrix.calculateDeterminant(tasks.get(i).second)));
        }

        Instant end = Instant.now();
        System.out.println(Thread.currentThread().getName() + " execution time was : " +
                Duration.between(begin, end).toMillis() + " ms");
    }
}
