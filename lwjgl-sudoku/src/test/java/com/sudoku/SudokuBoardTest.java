package com.sudoku;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sudoku.model.Field;
import com.sudoku.model.SudokuBoard;

public class SudokuBoardTest {
    private SudokuBoard board;
    private int boardSize = 9;
    private double difficulty = 0.5;

    @BeforeEach
    void makeBoard() {
        board = new SudokuBoard(boardSize);
    }
    

    @Test
    public void validityTest() {
        SudokuBoard board2 = new SudokuBoard(2);
        board2.readIntoBoard(new int[][] {{2,1},{3,0}});
        assertEquals(false,board2.isValid(1,1,1));
        assertEquals(true,board2.isValid(1,1,4));
    }

    // @Test
    // void uniquenessTest() {
    //     SudokuBoard board2 = new SudokuBoard(4);
    //     board2.readIntoBoard(
    //         new int[][] { 
    //         {2,0,0,0},
    //         {0,0,4,0},
    //         {0,2,0,0},
    //         {0,0,0,1}});
    //     board2.uniquenessTest();
    //     int before = board2.getSolutions();
    //     board2.setSolutions(0);
    //     board2.uniquenessTest();
    //     assertEquals(before,board2.getSolutions());
    //     assertEquals(1,board2.getSolutions());
    // }
   
    // @Test
    // void populateCorrectDifficultyTest() {
    //     //board.populate(difficulty);
    //     int amountToRemove = board.getFieldsToRemove(difficulty);
    //     int amountRemoved = 0;
    //     for (int i = 0 ; i < boardSize ; i++) {
    //         for (int j = 0 ; j < boardSize ; j++) {
    //             if (board.getSingleField(i,j).getValue() == 0) {
    //                 amountRemoved++;
    //             }
    //         }
    //     }
    //     assertEquals(amountToRemove, amountRemoved);
    //     // assertEquals(1,1);
    // }

    @Test
    public void changeFieldTest() {
        int tempVal = board.getSingleField(0,0).getValue();
        board.changeField(0,0,1);
        assertEquals(0,tempVal);
        assertEquals(1,board.getSingleField(0,0).getValue());
    }

    @Test
    public void coherentBoardSizeTest() {
        Field[][] boardFieldsBefore = board.getWholeBoard();
        board.changeField(boardFieldsBefore.length-1,boardFieldsBefore[0].length-1,1);
        board.changeField(0,0,1);
        Field[][] boardFields = board.getWholeBoard();
        assertEquals(boardSize, boardFields.length);
        assertEquals(boardSize, boardFields[0].length);
    }

    @Test
    public void nrOfFieldsLeftTest(){
        double scale = 0.5;
        SudokuBoard sb = new SudokuBoard(4);
        assertTrue(sb.getNrOfFieldsLeft() == 0);
        sb.populate(scale);
        assertTrue(sb.getNrOfFieldsLeft() == (int) (-7 * scale + 12));


    }

    @Test
    public void fieldsToRemoveTest(){
        double dif = 0.5;
        SudokuBoard sb = new SudokuBoard(4);
        sb.populate(dif);

        assertTrue(sb.getNrOfFieldsLeft() == sb.getFieldsToRemove(dif));
        sb.clear();

        sb = new SudokuBoard(9);
        sb.populate(dif);
        assertTrue(sb.getNrOfFieldsLeft() == sb.getFieldsToRemove(dif));
        sb.clear();

        sb = new SudokuBoard(16);
        sb.populate(dif);
        assertTrue(sb.getNrOfFieldsLeft() == sb.getFieldsToRemove(dif));
        sb.clear();

        sb = new SudokuBoard(25);
        sb.populate(dif);
        assertTrue(sb.getNrOfFieldsLeft() == sb.getFieldsToRemove(dif));
        sb.clear();

        sb = new SudokuBoard(36);
        sb.populate(dif);
        assertTrue(sb.getNrOfFieldsLeft() == sb.getFieldsToRemove(dif));
        sb.clear();

         sb = new SudokuBoard(1);
        sb.populate(dif);
        assertTrue(sb.getNrOfFieldsLeft() == sb.getFieldsToRemove(dif));
        sb.clear();
         
    }

    @Test
    public void clearTest(){
        boolean fail= true;
        SudokuBoard sb = new SudokuBoard(9);
        sb.populate(0.5);
        for(int i = 0; i < sb.getSize(); i++){
            for(int j = 0; j < sb.getSize(); j++){
                if(sb.getSingleField(i, j).getValue() != 0){
                    fail = false;
                }
            }   
        }
        if(fail){
            assertTrue(false);
        }
        sb.clear();
        

        for(int i = 0; i < sb.getSize(); i++){
            for(int j = 0; j < sb.getSize(); j++){
                if(sb.getSingleField(i, j).getValue() != 0){
                     assertTrue(false);
                }
            }   
        }
         assertTrue(true);
    }

    @Test
    public void basePopTest(){
        boolean fail= true;
        SudokuBoard sb = new SudokuBoard(9);

        for(int i = 0; i < sb.getSize(); i++){
            for(int j = 0; j < sb.getSize(); j++){
                if(sb.getSingleField(i, j).getValue() != 0){
                     assertTrue(false);
                }
            }   
        }

        
        sb.populatebase(0.5);
        for(int i = 0; i < sb.getSize(); i++){
            for(int j = 0; j < sb.getSize(); j++){
                if(sb.getSingleField(i, j).getValue() != 0){
                    fail = false;
                }
            }   
        }
        if(fail){
            assertTrue(false);
        }
    
         assertTrue(true);
    }

    @Test
    public void solverTest(){
        SudokuBoard board1 = new SudokuBoard(4);
        board1.readIntoBoard(new int[][] {
        {1,2,0,4},
        {0,3,0,0},
        {0,0,4,0},
        {0,4,0,1}
        });

        SudokuBoard board2 = new SudokuBoard(4);
        board2.readIntoBoard(new int[][] {
        {1,2,3,4},
        {4,3,1,2},
        {2,1,4,3},
        {3,4,2,1}
        });
        board1.solve();
        for(int i = 0; i < board2.getSize(); i++){
            for(int j = 0; j < board2.getSize(); j++){
                if(board2.getSingleField(i, j).getValue() != board1.getSingleField(i, j).getValue()){
                    assertTrue(false);
                }
            }   
        }
        assertTrue(true);


    }


    @Test void unicnessTest(){
        SudokuBoard board1 = new SudokuBoard(4);
        board1.readIntoBoard(new int[][] {
        {1,2,0,4},
        {0,3,0,0},
        {0,0,4,0},
        {0,4,0,1}
        });

        board1.uniquenessTest();

        assertTrue(board1.getSolutions() == 1);
    }


    @Test
    public void getDifficultyTest(){
        SudokuBoard sb1 = new SudokuBoard(4);
        sb1.populate(0.2);
        assertTrue(sb1.getDifficultyString().equals("Impossible"));

        SudokuBoard sb2 = new SudokuBoard(4);
        sb2.populate(0.3);
        assertTrue(sb2.getDifficultyString().equals("Hard"));

        SudokuBoard sb3 = new SudokuBoard(4);
        sb3.populate(0.6);
        assertTrue(sb3.getDifficultyString().equals("Medium"));

        SudokuBoard sb4 = new SudokuBoard(4);
        sb4.populate(1);
        System.out.println(sb4.getDifficultyString());
        assertTrue(sb4.getDifficultyString().equals("Easy"));
    }


    @Test
    public void nrOfFieldsLeftGetSetTest(){
        SudokuBoard sb = new SudokuBoard(4);
        assertTrue(sb.getNrOfFieldsLeft() == 0);
        sb.inputRemoved();
        assertTrue(sb.getNrOfFieldsLeft() == 1);
        sb.inputDetected();
        assertTrue(sb.getNrOfFieldsLeft() == 0);
    }


    @Test
    public void sbCopyTest(){
        SudokuBoard sb = new SudokuBoard(4);
        sb.populate();
        SudokuBoard sb1 = new SudokuBoard(4);
        sb1 = sb.getCopy();
         for(int i = 0; i < sb.getSize(); i++){
            for(int j = 0; j < sb.getSize(); j++){
                if(sb.getSingleField(i, j).getValue() != sb1.getSingleField(i, j).getValue()){
                    assertTrue(false);
                }
            }   
        }
        assertTrue(true);
    }


    private void assertFalse(Integer value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}