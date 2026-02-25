package com.sudoku.model;

public class Solver {
    private boolean solved = false;
    public void solves(SudokuBoard sudokuboard) {
    
    while (!solved) {
        for (int y = 0; y < sudokuboard.getSize(); y++) {
            for (int x = 0; x < sudokuboard.getSize(); x++) {

                Field f = sudokuboard.getSingleField(x, y);
                if (f.getValue() == 0 && f.getLeSize() == 1) {
                    sudokuboard.changeField(
                        f.getCoordinates()[0],
                        f.getCoordinates()[1],
                        f.getLegalEntries().get(0)
                    );
                }
            }
        }

        solved = true; // or add real termination condition
    }
}


}
