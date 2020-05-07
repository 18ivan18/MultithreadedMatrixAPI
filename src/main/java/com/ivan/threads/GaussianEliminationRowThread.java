package com.ivan.threads;

import com.ivan.matrix.Matrix;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;

public class GaussianEliminationRowThread extends Thread {
    private BigDecimal[][] matrix;
    private int i;
    private BigDecimal pivotElement;
    private int size;
    private int start;
    private int offset;
    private int threadNum;

    public GaussianEliminationRowThread(BigDecimal[][] matrix, int i, BigDecimal pivotElement,
                                        int start, int offset) {
        this.matrix = matrix;
        this.i = i;
        this.pivotElement = pivotElement;
        this.size = matrix.length;
        this.start = start + i + 1;
        this.offset = offset;
        this.threadNum = start;
    }

    @Override
    public void run() {
        for (int row = start; row < size; row+=offset) {
            for (int col = i + 1; col < size; col++) {
                matrix[row][col] = matrix[row][col].subtract(matrix[row][i].multiply(matrix[i][col])
                        .divide(pivotElement,20 , RoundingMode.HALF_UP));
            }
        }
    }
}
