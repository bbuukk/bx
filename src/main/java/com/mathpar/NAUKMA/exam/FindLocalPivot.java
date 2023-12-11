package com.mathpar.NAUKMA.exam;

public class FindLocalPivot {
    public static void main(String[] args) {

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

}
