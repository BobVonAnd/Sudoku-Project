package com.sudoku.model;

import java.util.ArrayList;
//This class helps the solver class by doing the setup that enables the human solver
public class humanSolverBoardHelper {
    public void BoardHelper(SudokuBoard sudokuBoard){
        ArrayList<Field> fields = sudokuBoard.getFields();
        for (Field f : fields){
            updateLegalEntriesOfField(f, sudokuBoard);
            makeEdges(f, sudokuBoard);
        }
    }  
    //This method removes the legal entries of empty fields based on the filled fields
    public void updateLegalEntriesOfField(Field field, SudokuBoard sudokuBoard) {
        Field[][] wholeBoard = sudokuBoard.getWholeBoard();
        int bigFieldSize = (int) Math.sqrt(sudokuBoard.getSize());
        int x_coordinate = field.getCoordinates()[0];
        int y_coordinate = field.getCoordinates()[1];

        int cornerX = x_coordinate - field.getPosition()[0];
        int cornerY = y_coordinate - field.getPosition()[1];

        for (Field fields : wholeBoard[x_coordinate]) {
            if (field != fields) {
                field.removeLE(fields.getValue());
            }

        }
        for (int i = 0; i < sudokuBoard.getSize(); i++) {
            if (field != wholeBoard[i][y_coordinate]) {
                field.removeLE(wholeBoard[i][y_coordinate].getValue());
            }

        }

        for (int j = 0; j < bigFieldSize; j++) {
            for (int k = 0; k < bigFieldSize; k++) {
                Field f = wholeBoard[cornerX + j][cornerY + k];
                if (field != f) {
                    field.removeLE(f.getValue());
                }
            }
        }

    }
    //This method makes edges between the fields that are empty and are within the same constraint
    public void makeEdges(Field field, SudokuBoard sudokuBoard) {
        int size = sudokuBoard.getSize();
        Field[][] wholeBoard = sudokuBoard.getWholeBoard();
        int bigFieldSize = (int) Math.sqrt(sudokuBoard.getSize());
        int x_coordinate = field.getCoordinates()[0];
        int y_coordinate = field.getCoordinates()[1];

        int cornerX = x_coordinate - field.getPosition()[0];
        int cornerY = y_coordinate - field.getPosition()[1];

        for (Field f : wholeBoard[x_coordinate]) {
            if (f.getValue() == 0 && field != f && field.notcontainsEdge(f)) {
                field.addEdge(f);
            }

        }
        for (int i = 0; i < size; i++) {
            Field f = wholeBoard[i][y_coordinate];
            if (f.getValue() == 0 && field != f && field.notcontainsEdge(f)) {
                field.addEdge(f);
            }

        }

        for (int j = 0; j < bigFieldSize; j++) {
            for (int k = 0; k < bigFieldSize; k++) {
                Field f = wholeBoard[cornerX + j][cornerY + k];
                if (f.getValue() == 0 && field.notcontainsEdge(f) && field != f) {
                    field.addEdge(f);
                }

            }
        }

    }
}
