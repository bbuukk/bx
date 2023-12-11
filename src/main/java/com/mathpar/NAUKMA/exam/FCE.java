package com.mathpar.NAUKMA.exam;

public class FCE {
    public static void forwardEliminateFirstColumn(double[][] matrix) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;

        // Find the pivot row for the first column
        int pivotRow = 0;
        for (int i = 1; i < numRows; i++) {
            if (Math.abs(matrix[i][0]) > Math.abs(matrix[pivotRow][0])) {
                pivotRow = i;
            }
        }

        // Swap the pivot row with the first row
        double[] temp = matrix[0];
        matrix[0] = matrix[pivotRow];
        matrix[pivotRow] = temp;

        // Perform forward elimination on the first column
        for (int i = 1; i < numRows; i++) {
            double factor = matrix[i][0] / matrix[0][0];
            for (int j = 0; j < numCols; j++) {
                matrix[i][j] -= factor * matrix[0][j];
            }
        }
    }

    public static void main(String[] args) {
        double[][] matrix = {
                {2, 1, -1},
                {-3, -1, 2},
                {-2, 1, 2}
        };
        printMatrix(matrix);

        forwardEliminateFirstColumn(matrix);

        printMatrix(matrix);
    }

    public static void printMatrix(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
