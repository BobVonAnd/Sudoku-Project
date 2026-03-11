package com.sudoku.model;
import java.util.ArrayList;

public class algoXSolver {

    public void algoXManager(SudokuBoard sudokuBoard){
        int[][] matrix = matrixCreate(sudokuBoard);
        int[][] sparseMatrix = sparseMatrixCreate(4);

    }

    public int[][] dlx (int[][] sparseMatrix){
        boolean notSolved = true;
        int size = sparseMatrix.length;
        int colIndex = 0;
        ArrayList<int[]> partialSolution = new ArrayList<int[]>();
        if (sparseMatrix[0].length == 0){
            notSolved = false;
        }
        //Finds a row in the first column that holds a 1
        for (int i = 0; i<sparseMatrix.length; i++){
            if (sparseMatrix[i][colIndex] == 1){//if it does check if it is in the partial solution
                partialSolution.add(takeRow(sparseMatrix, i));
                for (int j = 0; j<sparseMatrix.length ; j++){
                    if (sparseMatrix[i][j] == 1){
                        sparseMatrix = removeColumn(sparseMatrix, j);
                    }
                }
                sparseMatrix = removeRow(sparseMatrix, i);
            }

        }


        return matrix;
    }
    public int[] takeRow(int[][] matrix, int rowIndex){
        int[] rowOfMatrix = new int[matrix.length];
        int size = matrix.length;
        for (int i = 0; i<size; i++){
            rowOfMatrix[i] = matrix[rowIndex][i];
        }
        return rowOfMatrix;
    }
    public int[][] removeRow(int[][] matrix, int rowIndex){
        int size = matrix.length;
        int[][] tempMatrix = new int[size-1][size];
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                if (i == rowIndex){
                    continue;
                }
                tempMatrix[i][j] = matrix[i][j];
            }
        }
        return tempMatrix;
    }
    public int[][] removeColumn(int[][] matrix, int colIndex){
        int size = matrix.length;
        int[][] tempMatrix = new int[size][size-1];
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                if (j == colIndex){
                    continue;
                }
                tempMatrix[i][j] = matrix[i][j];
            }
        }
        return tempMatrix;
    }

    public int[][] sparseMatrixCreate(int size){  
        int constraints = 4;
        int[][] matrice = new int[4][4];
        size = matrice.length;
        int[][] sparseMatrix = new int[size*size*size][constraints*size*size];
        int colIndex = 0;

        int boxSize = (int)Math.sqrt(size);
        for (int i = 0; i < size; i++){
            for (int j = 0; j<size; j++){
                for (int n = 0; n < size ; n++){

                    //cell constraint
                    sparseMatrix[colIndex][i*size + j] = 1;

                    //row constraint
                    sparseMatrix[colIndex][size*size+i*size+n] = 1;

                    //column constraint
                    sparseMatrix[colIndex][2*size*size + j*size + n] = 1;

                    //Box constraint
                    int box = (i/boxSize)*boxSize + (j/boxSize);
                    sparseMatrix[colIndex][3*size*size + box*size + n] = 1;

                    colIndex++;
                }
            }
        }
        return sparseMatrix;
    }
    public int[][] matrixCreate(SudokuBoard sudokuBoard){
        int size = sudokuBoard.getSize();
        int [][] grid = new int[size][size];
        for (int i = 0 ; i < 9 ; i++){
            for (int j = 0; j < 9 ; j++){
                grid[i][j] = sudokuBoard.getSingleField(i,j).getValue();
            }
        }
        return grid;
    }
}
