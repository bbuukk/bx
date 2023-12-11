package com.mathpar.NAUKMA.exam;

import mpi.MPI;
import mpi.MPIException;
import mpi.Status;

import java.io.*;
import java.util.Arrays;

public class Exam {

    static final int NP = 4;
    static final int ord = 9;
    static final int len = ord / NP;

    public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {
        MPI.Init(args);

        final int rank = MPI.COMM_WORLD.getRank();

        final int MASTER_NODE = 0;
        final int MESSAGE_TAG = 3000;

        double[] localPivotRow = new double[ord + 1];
        double[] globalPivotRow = new double[ord + 1];
        int activeColumn = 0;
        double[][] part = null;

        if (rank == MASTER_NODE) {
            double[][] matrix = generateMatrix(ord, ord + 1);
            printMatrix(matrix);

            double[][][] splitMatrix = splitMatrix(matrix, NP);
            part = splitMatrix[0];
            sendObject(splitMatrix[1], 1, MESSAGE_TAG);
            sendObject(splitMatrix[2], 2, MESSAGE_TAG);
            sendObject(splitMatrix[3], 3, MESSAGE_TAG);

        } else {
            part = (double[][]) recvObject(MASTER_NODE, MESSAGE_TAG);
        }

        int pivotRowIdx = findIdxPivotRow(part, activeColumn);
        localPivotRow = part[pivotRowIdx];

        ///

        double[] localPivotRows = null;
        if (rank == MASTER_NODE) {
            localPivotRows = new double[(ord + 1) * NP];
        }

        MPI.COMM_WORLD.gather(localPivotRow, localPivotRow.length, MPI.DOUBLE, localPivotRows, localPivotRow.length,
                MPI.DOUBLE, MASTER_NODE);

        if (rank == MASTER_NODE) {
            globalPivotRow = findGlobalPivotRow(localPivotRows);
        }

        MPI.COMM_WORLD.bcast(globalPivotRow, ord + 1, MPI.DOUBLE, MASTER_NODE);

        System.out.println("Rank: " + rank + "\n" + Arrays.toString(globalPivotRow));

        // forwardElimination(part, pivotRow, activeColumn);

        MPI.Finalize();
    }

    // public static int[] getLengths(int rank) {
    // int[] lens = new int[len];
    // for (int i = 0; i < lens.length; i++) {
    // lens[i] = rank * len + i;
    // }

    // return lens;
    // }

    public static double[] findGlobalPivotRow(double[] localPivotRows) {
        double[] globalPivotRow = new double[ord + 1];
        globalPivotRow[0] = -1;

        for (int i = 0; i < localPivotRows.length; i += ord + 1) {
            double[] pivotRow = Arrays.copyOfRange(localPivotRows, i, i + ord + 1);
            if (pivotRow[0] > globalPivotRow[0]) {
                globalPivotRow = pivotRow;
            }

        }

        return globalPivotRow;
    }

    public static void forwardElimination(double[][] matrix, double[] pivotRow, int activeColumn) {
        for (int i = activeColumn + 1; i < matrix.length; i++) {
            double factor = matrix[i][activeColumn] / pivotRow[activeColumn];
            for (int j = activeColumn; j < matrix.length + 1; j++) {
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
        // System.out.println("\n");
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

    public static double[][][] splitMatrix(double[][] matrix, int parts) {
        int n = matrix.length;
        int size = n / parts;
        int remainder = n % parts;
        double[][][] result = new double[parts][][];
        for (int i = 0; i < parts; i++) {
            int start = i * size;
            int end = (i == parts - 1) ? n : start + size;
            int subSize = end - start;
            result[i] = new double[subSize][];
            System.arraycopy(matrix, start, result[i], 0, subSize);
        }
        return result;
    }

    // serialize
    public static void sendObject(Object a, int proc, int tag) throws MPIException, IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(a);
        byte[] tmp = bos.toByteArray();
        MPI.COMM_WORLD.send(tmp, tmp.length, MPI.BYTE, proc, tag);
    }

    // deserialize
    public static Object recvObject(int proc, int tag) throws MPIException, IOException, ClassNotFoundException {

        Status st = MPI.COMM_WORLD.probe(proc, tag);

        // статичний метод класу Status,
        // який пiдраховує кiлькiсть елементiв
        // у буферi (в цьому випадку MPI.BYTE)
        int size = st.getCount(MPI.BYTE);

        // створюємо байт-масив
        byte[] tmp = new byte[size];

        // recv - блокувальний прийом масиву з
        // буфера запису в масив tmp
        MPI.COMM_WORLD.recv(tmp, size, MPI.BYTE, proc, tag);
        Object res = null;

        ByteArrayInputStream bis = new ByteArrayInputStream(tmp);
        ObjectInputStream ois = new ObjectInputStream(bis);
        res = ois.readObject();
        // передаємо на вихiд процедури отриманий об’єкт
        return res;
    }

    public static Object bcastObject(Object o, int root)
            throws IOException, MPIException,
            ClassNotFoundException {
        byte[] tmp = null;
        int[] size = new int[1];
        int rank = MPI.COMM_WORLD.getRank();
        if (rank == root) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(o);
            tmp = bos.toByteArray();
            size[0] = tmp.length;
        }
        MPI.COMM_WORLD.bcast(size, 1, MPI.INT, root);
        if (rank != root)
            tmp = new byte[size[0]];
        MPI.COMM_WORLD.bcast(tmp, tmp.length,
                MPI.BYTE, root);
        if (rank != root) {
            ByteArrayInputStream bis = new ByteArrayInputStream(tmp);
            ObjectInputStream ois = new ObjectInputStream(bis);
            return ois.readObject();
        }
        return o;
    }
}
