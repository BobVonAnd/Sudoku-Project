package com.sudoku;
import com.sudoku.view.TerminalView;


import org.junit.jupiter.api.Test;

import com.sudoku.model.SudokuBoard;
import com.sudoku.model.algoXSolver;
import com.sudoku.view.TerminalView;

import static org.junit.jupiter.api.Assertions.*;

public class AlgoXTest {
    private SudokuBoard sudokutestBoard = new SudokuBoard(9);
    private algoXSolver algoX = new algoXSolver();
    @Test
    void notUniqueSudokuGetsFalse(){
    int[][] nonUniqueBoard = new int[][]{
        {1,0,0, 0,0,0, 0,0,0},
        {0,0,0, 0,0,0, 0,0,0},
        {0,0,0, 0,0,0, 0,0,0},

        {0,0,0, 0,0,0, 0,0,0},
        {0,0,0, 0,0,0, 0,0,0},
        {0,0,0, 0,0,0, 0,0,0},

        {0,0,0, 0,0,0, 0,0,0},
        {0,0,0, 0,0,0, 0,0,0},
        {0,0,0, 0,0,0, 0,0,0}
    };
        sudokutestBoard.readIntoBoard(nonUniqueBoard);
        assertFalse(algoX.algoXIsUnique(sudokutestBoard));
    }
    @Test
    void UniqueSudokuGetsTrue(){
    int[][] UniqueBoard = new int[][]{
        {2,6,0, 0,0,8, 0,0,0},
        {0,8,0, 0,0,9, 6,0,0},
        {0,0,0, 0,5,0, 0,0,0},

        {0,9,4, 3,0,0, 5,0,0},
        {0,0,2, 0,7,0, 0,0,0},
        {0,5,0, 0,0,0, 8,0,4},

        {0,3,5, 8,0,0, 0,0,0},
        {0,0,0, 0,0,0, 3,0,1},
        {7,0,0, 0,6,0, 0,0,0}
    };
        sudokutestBoard.readIntoBoard(UniqueBoard);
        assertTrue(algoX.algoXIsUnique(sudokutestBoard));
    }

}
