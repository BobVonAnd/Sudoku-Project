package com.sudoku.model;

import java.util.ArrayList;

public class humanSolverBoardHelper {
    public void BoardHelper(SudokuBoard sudokuBoard){
        ArrayList<Field> fields = sudokuBoard.getFields();
        for (Field f : fields){
            updateLegalEntriesOfField(f, sudokuBoard);
            makeEdges(f, sudokuBoard);
        }
    }
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
    public boolean sameBoardCheck(SudokuBoard sudokuBoard1, SudokuBoard sudokuBoard2){
        int size1 = sudokuBoard1.getSize();
        int size2 = sudokuBoard2.getSize();
        if (size1 != size2){
            return false;
        }
        for (int x = 0; x < size1; x++){
            for (int y = 0; y < size1; y++){
                int value1 = sudokuBoard1.getSingleField(x,y).getValue();
                int value2 = sudokuBoard2.getSingleField(x, y).getValue();
                if (value1 != value2 && value1 != 0 && value2 != 0){
                    return false;
                }
            }
        }
        return true;
    }
}
