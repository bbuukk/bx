package com.mathpar.NAUKMA.exam;

import java.util.Arrays;
import java.math.BigInteger;
import java.util.Random;

import mpi.*;

public class Cast {
    public static void main(String[] args)
            throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        int n = 5;
        int[] a = new int[n];
        if (myrank == 0) {
            for (int i = 0; i < n; i++) {
                a[i] = myrank * 10 + i;
            }
            System.out.println("myrank = " + myrank + " : a = "
                    + Arrays.toString(a));
        }
        MPI.COMM_WORLD.bcast(a, a.length, MPI.INT, 0);
        if (myrank != 0)
            System.out.println("myrank = " + myrank + " : a = "
                    + Arrays.toString(a));
        MPI.Finalize();
    }
}