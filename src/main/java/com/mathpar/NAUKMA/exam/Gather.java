package com.mathpar.NAUKMA.exam;

import mpi.*;

public class Gather {
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.getRank();
        int size = MPI.COMM_WORLD.getSize();

        // Assume 'matrix' is your matrix and 'maxRow' will store the row with max
        // value at index 0
        double[][] matrix = null;
        double[] maxRow = null;

        // Step 1: Each processor computes its max row
        for (double[] row : matrix) {
            if (row[0] > maxRow[0]) {
                maxRow = row;
            }
        }

        // Step 2: Gather max rows from all processors to the root (let's assume root
        // is processor 0)
        double[][] maxRows = null;
        if (rank == 0) {
            maxRows = new double[size][];
        }
        MPI.COMM_WORLD.gather(maxRow, maxRow.length, MPI.DOUBLE, maxRows,
                maxRow.length, MPI.DOUBLE, 0);

        // Step 3: Root processor computes the global max row
        if (rank == 0) {
            for (double[] row : maxRows) {
                if (row[0] > maxRow[0]) {
                    maxRow = row;
                }
            }
        }

        // Step 4: Broadcast the global max row to all processors
        MPI.COMM_WORLD.bcast(maxRow, maxRow.length, MPI.DOUBLE, 0);

        MPI.Finalize();
    }
}
