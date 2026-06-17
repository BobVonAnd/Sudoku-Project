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
                if (!f.getEdges().isEmpty() && f.getValue() == 0){
                    edgeSolver(f);
                }
            }

        }
    }
    public void edgeSolver(Field field){
        single(field);
        pointingSingleInBox(field);
        if (field.getLegalEntries().size() == 2){
            XY_wing(field);
            //XY_Chain(field);
            nakedPair(field);   
        }
        hiddenPair(field);
    }

    public void single(Field field){
        ArrayList<Integer> legalEntries = new ArrayList<>(field.getLegalEntries());
        Integer value = null;
        if (field.getLeSize() == 1){
            value = legalEntries.get(0);
        }
        else {
            for (int legalEntry : legalEntries){
                if (!legalEntryInFields(field.getBoxEdges(), legalEntry)
                        || !legalEntryInFields(field.getRowEdges(), legalEntry)
                        || !legalEntryInFields(field.getColumnEdges(), legalEntry)) {
                    value = legalEntry;
                    break;
                }
            }
        }
        if (value != null){
            field.updateField(value);
            progress = true;
            moves.add("Single in field " + field.getStringCoords() + " value: " + value);
        }
    }
    public void nakedPair(Field field){
        if (field.getLeSize() != 2){
            return;
        }
        for (Field f : field.getEdges()){
            if (f.getLeSize() != 2){
                continue;
            }
            if (f != field && f.getLegalEntries().equals(field.getLegalEntries())) {
                for (int legalEntry : field.getLegalEntries()){
                    for (Field edge : field.getEdges()){
                        if (sameTypeEdge(field, f, edge)){
                            removeLegalEntryInConstraint(field, f, legalEntry);
                        }
                    }
                    moves.add("Found naked pair between" + field.getStringCoords() + " " + f.getStringCoords());
                }
            }   
        }
    }


    public void hiddenPair(Field field){
        ArrayList<Integer> legalEntries = new ArrayList<>(field.getLegalEntries());

        ArrayList<Field> boxEdges = field.getBoxEdges();
        ArrayList<Field> rowEdges = field.getRowEdges();
        ArrayList<Field> columnEdges = field.getColumnEdges();

        for (int i = 0; i < legalEntries.size() - 1; i++) {
            for (int j = i + 1; j < legalEntries.size(); j++) {
                ArrayList<Integer> pair = new ArrayList<>();
                pair.add(legalEntries.get(i));
                pair.add(legalEntries.get(j));

                Field boxPartner = findValidPairInFields(boxEdges, pair);
                Field rowPartner = findValidPairInFields(rowEdges, pair);
                Field columnPartner = findValidPairInFields(columnEdges, pair);

                if (boxPartner != null) {
                    applyHiddenPair(field, boxPartner, pair);
                    return;
                }

                if (rowPartner != null) {
                    applyHiddenPair(field, rowPartner, pair);
                    return;
                }

                if (columnPartner != null) {
                    applyHiddenPair(field, columnPartner, pair);
                    return;
                }
            }
        }
    }

    public void applyHiddenPair(Field field, Field partner, ArrayList<Integer> pair){
        int fieldSize = field.getLeSize();
        int partnerSize = partner.getLeSize();

        field.updateLes(pair);
        partner.updateLes(pair);

        if (field.getLeSize() < fieldSize || partner.getLeSize() < partnerSize){
            progress = true;
            moves.add("Found pair between " + field.getStringCoords() + " " + partner.getStringCoords() + " pair:" + pair);
        }
    }
    public void pointingSingleInBox(Field field) {
    for (int candidate : field.getLegalEntries()) {
        boolean onlyInThisRow = true;
        boolean onlyInThisColumn = true;

        for (Field boxEdge : field.getBoxEdges()) {
            if (boxEdge.getLegalEntries().contains(candidate)) {
                if (!Field.isRowEdge(field, boxEdge)) {
                    onlyInThisRow = false;
                }
                if (!Field.isColumnEdge(field, boxEdge)) {
                    onlyInThisColumn = false;
                }
            }
        }

        if (onlyInThisRow) {
            for (Field rowEdge : field.getRowEdges()) {
                if (!field.getBoxEdges().contains(rowEdge)) {
                    int before = rowEdge.getLeSize();
                    rowEdge.removeLE(candidate);

                    if (rowEdge.getLeSize() != before) {
                        moves.add("Pointing candidate " + candidate + " from box removes from row at " + rowEdge.getStringCoords());
                        progress = true;
                    }
                }
            }
        }

        if (onlyInThisColumn) {
            for (Field columnEdge : field.getColumnEdges()) {
                if (!field.getBoxEdges().contains(columnEdge)) {
                    int before = columnEdge.getLeSize();
                    columnEdge.removeLE(candidate);

                    if (columnEdge.getLeSize() != before) {
                        moves.add("Pointing candidate " + candidate + " from box removes from column at " + columnEdge.getStringCoords());
                        progress = true;
                    }
                }
            }
        }
    }
}
    public void XY_wing(Field hinge){
        if (hinge.getLegalEntries().size() != 2){
            return;
        }
        ArrayList<Field> hingeEdges = hinge.getEdges();
        ArrayList<Integer> hingeEntries = hinge.getLegalEntries();
        for (Field edge1 : hingeEdges){
            for (Field edge2 : hingeEdges){
                if (edge1 != edge2 && !intersect(edge1, edge2) && edge1.getLeSize() == 2 && edge2.getLeSize() == 2){
                    ArrayList<Integer> edge1Entries = edge1.getLegalEntries();
                    ArrayList<Integer> edge2Entries = edge2.getLegalEntries();
                    Integer entry1 = hingeEntries.get(0);
                    Integer entry2 = hingeEntries.get(1);
                    Integer CommonCandidate = null;
                    for (int edge1Int : edge1Entries){
                        for (int edge2Int : edge2Entries){
                            if (edge1Int == edge2Int && edge1Int!= entry1 && edge1Int != entry2){
                                CommonCandidate = edge1Int;
                            }
                        }
                    }
                    if (CommonCandidate == null){
                        continue;
                    }
                    for (Field intersectEdge : edge1.getEdges()){
                        if (intersect(edge2, intersectEdge) && intersectEdge != hinge){
                            if (edge1Entries.contains(entry1) && edge2Entries.contains(entry2) && !edge1Entries.contains(entry2) && !edge2Entries.contains(entry1)
                            || edge1Entries.contains(entry2) && edge2Entries.contains(entry1) && !edge1Entries.contains(entry1) && !edge2Entries.contains(entry2)){
                                Integer leSize = intersectEdge.getLeSize();
                                intersectEdge.removeLE(CommonCandidate);
                                if (leSize > intersectEdge.getLeSize()){
                                    progress = true;
                                    moves.add("Found XY pattern between " + hinge.getStringCoords() + " " + edge1.getStringCoords() + " " + edge2.getStringCoords() + " number is " + CommonCandidate + " removed from " + intersectEdge.getStringCoords());
                                }
                            }
                            
                        }
                    }
                }
            }
        }
    }
    public void XY_Chain(Field hinge){
        if (hinge.getLeSize() != 2){
            return;
        }
        ArrayList<Integer> legalEntries = hinge.getLegalEntries();
        Field result = XY_Chain_Link(hinge, hinge, legalEntries.get(0), legalEntries.get(1));
        ArrayList<Field> visited = new ArrayList<>();
        if (result != null){
            removeLegalEntryFromIntersection(hinge, result, legalEntries.get(0));
        }
        if (result == null){
            result = XY_Chain_Link(hinge, hinge, legalEntries.get(1), legalEntries.get(0));
            if (result != null){
                removeLegalEntryFromIntersection(hinge, result, legalEntries.get(1));
            }
        }
    }
    public Field XY_Chain_Link(Field link, Field hinge, Integer legalEntry, Integer connection){
        if (link.getLeSize() != 2){
            return null;
        }
        if (!link.getLegalEntries().contains(connection)){
            return null;
        }
        if (link != hinge && !intersect(link, hinge) && link.getLegalEntries().contains(legalEntry)){
            return link;
        }
        for (Field edge : link.getEdges()){
            ArrayList<Integer> legalEntries = edge.getLegalEntries();
            if (edge == hinge){
                continue;
            }
            if (edge.getLeSize() != 2){
                continue;
            }
            Field result = XY_Chain_Link(edge, hinge, legalEntry, legalEntries.get(1));
            if (result == null){
                result = XY_Chain_Link(edge, hinge, legalEntry, legalEntries.get(0));
            }
            if (result != null){
                return result;
            }
        }
        return null;
    }

    public boolean legalEntryInFields(ArrayList<Field> fields, int legalEntry){
        for (Field field : fields){
            if (field.getLegalEntries().contains(legalEntry)){
                return true;
            }
        }
        return false;
    }
    public Field findValidPairInFields(ArrayList<Field> fields, ArrayList<Integer> legalEntries){
        int fieldsCounter = 0;
        Field candidate = null;
        for (Field field : fields){
            for (int legalEntry : legalEntries){
                if (field.getLegalEntries().contains(legalEntry)){
                    fieldsCounter++;
                    if (field.getLegalEntries().containsAll(legalEntries) && fieldsCounter < 2){
                        candidate = field;
                    }
                    if (fieldsCounter >1){
                        return null;
                    }  
                    break; 
                }
            }
        }
        return candidate;
    }
    
    
    public boolean intersect(Field field1, Field field2){
        return field1.getEdges().contains(field2) && field2.getEdges().contains(field1);
    }
    public boolean fieldIsInIntersection(Field field1, Field field2, Field intersectField){
        if (field1 == null || field2 == null || intersectField == null){
            return false;
        }
        return intersect(field1, intersectField) && intersect(field2, intersectField);
    }
    public boolean sameTypeEdge(Field field1, Field field2, Field edge){
        if (Field.isBoxEdge(field1,edge) && Field.isBoxEdge(field2, edge)){
            return true;
        }
        if (Field.isRowEdge(field1,edge) && Field.isRowEdge(field2, edge)){
            return true;
        }
        if (Field.isColumnEdge(field1,edge) && Field.isColumnEdge(field2, edge)){
            return true;
        }
        return false;
    }

    public ArrayList<String> getMoves(){
        return moves;
    }
    public void removeLegalEntryFromIntersection(Field field1, Field field2, int legalEntry){
        for (Field edge : field1.getEdges()){
            if (fieldIsInIntersection(field1, field2, edge) && edge != field2 && edge != field1){
                int size = edge.getLeSize();
                edge.removeLE(legalEntry);
                if (edge.getLeSize() < size){
                    progress = true;
                    moves.add("Because of XY chain this field" + edge.getStringCoords() + " has removed " + legalEntry);
                }
                if (edge.getValue() == 0 && edge.getLeSize() == 0){
                    moves.add("I removed the last LE");
                }
                
            }
        }
    }
    public void removeLegalEntryInConstraint(Field field1, Field field2, int legalEntry){
        for (Field edge : field1.getEdges()){
            if (edge == field1 || edge == field2) {
                continue;
            }
            if (sameTypeEdge(field1, field2, edge)){
                int size = edge.getLeSize();
                edge.removeLE(legalEntry);
                if (edge.getLeSize() < size){
                    progress = true;
                }
                if (edge.getLeSize() <= 0 && edge.getValue() == 0){
                    moves.add("I removed the last legal entry here");
                }
            }
        }
    }
}
