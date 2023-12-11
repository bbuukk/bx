package com.mathpar.NAUKMA.exam;

import java.util.Arrays;

public class Lens {

    static final int NP = 4;
    static final int ord = 13;
    static final int len = ord / NP;
    static final int lastPLen = ord - len * (NP - 1);

    public static void main(String[] args) {

        System.out.println(Arrays.toString(getLengths(0)));
        System.out.println(Arrays.toString(getLengths(1)));
        System.out.println(Arrays.toString(getLengths(2)));
        System.out.println(Arrays.toString(getLengths(3)));


    }

    public static int[] getLengths(int rank) {
        int[] lens = new int[len];
        for (int i = 0; i < lens.length; i++) {
            lens[i] = rank * len + i;
        }

        return lens;
    }


}
