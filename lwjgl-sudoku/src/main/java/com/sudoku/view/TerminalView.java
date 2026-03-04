package com.sudoku.view;



import com.sudoku.model.Field;
import com.sudoku.model.SudokuBoard;

public class TerminalView {
    
    private Field[][] sudokuBoard;

    public TerminalView(SudokuBoard sudokuBoard){
        this.sudokuBoard = sudokuBoard.getWholeBoard();
    }

    public void printBoard(){
        for (int i = 0; i<sudokuBoard.length;i++){
            if(i%3 == 0){
                System.out.println();
            }
            for (int j = 0; j<sudokuBoard.length;j++) {
                if(j%3 == 0){
                    System.out.print(" ");
                }
                System.out.print("[" + sudokuBoard[j][i].getValue() + "]");
            }
            System.out.println();
        }
    }
}
