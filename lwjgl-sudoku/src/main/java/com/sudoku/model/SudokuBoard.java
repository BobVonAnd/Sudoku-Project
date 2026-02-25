package com.sudoku.model;

public class SudokuBoard {
    
    private Field[][] wholeBoard;
    private int size;

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
        changeField(0, 0, 1);
        changeField(3, 0, 4);
        changeField(7, 0, 9);
        changeField(6, 1, 8);
        changeField(6, 2, 1);
        changeField(2, 2, 5);
        changeField(3, 2, 3);
        changeField(4, 2, 2);
        changeField(6, 2, 1);
        changeField(6, 3, 5);
        changeField(5, 3, 4);
        changeField(3, 3, 9);
        changeField(1, 3, 8);
        changeField(1, 4, 1);
        changeField(2, 4, 4);
        changeField(3, 4, 8);
        changeField(6, 4, 2);
        changeField(3, 5, 2);
        changeField(5, 5, 7);
        changeField(1, 6, 2);
        changeField(4, 6, 6);
        changeField(8, 6, 9);
        changeField(8, 7, 8);
        changeField(5, 7, 2);
        changeField(2, 7, 3);
        changeField(0, 7, 6);
        changeField(1, 8, 7);
        changeField(8, 8, 3);
    }

    public void changeField(int x, int y, int value) {
        // changes a value of a field and therefore updates other legal entries
        Field field = wholeBoard[x][y];
        field.setValue(value);
        
    }

    public void updateLegalEntriesOfField(Field field){
        int[] corner = new int[1];
        int x_coordinate = field.getCoordinates()[0];
        int y_coordinate = field.getCoordinates()[1];

        corner[0] = x_coordinate-field.getPosition()[0];
        corner[1] = y_coordinate-field.getPosition()[1];


        for (Field fields : wholeBoard[x_coordinate]){//Removes legal entry from itself rn
            fields.removeLE(field.getValue());
        }
        for (int i = 0; i<this.size; i++){
            wholeBoard[i][y_coordinate].removeLE(field.getValue());
        }
        for (int j = 0; j<3 ; j++){
            for (int k = 0; k<3; k++){
                if (wholeBoard[corner[0]+j][corner[1]+k].getPosition()[0] == field.getPosition()[0] || wholeBoard[corner[0]+j][corner[1]+k].getPosition()[1] == field.getPosition()[1]){
                    continue;
                }
                wholeBoard[corner[0]+j][corner[1]+k].removeLE(field.getValue());
            }
        }

    }
    public void makeEdges(Field field){
        int[] corner = new int[2];
        int x_coordinate = field.getCoordinates()[0];
        int y_coordinate = field.getCoordinates()[1];

        corner[0] = x_coordinate-field.getPosition()[0];
        corner[1] = y_coordinate-field.getPosition()[1];


        for (Field fields : wholeBoard[x_coordinate]){//Removes legal entry from itself rn
            if (fields.getValue() == 0){
                field.addEdge(fields);
            }
            
        }
        for (int i = 0; i<this.size; i++){
            if (wholeBoard[i][y_coordinate].getValue() == 0){
                field.addEdge(wholeBoard[i][y_coordinate]);
            }
            
        }
        for (int j = 0; j<3 ; j++){
            for (int k = 0; k<3; k++){
                if (wholeBoard[corner[0]+j][corner[1]+k].getPosition()[0] == field.getPosition()[0] || wholeBoard[corner[0]+j][corner[1]+k].getPosition()[1] == field.getPosition()[1]){
                    continue;
                }
                if (wholeBoard[corner[0]+j][corner[1]+k].getValue() == 0){
                    field.addEdge(wholeBoard[corner[0]+j][corner[1]+k]);
                }
                
            }
        }

    }


}
