package com.mathpar.NAUKMA.exam;

import mpi.MPI;
import mpi.MPIException;

public class Test {

    public static void main(String[] args) throws MPIException {

        MPI.Init(args);

        final int rank = MPI.COMM_WORLD.getRank();
        System.out.println(rank);

        MPI.Finalize();

    }

}
