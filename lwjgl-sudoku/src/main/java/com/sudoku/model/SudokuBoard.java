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

    public void populate() {
        // preset board
        changeField(0, 0, 1);
        changeField(3, 0, 4);
        changeField(7, 0, 9);
        changeField(6, 1, 8);
        changeField(6, 2, 1);
        changeField(2, 2, 5);
        changeField(3, 2, 3);
        changeField(4, 2, 2);
        changeField(6, 2, 1);
        changeField(6, 3, 5);
        changeField(5, 3, 4);
        changeField(3, 3, 9);
        changeField(1, 3, 8);
        changeField(1, 4, 1);
        changeField(2, 4, 4);
        changeField(3, 4, 8);
        changeField(6, 4, 2);
        changeField(3, 5, 2);
        changeField(5, 5, 7);
        changeField(1, 6, 2);
        changeField(4, 6, 6);
        changeField(8, 6, 9);
        changeField(8, 7, 8);
        changeField(5, 7, 2);
        changeField(2, 7, 3);
        changeField(0, 7, 6);
        changeField(1, 8, 7);
        changeField(8, 8, 3);
    }

    public void changeField(int x, int y, int value) {
        // changes a value of a field and therefore updates other legal entries
        Field field = wholeBoard[x][y];
        field.setValue(value);
        
    }

}
