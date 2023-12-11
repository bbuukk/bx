package com.mathpar.NAUKMA.exam;

import java.util.Arrays;

public class ForElimLens {

    static final int NP = 4;
    static final int ord = 9;
    static final int len = ord / NP;
    //    static final int[] lengths = getLengths(1);
    static final int[] lengths = {2, 3};


    public static void main(String[] args) {

        int activeColumn = 0;

        double[][] matrix = generateMatrix(ord, ord + 1);
        printMatrix(matrix);
        System.out.println(Arrays.toString(lengths));
        System.out.println();

        int pivotRowIdx = findIdxPivotRow(matrix, activeColumn);
        double[] pivotRow = matrix[pivotRowIdx];
        printRow(pivotRow);

        swapRows(matrix, pivotRowIdx, activeColumn);
        printMatrix(matrix);

        forwardElimination(matrix, pivotRow, activeColumn);
        printMatrix(matrix);

        ///////

//        ++activeColumn;
//        pivotRowIdx = findIdxPivotRow(matrix, activeColumn);
//        pivotRow = matrix[pivotRowIdx];
//        printRow(pivotRow);
//
//        swapRows(matrix, pivotRowIdx, activeColumn);
//        printMatrix(matrix);
//
//        forwardElimination(matrix, pivotRow, activeColumn);
//        printMatrix(matrix);
    }

    public static int[] getLengths(int rank) {
        int[] lens = new int[len];
        for (int i = 0; i < lens.length; i++) {
            lens[i] = rank * len + i;
        }

        return lens;
    }

    public static void forwardElimination(double[][] matrix, double[] pivotRow, int activeColumn) {
        for (int i = activeColumn + 1; i < matrix.length; i++) {
            double factor = matrix[i][activeColumn] / pivotRow[activeColumn];
            for (int j = activeColumn; j < matrix.length + 1; j++) {
                if (lengths[i] <= j + 1) {
                    break;
                }
                matrix[i][j] -= factor * pivotRow[j];
            }
        }
    }

    private static int findIdxPivotRow(double[][] matrix, int k) {
        double maxPivot = Math.abs(matrix[k][k]);
        int pivotRow = k;

        int matLen = matrix.length;
        for (int i = k + 1; i < matLen; i++) {
            double pivot = Math.abs(matrix[i][k]);
            if (pivot > maxPivot) {
                maxPivot = pivot;
                pivotRow = i;
            }
        }
        return pivotRow;
    }

    public static void swapRows(double[][] matrix, int pivotRowIdx, int activeColumn) {
        double[] temp = matrix[activeColumn];
        matrix[activeColumn] = matrix[pivotRowIdx];
        matrix[pivotRowIdx] = temp;
    }

    public static void printMatrix(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.printf("%.2f ", matrix[i][j]);
            }
            System.out.println();
        }
//        System.out.println("\n");
        System.out.println();
    }

    public static void printRow(double[] row) {
        for (int j = 0; j < row.length; j++) {
            System.out.printf("%.2f ", row[j]);
        }
        System.out.println('\n');
    }

    public static double[][] generateMatrix(int rows, int cols) {
        double[][] matrix = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = (int) (Math.random() * 10);
            }
        }
        return matrix;
    }
}

