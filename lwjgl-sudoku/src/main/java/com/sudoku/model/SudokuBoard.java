    package com.sudoku.model;

    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.Collections;
    import java.util.Random;
    import java.util.stream.IntStream;

import com.sudoku.view.TerminalView;

    public class SudokuBoard {

        private ArrayList<Field> fields = new ArrayList<>();
        private Field[][] wholeBoard;
        private int size;
        private int bigFieldSize;
        private double difficultyScale;
        private algoXSolver algoX = new algoXSolver();
        private int fieldsAmount = 0;

        private int nrOfFieldsLeft = 0;

        private int solutions; // for generating the sudoku

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

        
        public void clear() {
            for (int i = 0 ; i < wholeBoard.length ; i++) {
                for (int j = 0 ; j < wholeBoard.length ; j++) {
                    wholeBoard[i][j].setValue(0);
                }
            }
        }

    public void setDifficultyScale(double difficultyScale) {
        this.difficultyScale = difficultyScale;
    }
    
    public void solve() {
        double startTime = System.currentTimeMillis();
        //boolean unique = algoX.algoXIsUnique(this);
        double endTime = System.currentTimeMillis();
        double sudokuBoardStartTime = System.currentTimeMillis();
        //this.uniquenessTest();
		algoX.algoXManager(this);
        double sudokuBoardEndTIme = System.currentTimeMillis();
        double duration = endTime - startTime;
        double sudokuBoardDuration = sudokuBoardEndTIme - sudokuBoardStartTime;
        System.out.println("It took " + duration + " ms to check if it is unique with algox");
        System.out.println("It took " + sudokuBoardDuration + " ms to check if it is unique without algox");
        System.out.println("The sudoku is unique " + String.valueOf(this.solutions == 0));
    }

        public algoXSolver getAlgoX() {
            return algoX;
        }

        public void readIntoBoard(int[][] integerBoard) {
            for (int y = 0; y < this.size; y++) {
                for (int x = 0; x < this.size; x++) {
                    changeField(x, y, integerBoard[y][x]);
                }
            }
        }

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
        
        public void populate(double difficultyScale) {
            this.difficultyScale = difficultyScale;    
            populate();
        }

        public void populate() {
            this.clear();
            for (int i = 0; i < this.bigFieldSize; i+=2) {
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
            TerminalView before = new TerminalView(this);
            before.printBoard();
            algoX.algoXManager(this);

        TerminalView solved = new TerminalView(this);
        solved.printBoard();
        System.out.println("Solved Sudoku (Before removal)^^");
        ArrayList<Field> notRemoved = new ArrayList<>();
        for (int i = 0 ; i < size ; i++) {
            for (int j = 0 ; j < size ; j++) {
                notRemoved.add(wholeBoard[i][j]);
            }
        }
        int amountToRemove = getFieldsToRemove(this.difficultyScale);
        Random rand = new Random();
        int beforesize = notRemoved.size();
        
        while (notRemoved.size() > beforesize-amountToRemove) {
            Field f = notRemoved.get(rand.nextInt(notRemoved.size()));
            int x = f.getCoordinates()[0];
            int y = f.getCoordinates()[1];

            // temp removal of field
            int tempVal = wholeBoard[x][y].getValue();
            wholeBoard[x][y].setValue(0);
            
            

            boolean isUnique = algoX.algoXIsUnique(this);
            if (!isUnique) {
                wholeBoard[x][y].setValue(tempVal);
            } else if (isUnique) {
                notRemoved.remove(wholeBoard[x][y]);
                wholeBoard[x][y].setLocked(false);
                nrOfFieldsLeft += 1;
                System.out.println(String.valueOf(beforesize-notRemoved.size()));
            } else {
                wholeBoard[x][y].setValue(tempVal);
            }

            }
            System.out.println("Removing " + Integer.toString(amountToRemove) + " fields.");
            TerminalView after = new TerminalView(this);
            after.printBoard();
            System.out.println("Stopped initialising here");
        }


        public int getFieldsToRemove(double difficultyScale/* hard to easy aka 0 to 1 (decimal) */) {
            double scale = Math.min(1, Math.max(difficultyScale, 0));
            int totalCells = this.size * this.size;
            switch (this.size) {
                case 4:
                    return (int) (-7 * scale + 12); 

                case 9:
                    return (int) (-20 * scale + 56); 

                case 16:
                    return (int) (-82 * scale + 160); 

                case 25:
                    return (int) (-93 * scale + 311);

                case 36:
                    return (int) (-135 * scale + 580);

                default:
                    double fraction = 0.65 - 0.3 * scale;
                    return (int) (totalCells * fraction);
            }
        }

        public double getDifficultyScale() {
            return difficultyScale;
        }

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

    public Boolean uniquenessTest() {
        if (this.solutions > 1) {
            return false;
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

