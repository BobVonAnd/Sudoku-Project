package com.sudoku;
import java.util.ArrayList;
//import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.sudoku.model.Field;
import com.sudoku.model.SudokuBoard;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

public class SudokuBoardTest {
    private SudokuBoard board;
    private int boardSize = 9;
    private double difficulty = 0.5;

    @BeforeEach
    void makeBoard() {
        board = new SudokuBoard(boardSize);
    }
    
    @Test
void updateLETest() {
    SudokuBoard board2 = new SudokuBoard(4);
    board2.readIntoBoard(new int[][] {
        {2,1,3,4},
        {3,0,1,2},
        {1,2,4,3},
        {4,3,2,1}
    });

    Field f = board2.getSingleField(1,1);
    board2.updateLegalEntriesOfField(f);

    System.out.println(f.getValue()); // 0
    ArrayList<Integer> LE = f.getLegalEntries();
    ArrayList<Integer> ManualLE = new ArrayList<>();
    ManualLE.add(4);
    assertEquals(ManualLE, LE);
}

    @Test
    void validityTest() {
        SudokuBoard board2 = new SudokuBoard(2);
        board2.readIntoBoard(new int[][] {{2,1},{3,0}});
        assertEquals(false,board2.isValid(1,1,1));
        assertEquals(true,board2.isValid(1,1,4));
    }

    @Test
    void uniquenessTest() {
        SudokuBoard board2 = new SudokuBoard(2);
        board2.readIntoBoard(new int[][] {{2,1},{3,0}});
        board2.uniquenessTest();
        int before = board2.getSolutions();
        board2.setSolutions(0);
        board2.uniquenessTest();
        assertEquals(before,board2.getSolutions());
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
        assertEquals(amountToRemove, amountRemoved);
        // assertEquals(1,1);
    }

    @Test
    void changeFieldTest() {
        int tempVal = board.getSingleField(0,0).getValue();
        board.changeField(0,0,1);
        assertEquals(0,tempVal);
        assertEquals(1,board.getSingleField(0,0).getValue());
    }

    @Test
    void coherentBoardSizeTest() {
        Field[][] boardFieldsBefore = board.getWholeBoard();
        board.changeField(boardFieldsBefore.length-1,boardFieldsBefore[0].length-1,1);
        board.changeField(0,0,1);
        Field[][] boardFields = board.getWholeBoard();
        assertEquals(boardSize, boardFields.length);
        assertEquals(boardSize, boardFields[0].length);
    }
}