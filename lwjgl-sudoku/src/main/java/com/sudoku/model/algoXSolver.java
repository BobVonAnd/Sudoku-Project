package com.sudoku.model;

public class algoXSolver {

    public void algoXManager(SudokuBoard sudokuBoard){
        int[][] matrix = matrixCreate(sudokuBoard);
        int[][] sparseMatrix = sparseMatrixCreate(4);

    }
    public int[][] sparseMatrixCreate(int size){  
        int constraints = 4;
        int[][] matrice = new int[4][4];
        size = matrice.length;
        int[][] sparseMatrix = new int[size*size*size][constraints*size*size];
        int rowIndex = 0;

        int boxSize = (int)Math.sqrt(size);
        for (int i = 0; i < size; i++){
            for (int j = 0; j<size; j++){
                for (int n = 0; n < size ; n++){

                    //cell constraint
                    sparseMatrix[rowIndex][i*size + j] = 1;

                    //row constraint
                    sparseMatrix[rowIndex][size*size+i*size+n] = 1;

                    //column constraint
                    sparseMatrix[rowIndex][2*size*size + j*size + n] = 1;

                    //Box constraint
                    int box = (i/boxSize)*boxSize + (j/boxSize);
                    sparseMatrix[rowIndex][3*size*size + box*size + n] = 1;

                    rowIndex++;
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
