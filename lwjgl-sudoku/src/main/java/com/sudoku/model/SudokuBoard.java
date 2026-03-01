package com.sudoku.model;

public class SudokuBoard {
    
    private Field[][] wholeBoard;
    private int size = 9;

    public SudokuBoard(int size){
        wholeBoard = new Field[size][size];
        this.size = size;
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                wholeBoard[j][i] = new Field(j,i, 0, size);
            }
        }
    }

    public void populate() {
        // preset board
        int[][] board = {
    {0,0,4,0,0,2,5,0,0},
    {0,0,0,0,3,0,0,0,0},
    {0,0,3,0,0,9,0,7,2},

    {0,0,5,0,0,6,0,0,1},
    {8,0,0,1,0,0,4,6,9},
    {1,9,0,0,0,4,0,0,5},

    {0,7,0,4,0,0,0,0,0},
    {0,0,0,7,6,0,0,0,0},
    {0,6,0,0,2,0,3,4,0}
};

for (int y = 0; y < 9; y++) {
    for (int x = 0; x < 9; x++) {
        changeField(x, y, board[y][x]);
    }
}


        System.out.println("Stopped initialising here");
        System.out.println("");
    }

    public void changeField(int x, int y, int value) {
        // changes a value of a field and therefore updates other legal entries
        Field field = wholeBoard[x][y];
        field.setValue(value);
        if (value!=0){
            field.clearLe();
        }
        field.removeValueFromLegalEntriesOfNeighbours();
        System.out.println("Inserted " + value + " at (" + x + "," + y + ")");
    }

public void updateLegalEntriesOfField(Field field){
        int x_coordinate = field.getCoordinates()[0];
        int y_coordinate = field.getCoordinates()[1];

        int cornerX = x_coordinate-field.getPosition()[0];
        int cornerY = y_coordinate-field.getPosition()[1];

        for (Field fields : wholeBoard[x_coordinate]){//Removes legal entry from itself rn
            if (field != fields ){
                field.removeLE(fields.getValue());
            }
            
        }
        for (int i = 0; i<this.size; i++){
            if (field != wholeBoard[i][y_coordinate]){
                field.removeLE(wholeBoard[i][y_coordinate].getValue());
            }
            
        }

        for (int j = 0; j<3 ; j++){
            for (int k = 0; k<3; k++){
                Field f = wholeBoard[cornerX+j][cornerY+k];
                if (field != f ){
                    field.removeLE(f.getValue());
                }
            }   
        }

    }
    public void makeEdges(Field field){
        int x_coordinate = field.getCoordinates()[0];
        int y_coordinate = field.getCoordinates()[1];

        int cornerX = x_coordinate-field.getPosition()[0];
        int cornerY = y_coordinate-field.getPosition()[1];



        for (Field fields : wholeBoard[x_coordinate]){//Removes legal entry from itself rn
            if (fields.getValue() == 0 && field != fields && field.notcontainsEdge(fields)){
                field.addEdge(fields);
            }
            
        }
        for (int i = 0; i<this.size; i++){
            if (wholeBoard[i][y_coordinate].getValue() == 0 && field != wholeBoard[i][y_coordinate] && field.notcontainsEdge(wholeBoard[i][y_coordinate])){
                field.addEdge(wholeBoard[i][y_coordinate]);
            }
            
        }

        for (int j = 0; j<3 ; j++){
            for (int k = 0; k<3; k++){
                Field f = wholeBoard[cornerX+j][cornerY+k];
                if (f.getValue() == 0 && field.notcontainsEdge(f)){
                    field.addEdge(f);
                }
                
            }
        }

    }

    public Field[][] getWholeBoard() {
        return this.wholeBoard;
    }
    public int getSize(){
        return this.size;
    }
    public Field getSingleField(int x, int y){
        Field f = this.wholeBoard[x][y];
        return f;
    }


}
