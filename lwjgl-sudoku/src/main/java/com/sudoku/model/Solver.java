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
                
                //if (f.getValue() == 0 && f.getLeSize() == 2){
                //    boxPair(f);
                //    progress = true;
                //    continue;
                //}
            }
        }
    }
}
    public void boxPair(Field f){   
        ArrayList<Field> edges = f.getEdges(); 
        ArrayList<Integer> legalEntries = f.getLegalEntries();
        for (Field g : edges){
            if (isBoxEdge(f, g)){
                ArrayList<Integer> partnerLe = g.getLegalEntries();
                if (partnerLe.size() == 2){
                    if (legalEntries.containsAll(partnerLe)){
                        for (Field h : edges){
                            if (!isBoxEdge(f, h)){
                                continue;
                            }
                            if (h == f || h == g){
                                continue;
                            }
                            if (h.getValue() != 0){
                                continue;
                            }
                            if (h.getLegalEntries().equals(legalEntries)){
                                continue;
                            }
                            if (h.getLeSize() <= 2){
                                continue;
                            }
                            for (Integer i : legalEntries){
                                System.out.println("This is the pair" + f.getLegalEntries().get(0) + f.getLegalEntries().get(1) + "  "+ g.getLegalEntries().get(0)+ g.getLegalEntries().get(1));
                                System.out.println("This is the legal entry that is being removed" + h.getLegalEntries());
                                h.removeLE(i);
                                System.out.println("This is the legal entry that is being removed after" + h.getLegalEntries());
                            }
                        }
                    }
                }
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
