    package com.sudoku.model;

    import java.util.ArrayList;
    import java.util.Random;

import com.sudoku.view.TerminalView;


    public class SudokuBoard {

        private ArrayList<Field> fields = new ArrayList<>();
        private Field[][] wholeBoard;
        private int size;
        private int bigFieldSize;
        private double difficultyScale = 1;
        private algoXSolver algoX = new algoXSolver();

        private int nrOfFieldsLeft = 0;

        private int solutions = 0; // for generating the sudoku
        //The sudokuBoard class is initialized and creates size*size fields and saves them in a field[][]
        public SudokuBoard(int size) {
            wholeBoard = new Field[size][size];
            this.size = size;
            this.bigFieldSize = (int) Math.sqrt(size);
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    wholeBoard[i][j] = new Field(i,j, 0, size, new double[]{0,0,0});
                    fields.add(wholeBoard[i][j]);
                }
            }
        }

        //Get the solutions
        public int getSolutions(){
            return solutions;
        }

        public void inputRemoved(){
            nrOfFieldsLeft += 1;
        }

        public void inputDetected(){
            nrOfFieldsLeft -= 1;
        }

        public int getNrOfFieldsLeft(){
            return nrOfFieldsLeft;
        }
        

        public void setWholeBoard(Field[][] wholeBoard){
            this.wholeBoard = wholeBoard;
        }

        //This sets all fields values to 0
        public void clear(){
            for (int i = 0 ; i < wholeBoard.length ; i++) {
                for (int j = 0 ; j < wholeBoard.length ; j++) {
                    wholeBoard[i][j].setValue(0);
                }
            }
            nrOfFieldsLeft = 0;
        }

    public void setDifficultyScale(double difficultyScale) {
        this.difficultyScale = difficultyScale;
    }
    //This solves the sudokuBoard using algorithm X
    public void solve() {
		algoX.algoXManager(this);
    }

    public algoXSolver getAlgoX() {
        return algoX;
    }
    //Reads an integerboard into the sudokuBoard
    public void readIntoBoard(int[][] integerBoard) {
        for (int y = 0; y < this.size; y++) {
            for (int x = 0; x < this.size; x++) {
                changeField(x, y, integerBoard[y][x]);
                if(integerBoard[y][x]== 0){
                    getSingleField(x, y).setLocked(false);
                    nrOfFieldsLeft += 1;
                }
            }
        }
    }
    //Reads the sudoku board into and integerboard
    public static int[][] readOutOffBoard(SudokuBoard sudokuBoard){
        int size = sudokuBoard.getSize();
        int[][] integerBoard = new int[size][size];
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                integerBoard[j][i] = sudokuBoard.getSingleField(i, j).getValue();
            }
        }
        return integerBoard;
    }
    //Populate function with difficulty scale
    public void populate(double difficultyScale) {
        this.difficultyScale = difficultyScale;    
        populate();
    }
    //Populates using algorithm X
    public void populate() {
        this.clear();
        int fieldsToRemove = getFieldsToRemove(difficultyScale);
        SudokuBoard sudokuBoard = algoX.algoXCreateUnique(this.size, fieldsToRemove);
        int[][] integerBoard = SudokuBoard.readOutOffBoard(sudokuBoard);
        this.readIntoBoard(integerBoard);
    }
    //This method makes a filled sudoku and tries to generate a puzzle with amountToRemove removed fields based on that sudoku
    private void populatebasepop(int amountToRemove){
        this.clear();
        //Puts in a random number and solves it using algorithm X
        Random rand = new Random(System.currentTimeMillis());
        changeField(rand.nextInt(this.size), rand.nextInt(this.size), rand.nextInt(this.size));
        algoX.algoXManager(this);
        ArrayList<Field> notRemoved = new ArrayList<>();
        //Adds all fields to an arrayList
        for (int i = 0 ; i < size ; i++) {
            for (int j = 0 ; j < size ; j++) {
                notRemoved.add(wholeBoard[i][j]);
            }
        }
        //Removes values and does a uniqueness test after each removal
        while (notRemoved.size() > 0 && nrOfFieldsLeft < amountToRemove) {
            Field f = notRemoved.get(rand.nextInt(notRemoved.size()));
            int x = f.getCoordinates()[0];
            int y = f.getCoordinates()[1];
                //temp removal of field
            int tempVal = wholeBoard[x][y].getValue();
            wholeBoard[x][y].setValue(0);
            notRemoved.remove(wholeBoard[x][y]);
            this.solutions = 0;
            uniquenessTest();
            if (this.solutions == 1) {
                wholeBoard[x][y].setLocked(false);
                nrOfFieldsLeft += 1;
            } else {
                wholeBoard[x][y].setValue(tempVal);
            }   

        }
    }
    //This method is called by the other classes in the solver. It get's the amount of fields to remove based on the difficulty scale
    //And prints the board to the terminal
    public void populatebase(){
        int amountToRemove = getFieldsToRemove(this.difficultyScale);
        this.clear();
        while (nrOfFieldsLeft < amountToRemove){
            populatebasepop(amountToRemove);
        }
    }
    //Sets difficulty scale
    public void populatebase(double difficultyScale) {
        this.difficultyScale = difficultyScale;    
        populatebase();
    }
    //Returns the amount of fields to remove as an int based on the difficulty scale
    public int getFieldsToRemove(double difficultyScale/* hard to easy aka 0 to 1 (decimal) */) {
        double scale = Math.min(1, Math.max(difficultyScale, 0));
        int totalCells = this.size * this.size;
        switch (this.size) {
            case 4:
                return (int) (-7 * scale + 12); 

            case 9:
                return (int) (-20 * scale + 60); 

            case 16:
                return (int) (-82 * scale + 163); 

            case 25:
                return (int) (-93 * scale + 311);

            case 36:
                return (int) (-135 * scale + 400);

            default:
                double fraction = 0.65 - 0.3 * scale;
                return (int) (totalCells * fraction);
        }
    }

    public double getDifficultyScale() {
        return difficultyScale;
    }
    //This method returns false if the sudoku is invalid based on if another field in one of it's constraints has the same value
    public Boolean isValid(int row, int col, int num) {
        // Check row
        for (int x = 0; x < this.size; x++) {
            if (wholeBoard[row][x].getValue() == num) {
                return false;
            }
        }
        // Check col
        for (int x = 0; x < this.size; x++) {
            if (this.wholeBoard[x][col].getValue() == num) {
                return false;
            }
        }
        // Check bigfield
        int startRow = row - row % this.bigFieldSize;
        int startCol = col - col % this.bigFieldSize;
        for (int i = 0; i < this.bigFieldSize; i++) {
            for (int j = 0; j < this.bigFieldSize; j++) {
                if (this.wholeBoard[startRow + i][startCol + j].getValue() == num) {
                    return false;
                }
            }
        }
        return true;
    }
    //A uniqueness test that recursively checks if the sudoku is unique by checking if each field of value 0 can hold another value
    public Boolean uniquenessTest() {
        if (this.solutions > 1) {
            return true;
        }
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (wholeBoard[i][j].getValue() == 0) {
                    for (int k = 1; k <= this.size; k++) {
                        if (isValid(i, j, k)) {
                            changeField(i, j, k);
                            uniquenessTest();
                            changeField(i, j, 0);
                        }
                    }
                    return false;
                }
            }
        }
        this.solutions++;
        return false;
    }   
    //This sets the name of the difficulty based on where the slider has set the difficulty scale
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
        // changes a value of a field and therefore updates other legal entries and loses all it's own legal entries
        Field field = wholeBoard[x][y];
        field.setValue(value);
        if (value != 0) {
            field.clearLe();
        }
        field.removeValueFromLegalEntriesOfNeighbours();
    }

    public Field[][] getWholeBoard() {
        return this.wholeBoard;
    }

    //Creates a copy of the sudoku board
    public SudokuBoard getCopy(){
        ArrayList<Field> fields = this.getFields();
        SudokuBoard copyBoard = new SudokuBoard(this.size);
        for (Field field : fields){
            Integer value = field.getValue();
            if (value == 0){
                continue;
            }
            int[] coords = field.getCoordinates();
            copyBoard.getSingleField(coords[0], coords[1]).setValue(value);
        }
        return copyBoard;
    }
    public int getSize() {
        return this.size;
    }

    public void setSolutions(int sol) {
        this.solutions = sol;
    }

    public Field getSingleField(int x, int y) {
        return wholeBoard[x][y];
    }
    public ArrayList<Field> getFields(){
        return fields;
    }

}

