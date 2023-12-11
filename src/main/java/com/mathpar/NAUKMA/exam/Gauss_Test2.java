//package com.mathpar.NAUKMA.examples;
//
//import mpi.MPI;
//import mpi.MPIException;
//
//public class Gauss_Test2 {
//
//    private static final int N = 4; // Matrix dimension
//    private static final int BLOCK_SIZE = N / 4; // Block size for parallel processing
//
//    public static void main(String[] args) throws MPIException {
//        MPI.Init(args);
//        int rank = MPI.COMM_WORLD.getRank();
//        int size = MPI.COMM_WORLD.getSize();
//
//        // Initialize matrix
//        double[][] matrix = initializeMatrix();
//
//        // Perform Gaussian elimination
//        for (int k = 0; k < N; k++) {
//            // Pivot selection and broadcast
//            int pivotRow = findPivotRow(matrix, k, rank);
//            if (rank != 0) {
//                MPI.COMM_WORLD.recv(matrix[k], 0, N, MPI.DOUBLE, 0, 0); // всі крім нульового отримали блок
//            } else {
//                MPI.COMM_WORLD.bcast(matrix[k], N+1, MPI.DOUBLE, 0);
//            }
//
//            // Elimination within block
//            for (int i = k + 1; i < k + BLOCK_SIZE; i++) {
//                eliminateRow(matrix, k, i);
//            }
//
//            // Elimination in other blocks
//            for (int p = 1; p < size; p++) {
//                if (p % 2 == 0) {
//                    MPI.COMM_WORLD.send(matrix[k], 0, N, MPI.DOUBLE, p, 0);
//                    MPI.COMM_WORLD.recv(matrix[k + BLOCK_SIZE + p], 0, N, MPI.DOUBLE, p, 0);
//                } else {
//                    MPI.COMM_WORLD.recv(matrix[k + BLOCK_SIZE + p], 0, N, MPI.DOUBLE, p, 0);
//                    MPI.COMM_WORLD.send(matrix[k], 0, N, MPI.DOUBLE, p, 0);
//                }
//            }
//        }
//
//        // Forward and backward substitution
//        if (rank == 0) {
//            forwardSubstitution(matrix);
//            backwardSubstitution(matrix);
//            printMatrix(matrix);
//        }
//
//        MPI.Finalize();
//    }
//
//    private static double[][] initializeMatrix() {
//        // Initialize a random matrix
//        double[][] matrix = new double[N][N + 1];
//        for (int i = 0; i < N; i++) {
//            for (int j = 0; j <= N; j++) {
//                matrix[i][j] = Math.random();
//            }
//        }
//        return matrix;
//    }
//
//    private static int findPivotRow(double[][] matrix, int k, int rank) throws MPIException {
//        double maxPivot = Math.abs(matrix[k][k]);
//        int pivotRow = k;
//
//        if (rank == 0) {
//            for (int i = k + 1; i < N; i++) {
//                double pivot = Math.abs(matrix[i][k]);
//                if (pivot > maxPivot) {
//                    maxPivot = pivot;
//                    pivotRow = i;
//                }
//            }
//        }
//
//        // Broadcast pivot row
//        MPI.COMM_WORLD.bcast(new int[]{pivotRow}, 0, 1, MPI.INT, 0);
//
//        return pivotRow;
//    }
//
//    private static void eliminateRow(double[][] matrix, int pivotRow, int i) {
//        double factor = -matrix[i][pivotRow] / matrix[pivotRow][pivotRow];
//        for (int j = pivotRow; j <= N; j++) {
//            matrix[i][j] += matrix[pivotRow][j] * factor;
//        }
//    }
//
//    private static void forwardSubstitution(double[][] matrix) {
//        for (int k = 1; k < N; k++) {
//            for (int i = k + 1; i < N; i++) {
//                matrix[i][N] -= matrix[k][N] * matrix[i][k];
//                matrix[i][k] = 0;
//            }
//        }
//    }
//
//    private static void backwardSubstitution(double[][] matrix) {
//        for (int i = N - 1; i >= 0; i--) {
//            double sum = 0;
//            for (int j = i + 1; j < N; j++) {
//                sum += matrix[i][j]
//
