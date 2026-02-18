package com.sudoku.model;

public class SudokuBoard {
    
    private BigField[][] wholeBoard;

    public SudokuBoard(int size){
        wholeBoard = new BigField[size][size];
        
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                wholeBoard[j][i] = new BigField(size);
            }
        }
    }

}
