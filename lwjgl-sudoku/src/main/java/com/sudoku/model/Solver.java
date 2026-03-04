package com.sudoku.model;
import java.util.ArrayList;

public class Solver {
    private boolean progress = true;
    private int counter = 0;
    public void solves(SudokuBoard sudokuboard) {
    
    while (progress) {
        progress = false;
        int x = 0;
        int y = 0;
        for ( x = 0; x < sudokuboard.getSize(); x++){
            for ( y = 0; y < sudokuboard.getSize(); y++){
                Field f = sudokuboard.getSingleField(x, y);
                int candidate = lookAtNeighbours(f);
                if (f.getValue() == 0 && f.getLeSize() == 1) {
                    sudokuboard.changeField(
                        f.getCoordinates()[0],
                        f.getCoordinates()[1],
                        f.getLegalEntries().get(0)
                    );
                    sudokuboard.updateLegalEntriesOfField(f);
                    f.removeValueFromLegalEntriesOfNeighbours();
                    progress = true;
                }
                else if (candidate != 0 && f.getValue() == 0){
                    sudokuboard.changeField(
                        f.getCoordinates()[0],
                        f.getCoordinates()[1],
                        candidate
                    );
                    sudokuboard.updateLegalEntriesOfField(f);
                    f.removeValueFromLegalEntriesOfNeighbours();
                    progress = true;
                }
                
                if (f.getValue() == 0 && f.getLeSize() == 2){
                    progress = nakedPair(f, progress);
                    System.out.println("Used naked pair");
                }
                if (f.getValue() == 0 && f.getLegalEntries().isEmpty()) {
                    System.out.println("HOLE at: " + x + "," + y);
                }
                if (x >=sudokuboard.getSize() && y == sudokuboard.getSize()-1 && progress == false && counter == 0){
                    System.out.println("Trying more complex algorithms");
                    counter = 1;
                }
            }
        }
    }
}
    public Boolean nakedPair(Field f, boolean bool){   
        ArrayList<Field> edges = f.getEdges();
        ArrayList<Field> boxEdges = new ArrayList<>();
        ArrayList<Field> yEdges = new ArrayList<>();
        ArrayList<Field> xEdges = new ArrayList<>();
        for (Field g : edges){
            if (isBoxEdge(f, g)){//Same box
                boxEdges.add(g);
            }
            if (isRowEdge(f, g)){//Same row
                xEdges.add(g);
            }
            if (isColumnEdge(f, g)){//Same column
                yEdges.add(g);
            }
        }
        for (Field g : edges){
            if ((g.getLeSize() == 2 && g.getLegalEntries().equals(f.getLegalEntries())) && g.equals(f) == false){
                if (isBoxEdge(f, g)){
                    for (Field h : boxEdges){
                        if (h.getValue() == 0 && h!=g && h!=f){ 
                            for (Integer i : f.getLegalEntries()){
                                h.removeLE(i);
                                bool = true;
                            }
                        }
                    }
                }
                else if (isRowEdge(f, g)){
                    for (Field h : xEdges){
                        if (h.getValue() == 0 && h!=g && h!=f){ 
                            for (Integer i : f.getLegalEntries()){
                                h.removeLE(i);
                                bool = true;
                            }
                        }
                    }
                }
                else {
                    for (Field h : yEdges){
                        if (h.getValue() == 0 && h!=g && h!=f){ 
                            for (Integer i : f.getLegalEntries()){
                                h.removeLE(i);
                                bool = true;
                            }
                        }
                    }
                }
            }
        }
        return bool;
    }

    public Integer lookAtNeighbours(Field f){
        if (f.getValue() != 0) return 0;
        ArrayList<Field> edges = f.getEdges();
        ArrayList<Field> boxEdges = new ArrayList<>();
        ArrayList<Field> yEdges = new ArrayList<>();
        ArrayList<Field> xEdges = new ArrayList<>();
        for (Field g : edges){
            if (isBoxEdge(f, g)){//Same box
                boxEdges.add(g);
            }
            if (isRowEdge(f, g)){//Same row
                xEdges.add(g);
            }
            if (isColumnEdge(f, g)){//Same column
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
        if (otherField_x == x_coordinate && otherField_y == y_coordinate){
            return false;
        }

        for (int j = 0; j<3 ; j++){
            for (int k = 0; k<3; k++){
                if(cornerX+j == otherField_x && cornerY+k == otherField_y ){
                    return true;
                }
            }
        }
        return false;
        
    }
    public Boolean isRowEdge(Field f, Field otherField){
        return otherField.getCoordinates()[0]!=f.getCoordinates()[0] && otherField.getCoordinates()[1] == f.getCoordinates()[1];
        
    }
    public Boolean isColumnEdge(Field f, Field otherField){
        return otherField.getCoordinates()[1]!=f.getCoordinates()[1] && otherField.getCoordinates()[0] == f.getCoordinates()[0];
        
    }

}
