package com.sudoku.model;
import java.util.ArrayList;
import java.util.Collections;

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
                moves.add("Hidden single found at " + field.getStringCoords() + " number is " + candidate);
                field.updateField(candidate);
                progress = true;
            }
            hiddenPair(field);
            
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

    public void hiddenPair(Field field){
        //Fixes:
        //Maybe check if you can find a partner once and then check also if you can find other fields that hold one of the candidates
        //Remember, if they are the only two fields that hold those numbers you remove all other legal entries from those fields
        //Generally look at this function/method again

        
        ArrayList<Integer> legalEntries = field.getLegalEntries();
        Field boxPartner = null;
        Field rowPartner = null;
        Field columnPartner = null;
        for (int i : legalEntries){
            for (int j : legalEntries){
                if (j == i){
                    break;
                }
                int boxCounter = 0;
                int rowCounter = 0;
                int columnCounter = 0;
                ArrayList<Integer> candidate = new ArrayList<>();
                candidate.add(i);
                candidate.add(j);
                ArrayList<Field> boxEdges = field.getBoxEdges();
                ArrayList<Field> rowEdges = field.getRowEdges();
                ArrayList<Field> columnEdges = field.getColumnEdges();
                for (Field boxEdge : boxEdges){
                    if (!Collections.disjoint(candidate, boxEdge.getLegalEntries())){
                        boxCounter+=1;
                        boxPartner = boxEdge;
                    }
                }
                for (Field rowEdge : rowEdges){
                    if (!Collections.disjoint(candidate, rowEdge.getLegalEntries())){
                        rowCounter+=1;
                        rowPartner = rowEdge;
                    }
                }
                for (Field columnEdge : columnEdges){
                    if (!Collections.disjoint(candidate, columnEdge.getLegalEntries())){
                        columnCounter+=1;
                        columnPartner = columnEdge;
                    }
                }
                if (boxCounter == 1){
                    for (Field otherBoxEdge : field.getBoxEdges()){
                        if (otherBoxEdge == boxPartner){
                            continue;
                        }
                        progress = true;
                        otherBoxEdge.removeLEs(candidate);
                        moves.add("Found hidden pair between "+ field.getStringCoords() + " and " + boxPartner.getStringCoords() + " consisting of [" + candidate.get(0) +","+ candidate.get(1)+"]");
                    }
                }
                if (rowCounter == 1){
                    for (Field otherRowEdge : field.getRowEdges()){
                        if (otherRowEdge == rowPartner){
                            continue;
                        }
                        progress = true;
                        otherRowEdge.removeLEs(candidate);
                        moves.add("Found hidden pair between "+ field.getStringCoords() + " and " + rowPartner.getStringCoords() + " consisting of [" + candidate.get(0) +","+ candidate.get(1)+"]");
                    }
                }
                if (columnCounter == 1){
                    for (Field otherColumnEdge : field.getColumnEdges()){
                        if (otherColumnEdge == columnPartner){
                            continue;
                        }
                        progress = true;
                        otherColumnEdge.removeLEs(candidate);
                        moves.add("Found hidden pair between "+ field.getStringCoords() + " and " + columnPartner.getStringCoords() + " consisting of [" + candidate.get(0) +","+ candidate.get(1)+"]");
                    }
                }
            }
        }
    }


    public ArrayList<String> getMoves(){
        return moves;
    }
}

