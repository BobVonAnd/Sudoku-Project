package com.sudoku.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.stream.IntStream;

import com.sudoku.view.TerminalView;

public class SudokuBoard {

    private Field[][] wholeBoard;
    private int size;
    private int bigFieldSize;
    private double difficultyScale;
    private ArrayList<Field> emptyCells = new ArrayList<Field>(); // <-- optimization: emptycells instead of triple for loop

    private int solutions; // for generating the sudoku

    public SudokuBoard(int size) {
        wholeBoard = new Field[size][size];
        this.size = size;
        this.bigFieldSize = (int) Math.sqrt(size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                wholeBoard[i][j] = new Field(i,j, 0, size);
                emptyCells.add(wholeBoard[i][j]);
            }
        }
    }
    
    public void solve() {
        algoXSolver algoX = new algoXSolver(); 
		algoX.algoXManager(this);
    }

    public void clear() {
        for (int i = 0 ; i < wholeBoard.length ; i++) {
            for (int j = 0 ; j < wholeBoard.length ; j++) {
                wholeBoard[i][j].setValue(0);
            }
        }
    }

    public void readIntoBoard(int[][] integerBoard) {
        for (int y = 0; y < this.size; y++) {
            for (int x = 0; x < this.size; x++) {
                changeField(x, y, integerBoard[y][x]);
            }
        }
    }

    public void populate(double difficultyScale) {
        double cumulatedTime = 0;
        int tests = 100;
        for (int p = 0 ; p < tests ; p++) {
            this.clear();
            double startTime = System.nanoTime();
            this.difficultyScale = difficultyScale;
            do {
                 for (int i = 0; i < this.bigFieldSize; i++) {
                    // Get choices
                    ArrayList<Integer> choices = new ArrayList<>(
                            Arrays.asList(IntStream.rangeClosed(1, this.size).boxed().toArray(Integer[]::new)));
                    Collections.shuffle(choices);
                    int counter = 0;
                    // Insert Field
                    for (int j = i * this.bigFieldSize; j < this.bigFieldSize + i * this.bigFieldSize; j++) {
                        for (int k = i * this.bigFieldSize; k < this.bigFieldSize + i * this.bigFieldSize; k++) {
                            changeField(k, j, choices.get(counter));
                            counter++;
                        }
                    }
                }
                // if size <= 4 test for uniqueness to see if its solvable, if its not we try again.
                if (size <= 4) {
                    setSolutions(0);
                    uniquenessTest();
                } 
            } while (solutions != 1 && size <= 4);
            TerminalView before = new TerminalView(this);
            before.printBoard();
            algoXSolver algoX = new algoXSolver(); 
            int amountToRemove = getFieldsToRemove(this.difficultyScale);
            algoX.algoXManager(this);
            setSolutions(0);
            uniquenessTest();
            TerminalView solved = new TerminalView(this);
            solved.printBoard();
            System.out.println("Solutions: " + solutions);
            System.out.println("Solved Sudoku (Before removal)^^");
            
            Random rand = new Random();
            int removed = 0;
            int attempts = 0;
            
            while (removed < amountToRemove) {
                attempts++;
                int x = rand.nextInt(this.size);
                int y = rand.nextInt(this.size);

                // if chosen value is 0, try again (brute force, this is temp)
                if (wholeBoard[x][y].getValue() == 0) {
                    continue;
                }

                // temp removal of field
                int tempVal = wholeBoard[x][y].getValue();
                wholeBoard[x][y].setValue(0);
                wholeBoard[size-x-1][size-y-1].setValue(0);
                this.solutions = 0;
                uniquenessTest();
                if (this.solutions > 1) {
                    wholeBoard[x][y].setValue(tempVal);
                    wholeBoard[size-x-1][size-y-1].setValue(tempVal);
                } else if (this.solutions == 1) {
                    removed+=2;
                } else {
                    wholeBoard[x][y].setValue(tempVal);
                    wholeBoard[size-x-1][size-y-1].setValue(tempVal);
                }
            }
            System.out.println("Removing " + Integer.toString(amountToRemove) + " fields.");
            TerminalView after = new TerminalView(this);
            after.printBoard();
            System.out.println("Stopped initialising here");
            double endTime = System.nanoTime();
            double durationOfPopulate = (endTime - startTime)/1000000;
            cumulatedTime += durationOfPopulate;
            System.out.printf("Took %.2f ms to populate.%n", durationOfPopulate);
            System.out.println("");
        }
        System.out.printf("Took %.2f ms to test populate.%n", cumulatedTime);
        System.out.printf("Took on avg %.2f ms to populate each.%n", cumulatedTime/tests);       
    }


    public int getFieldsToRemove(double difficultyScale/* hard to easy aka 0 to 1 (decimal) */) {
        double scale = Math.min(1, Math.max(difficultyScale, 0));
        int totalCells = this.size * this.size;
        switch (this.size) {
            case 4:
                return (int) (-4*scale+7);
            case 9:
                return (int) (-28*scale+64);
            default:
                double fraction = 0.55 - 0.2 * scale;
                return (int) (totalCells * fraction);
        }
    }


    public Boolean uniquenessTest() {
        if (this.solutions > 1) {
            return false;
        }
        
        for (int i = 0; i < emptyCells.size(); i++) {
            if (emptyCells.get(i).getValue() == 0) {
                ArrayList<Integer> candidates = emptyCells.get(i).getLegalEntries();
                for (int k = 0; k < candidates.size(); k++) { // <-- optimization: only valid candidates
                    int[] fieldPos = emptyCells.get(i).getCoordinates();
                    changeField(fieldPos[0], fieldPos[1], candidates.get(k));
                    if (uniquenessTest()) { 
                        changeField(fieldPos[0], fieldPos[1], 0);
                        return true;
                    }
                    changeField(fieldPos[0], fieldPos[1], 0);
                }
                return false;
            }
        }
        this.solutions++;
        return false;
    }

    public String getDifficultyString() {
        int amountToRemove = getFieldsToRemove(this.difficultyScale);
        if (amountToRemove >= getFieldsToRemove(0.25)) {
            return "Impossible";
        } else if (amountToRemove >= getFieldsToRemove(0.5)) {
            return "Hard";
        } else if (amountToRemove >= getFieldsToRemove(0.75)) {
            return "Medium";
        } else {
            return "Easy";
        }
    }

    public void changeField(int x, int y, int value) {
        // changes a value of a field and therefore updates other legal entries
        Field field = wholeBoard[x][y];
        field.setValue(value);
        if (value != 0) {
            field.clearLe();
            emptyCells.remove(field);
        } else if (!emptyCells.contains(field)) {
            emptyCells.add(field);
        }
        field.removeValueFromLegalEntriesOfNeighbours();
        //System.out.println("Inserted " + value + " at (" + x + "," + y + ")");
    }

    public void updateLegalEntriesOfField(Field field) {
        int x_coordinate = field.getCoordinates()[0];
        int y_coordinate = field.getCoordinates()[1];

        int cornerX = x_coordinate - field.getPosition()[0];
        int cornerY = y_coordinate - field.getPosition()[1];

        for (Field fields : wholeBoard[x_coordinate]) {// Removes legal entry from itself rn
            if (field != fields) {
                field.removeLE(fields.getValue());
            }

        }
        for (int i = 0; i < this.size; i++) {
            if (field != wholeBoard[i][y_coordinate]) {
                field.removeLE(wholeBoard[i][y_coordinate].getValue());
            }

        }

        for (int j = 0; j < this.bigFieldSize; j++) {
            for (int k = 0; k < this.bigFieldSize; k++) {
                Field f = wholeBoard[cornerX + j][cornerY + k];
                if (field != f) {
                    field.removeLE(f.getValue());
                }
            }
        }

    }

    public void makeEdges(Field field) {
        int x_coordinate = field.getCoordinates()[0];
        int y_coordinate = field.getCoordinates()[1];

        int cornerX = x_coordinate - field.getPosition()[0];
        int cornerY = y_coordinate - field.getPosition()[1];

        for (Field fields : wholeBoard[x_coordinate]) {// Removes legal entry from itself rn
            if (fields.getValue() == 0 && field != fields && field.notcontainsEdge(fields)) {
                field.addEdge(fields);
            }

        }
        for (int i = 0; i < this.size; i++) {
            if (wholeBoard[i][y_coordinate].getValue() == 0 && field != wholeBoard[i][y_coordinate]
                    && field.notcontainsEdge(wholeBoard[i][y_coordinate])) {
                field.addEdge(wholeBoard[i][y_coordinate]);
            }

        }

        for (int j = 0; j < this.bigFieldSize; j++) {
            for (int k = 0; k < this.bigFieldSize; k++) {
                Field f = wholeBoard[cornerX + j][cornerY + k];
                if (f.getValue() == 0 && field.notcontainsEdge(f)) {
                    field.addEdge(f);
                }

            }
        }

    }

    public Field[][] getWholeBoard() {
        return this.wholeBoard;
    }

    public int getSize() {
        return this.size;
    }

    public int getSolutions() {
        return this.solutions;
    }

    public void setSolutions(int sol) {
        this.solutions = sol;
    }

    public Field getSingleField(int x, int y) {
        Field f = this.wholeBoard[x][y];
        return f;
    }

}

