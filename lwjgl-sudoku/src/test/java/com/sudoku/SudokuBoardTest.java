package com.sudoku;
import org.junit.jupiter.api.Test;

import com.sudoku.model.Field;
import com.sudoku.model.SudokuBoard;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

public class SudokuBoardTest {
    /// We need to test
    // Populate, to see if it has removed the exact amount of fields to remove
    // Change field, to see if the field value gets changed
    // Check if the board size changes after operations to catch a error/issue or edgecase
    // Uniqueness Test
    // 


    private SudokuBoard board;
    private int boardSize = 9;
    private double difficulty = 0.5;

    @BeforeEach
    void makeBoard() {
        board = new SudokuBoard(boardSize);
    }
    
    @Test
    void populateCorrectDifficultyTest() {
        board.populate(difficulty);
        int amountToRemove = board.getFieldsToRemove(difficulty);
        int amountRemoved = 0;
        for (int i = 0 ; i < boardSize ; i++) {
            for (int j = 0 ; j < boardSize ; j++) {
                if (board.getSingleField(i,j).getValue() == 0) {
                    amountRemoved++;
                }
            }
        }
        assertEquals(amountRemoved,amountToRemove);
    }

    @Test
    void changeFieldTest() {
        int tempVal = board.getSingleField(0,0).getValue();
        board.changeField(0,0,1);
        assertEquals(tempVal,0);
        assertEquals(board.getSingleField(0,0).getValue(),1);
    }

    @Test
    void coherentBoardSizeTest() {
        Field[][] boardFieldsBefore = board.getWholeBoard();
        board.changeField(boardFieldsBefore.length-1,boardFieldsBefore[0].length-1,1);
        board.changeField(0,0,1);
        Field[][] boardFields = board.getWholeBoard();
        assertEquals(boardFields.length, boardSize);
        assertEquals(boardFields[0].length, boardSize);
    }
}