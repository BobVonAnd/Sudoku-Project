package com.sudoku.model;

public class SudokuBoard {
    
    private Field[][] wholeBoard;

    public SudokuBoard(int size){
        wholeBoard = new Field[size][size];
        
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                wholeBoard[j][i] = new Field(j,i, 0, size);
            }
        }
    }

}
