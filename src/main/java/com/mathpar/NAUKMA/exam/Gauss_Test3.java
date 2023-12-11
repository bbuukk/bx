//package com.mathpar.NAUKMA.examples;
//
//import java.nio.ByteBuffer;
//import mpi.MPI;
//import mpi.Comm;
//import mpi.Status;
//
//public class Gauss_Test3 {
//
//    static final int MASTER = 0;
//    static final int N = 4; // Number of processors
//    static final int M = 10; // Matrix size
//
//    public static void main(String[] args) throws Exception {
//        MPI.Init(args);
//        Comm comm = MPI.COMM_WORLD;
//        int rank = comm.Rank();
//        int size = comm.Size();
//
//        if (rank == MASTER) {
//            // Initialize matrix A and vector b
//            double[][] A = new double[M][M];
//            double[] b = new double[M];
//            initializeMatrix(A, b);
//
//            // Divide matrix into blocks
//            double[][][] blocksA = new double[N][M / 2][M / 2];
//            double[][] blocksB = new double[N][M / 2];
//            for (int i = 0; i < N; i++) {
//                for (int j = 0; j < M / 2; j++) {
//                    System.arraycopy(A[i * M / 2 + j], 0, blocksA[i][j], 0, M / 2);
//                    System.arraycopy(b, i * M / 2 + j, blocksB[i], 0, M / 2);
//                }
//            }
//
//            // Send blocks to other processors
//            for (int i = 1; i < N; i++) {
//                comm.Send(blocksA[i], 0, blocksA[i].length, MPI.OBJECT, i, 0);
//                comm.Send(blocksB[i], 0, blocksB[i].length, MPI.DOUBLE, i, 1);
//            }
//
//            // Perform Gaussian elimination on local block
//            eliminate(blocksA[0], blocksB[0]);
//
//            // Receive results from other processors
//            for (int i = 1; i < N; i++) {
//                comm.Recv(blocksA[i], 0, blocksA[i].length, MPI.OBJECT, i, 2);
//                comm.Recv(blocksB[i], 0, blocksB[i].length, MPI.DOUBLE, i, 3);
//            }
//
//            // Combine blocks and print result
//            combineBlocks(A, b, blocksA, blocksB);
//            printMatrix(A);
//        } else {
//            // Receive blocks from master
//            double[][] blockA = new double[M / 2][M / 2];
//            double[] blockB = new double[M / 2];
//            comm.Recv(blockA, 0, blockA.length, MPI.OBJECT, MASTER, 0);
//            comm.Recv(blockB, 0, blockB.length, MPI.DOUBLE, MASTER, 1);
//
//            // Perform Gaussian elimination on local block
//            eliminate(blockA, blockB);
//
//            // Send results back to master
//            comm.Send(blockA, 0, blockA.length, MPI.OBJECT, MASTER, 2);
//            comm.Send(blockB, 0, blockB.length, MPI.DOUBLE, MASTER, 3);
//        }
//
//        MPI.Finalize();
//    }
//
//    private static void initializeMatrix(double[][] A, double[] b) {
//        // Fill A and b with random values
//        for (int i = 0; i < M; i++) {
//            for (int j = 0; j < M; j++) {
//                A[i][j] = Math.random();
//            }
//            b[i] = Math.random();
//        }
//    }
//
//    private static void eliminate(double[][] A, double[] b) {
//        for (int k = 0; k < M - 1; k++) {
//            // Forward elimination
//            for (int i = k + 1; i < M; i++) {
//                double factor = A[i][k] / A[k][k];
//                for (int j = k + 1; j < M; j++) {
//                    A[i][j] -= factor * A[k][j];
//                }
//                b[i] -= factor * b[k];
//            }
//        }
//    }
//
