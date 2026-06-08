package com.sudoku.model;
import java.util.ArrayList;

public class Solver {
    private boolean progress = true;
    ArrayList<String> moves = new ArrayList<>();
    public void solves(SudokuBoard sudokuboard) {
    humanSolverBoardHelper boardManager = new humanSolverBoardHelper();
    boardManager.BoardHelper(sudokuboard);
    while(progress){
        progress = false;
        for (Field f : sudokuboard.getFields()){
            if (!f.getEdges().isEmpty()){
                edgeSolver(f);
            }
        }
    }
}
    public void edgeSolver(Field field){
        if (field.getLegalEntries().size() == 1){
            progress = true;
            int value = field.getLegalEntries().get(0);
            moves.add("Single in field [" + field.getCoordinates()[0] + "," + field.getCoordinates()[1] + "] value: " + value);
            field.updateField(value);
        }
        else {
            if (field.getLegalEntries().size() == 2){
                Field pairField = nakedBoxPair(field);
                if (pairField != null){
                    for (Field boxField : field.getBoxEdges()){
                        if (boxField != field && boxField != pairField){
                            int boxFieldLeSizePre = boxField.getLeSize();
                            boxField.removeLEs(field.getLegalEntries());
                            if (boxFieldLeSizePre - boxField.getLeSize() != 0){
                                moves.add("Found naked pair between " + field.getStringCoords() + " and " + pairField.getStringCoords());
                                progress = true;
                            }
                        }
                    }
                }
                pairField = nakedRowPair(field);
                if (pairField != null){
                    for (Field rowField : field.getRowEdges()){
                        if (rowField != field && rowField != pairField){
                            int rowFieldLeSizePre = rowField.getLeSize();
                            rowField.removeLEs(field.getLegalEntries());
                            if (rowFieldLeSizePre - rowField.getLeSize() != 0){
                                moves.add("Found naked pair between " + field.getStringCoords() + " and " + pairField.getStringCoords());
                                progress = true;
                            }
                        }
                    }
                }
                pairField = nakedColumnPair(field);
                if (pairField != null){
                    for (Field columnField : field.getColumnEdges()){
                        if (columnField != field && columnField != pairField){
                            int columnFieldLeSizePre = columnField.getLeSize();
                            columnField.removeLEs(field.getLegalEntries());
                            if (columnFieldLeSizePre - columnField.getLeSize() != 0){
                                moves.add("Found naked pair between " + field.getStringCoords() + " and " + pairField.getStringCoords());
                                progress = true;
                            }
                        }
                    }
                }
                
            }
            int candidate = hiddenSingle(field);
            if (candidate != 0){
                moves.add("Hidden single found at " + field.getStringCoords());
                field.updateField(candidate);
            }
        }
    }
    public Field nakedBoxPair(Field field) {
    for (Field f : field.getBoxEdges()) {
        if (f != field && f.getLegalEntries().equals(field.getLegalEntries())) {
            return f;
        }
    }
    return null;
    }

    public Field nakedRowPair(Field field) {
        for (Field f : field.getRowEdges()) {
            if (f != field && f.getLegalEntries().equals(field.getLegalEntries())) {
                return f;
            }
        }
        return null;
    }

    public Field nakedColumnPair(Field field) {
        for (Field f : field.getColumnEdges()) {
            if (f != field && f.getLegalEntries().equals(field.getLegalEntries())) {
                return f;
            }
        }
        return null;
    }

    public int hiddenSingle(Field field){
        int candidate = 0;
        for (int i : field.getLegalEntries()){
        boolean candidateInRow = false;
        boolean candidateInBox = false;
        boolean candidateInColumn = false;
            for (Field rowEdge : field.getRowEdges()){
                if (rowEdge.getLegalEntries().contains(i)){
                    candidateInRow = true;
                    break;
                }
            }
            for (Field boxEdge : field.getBoxEdges()){
                if (boxEdge.getLegalEntries().contains(i)){
                    candidateInBox = true;
                    break;
                }
            }
            for (Field columnEdge : field.getColumnEdges()){
                if (columnEdge.getLegalEntries().contains(i)){
                    candidateInColumn = true;
                    break;
                }
            }
            if (candidateInBox == false || candidateInRow == false || candidateInColumn == false){
                candidate = i;
                break;
            }
        }
        
        return candidate;
    }
    public ArrayList<String> getMoves(){
        return moves;
    }
}

