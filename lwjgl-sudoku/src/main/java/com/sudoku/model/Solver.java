package com.sudoku.model;
import java.util.ArrayList;

public class Solver {
    private boolean solved = false;
    public void solves(SudokuBoard sudokuboard) {
    
    while (!solved) {
        boolean progress = false;
        int x = 0;
        int y = 0;
        for ( x = 0; x < sudokuboard.getSize(); x++){
            for ( y = 0; y < sudokuboard.getSize(); y++){
                Field f = sudokuboard.getSingleField(x, y);
                if (f.getValue() == 0 && f.getLeSize() == 1) {
                    sudokuboard.changeField(
                        f.getCoordinates()[0],
                        f.getCoordinates()[1],
                        f.getLegalEntries().get(0)
                    );
                    sudokuboard.updateLegalEntriesOfField(f);
                    f.removeValueFromLegalEntriesOfNeighbours();
                    progress = true;
                    continue;
                }
                int candidate = lookAtNeighbours(f);
                if (candidate != 0 && f.getValue() == 0){
                    sudokuboard.changeField(
                        f.getCoordinates()[0],
                        f.getCoordinates()[1],
                        candidate
                    );
                    sudokuboard.updateLegalEntriesOfField(f);
                    f.removeValueFromLegalEntriesOfNeighbours();
                    progress = true;
                    continue;
                }
            }
        }
        if (progress == false){
            solved = true;
        }
    }
}
    public Integer lookAtNeighbours(Field f){
        if (f.getValue() != 0) return 0;
        ArrayList<Field> edges = f.getEdges();
        ArrayList<Field> boxEdges = new ArrayList<>();
        ArrayList<Field> yEdges = new ArrayList<>();
        ArrayList<Field> xEdges = new ArrayList<>();
        for (Field g : edges){
            if (isBoxEdge(f, g)){
                boxEdges.add(g);
            }
            if (g.getCoordinates()[0]!=f.getCoordinates()[0] && g.getCoordinates()[1] == f.getCoordinates()[1]){
                xEdges.add(g);
            }
            if (g.getCoordinates()[1]!=f.getCoordinates()[1] && g.getCoordinates()[0] == f.getCoordinates()[0]){
                yEdges.add(g);
            }
        }
        for (int candidate : f.getLegalEntries()){
            boolean candidateInBox = false;
            for (Field k : boxEdges){
                if (k.getLegalEntries().contains(candidate) && k.getValue() == 0){
                    candidateInBox = true;
                    break;
                }
            }
            if (!candidateInBox) return candidate;

            boolean candidateInRow = false;
            for (Field k : xEdges){
                if (k.getLegalEntries().contains(candidate) && k.getValue() == 0){
                    candidateInRow = true;
                    break;
                }
            }
            if (!candidateInRow) return candidate;

            boolean candidateInColumn = false;
            for (Field k : yEdges){
                if (k.getLegalEntries().contains(candidate) && k.getValue() == 0){
                    candidateInColumn = true;
                    break;
                }
            }
            if (!candidateInColumn) return candidate;
        }
        return 0;
    }
    public Boolean isBoxEdge(Field f, Field otherField){
        int x_coordinate = f.getCoordinates()[0];
        int y_coordinate = f.getCoordinates()[1];

        int cornerX = x_coordinate-f.getPosition()[0];
        int cornerY = y_coordinate-f.getPosition()[1];

        int otherField_x = otherField.getCoordinates()[0];
        int otherField_y = otherField.getCoordinates()[1];

        for (int j = 0; j<3 ; j++){
            for (int k = 0; k<3; k++){
                if(cornerX+j == otherField_x && cornerY+k == otherField_y ){
                    return true;
                }
            }
        }
        return false;
        
    }
}
