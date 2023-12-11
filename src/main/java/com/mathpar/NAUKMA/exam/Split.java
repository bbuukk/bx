package com.mathpar.NAUKMA.exam;

import java.util.Arrays;

import java.util.Arrays;

public class Split {
    public static void main(String[] args) {
        int[][] matrix = {
                {1, 2, 3, 4},
                {4, 5, 6, 7},
                {7, 8, 9, 10},
                {7, 8, 9, 10},
                {7, 8, 9, 10},
                {7, 8, 9, 10},
                {7, 8, 9, 10},
                {7, 8, 9, 10},
                {7, 8, 9, 10}
        };
        int[][][] splitMatrix = splitMatrix(matrix, 4);
        for (int[][] subMatrix : splitMatrix) {
            for (int[] row : subMatrix) {
                System.out.println(Arrays.toString(row));
            }
            System.out.println();
        }
    }

    public static int[][][] splitMatrix(int[][] matrix, int parts) {
        int n = matrix.length;
        int size = n / parts;
        int remainder = n % parts;
        int[][][] result = new int[parts][][];
        for (int i = 0; i < parts; i++) {
            int start = i * size;
            int end = (i == parts - 1) ? n : start + size;
            int subSize = end - start;
            result[i] = new int[subSize][];
            System.arraycopy(matrix, start, result[i], 0, subSize);
        }
        return result;
    }
}
