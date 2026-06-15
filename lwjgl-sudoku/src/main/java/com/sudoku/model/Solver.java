package com.sudoku.model;
import java.util.ArrayList;


public class Solver {
    private boolean progress = true;
    ArrayList<String> moves = new ArrayList<>();
    public void solves(SudokuBoard sudokuboard) {
        humanSolverBoardHelper boardManager = new humanSolverBoardHelper();
        boardManager.BoardHelper(sudokuboard);
        while(progress){
            
            System.out.println(sudokuboard.getSingleField(8, 5).getLegalEntries().size());
            progress = false;
            for (Field f : sudokuboard.getFields()){
                if (!f.getEdges().isEmpty()){
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
            XY_Chain(field);
            nakedPair(field);   
        }
        if (field.getLeSize() > 2){
            Field boxPair = hiddenBoxPair(field);
            if (boxPair != null){
                moves.add("Hidden box pair found at " + field.getStringCoords() + " and " + boxPair.getStringCoords());
                progress = true;
                Field nextEdge = chooseNextEdge(field);
                if (nextEdge != null) {
                    edgeSolver(nextEdge);
                }
            }
        }
        if (field.getLeSize() > 2 ){
            Field rowPair = hiddenRowPair(field);
            if (rowPair != null){
                moves.add("Hidden row pair found at " + field.getStringCoords() + " and " + rowPair.getStringCoords());
                progress = true;
                Field nextEdge = chooseNextEdge(field);
                if (nextEdge != null) {
                    edgeSolver(nextEdge);
                }
            }
        }
        if (field.getLeSize() > 2){
            Field columnPair = hiddenColumnPair(field);
            if (columnPair != null){
                moves.add("Hidden column pair found at " + field.getStringCoords() + " and " + columnPair.getStringCoords());
                progress = true;
                Field nextEdge = chooseNextEdge(field);
                if (nextEdge != null) {
                    edgeSolver(nextEdge);
                }
            }
        }

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

            Field nextEdge = chooseNextEdge(field);
            if (nextEdge != null) {
                edgeSolver(nextEdge);
            }
        }
    }

    public void nakedPair(Field field){
        for (Field f : field.getEdges()){
            if (f != field && f.getLegalEntries().equals(field.getLegalEntries())) {
                for (int legalEntry : field.getLegalEntries()){
                    removeLegalEntryFromIntersection(field, f, legalEntry);
                    moves.add("Found naked pair between" + field.getStringCoords() + " " + f.getStringCoords());
                }
            }   
        }
    }


    public void hiddenPair(Field field){
        ArrayList<Integer> legalEntries = field.getLegalEntries();
        for (int i = 0; i < legalEntries.size() - 1; i++) {
            for (int j = i + 1; j < legalEntries.size(); j++) {

            }
        }
    }


    public Field hiddenBoxPair(Field field) {
    ArrayList<Integer> legalEntries = field.getLegalEntries();
    for (int i = 0; i < legalEntries.size() - 1; i++) {
        for (int j = i + 1; j < legalEntries.size(); j++) {
            int candidate1 = legalEntries.get(i);
            int candidate2 = legalEntries.get(j);

            Field matchingField = null;
            int matchingcandidate1 = 0;
            int matchingcandidate2 = 0;
            int boxCount = 0;

            for (Field boxEdge : field.getBoxEdges()) {
                ArrayList<Integer> boxEntries = boxEdge.getLegalEntries();
                if (boxEntries.contains(candidate1) || boxEntries.contains(candidate2)) {
                    boxCount++;
                    if (boxEntries.contains(candidate1) && boxEntries.contains(candidate2)) {
                        matchingField = boxEdge;
                        matchingcandidate1 = candidate1;
                        matchingcandidate2 = candidate2;
                    }
                }
            }

            if (boxCount == 1 && matchingField != null) {
                field.clearLe();
                field.addLegalEntry(matchingcandidate1);
                field.addLegalEntry(matchingcandidate2);
                matchingField.clearLe();
                matchingField.addLegalEntry(matchingcandidate1);
                matchingField.addLegalEntry(matchingcandidate2);
                return matchingField;
            }
        }
    }
    return null;
} 

    public Field hiddenRowPair(Field field) {
    ArrayList<Integer> legalEntries = field.getLegalEntries();
    for (int i = 0; i < legalEntries.size() - 1; i++) {
        for (int j = i + 1; j < legalEntries.size(); j++) {
            int candidate1 = legalEntries.get(i);
            int candidate2 = legalEntries.get(j);

            Field matchingField = null;
            int matchingcandidate1 = 0;
            int matchingcandidate2 = 0;
            int rowCount = 0;

            for (Field rowEdge : field.getRowEdges()) {
                ArrayList<Integer> rowEntries = rowEdge.getLegalEntries();
                if (rowEntries.contains(candidate1) || rowEntries.contains(candidate2)) {
                    rowCount++;
                    if (rowEntries.contains(candidate1) && rowEntries.contains(candidate2)) {
                        matchingField = rowEdge;
                        matchingcandidate1 = candidate1;
                        matchingcandidate2 = candidate2;
                    }
                }
            }

            if (rowCount == 1 && matchingField != null) {
                field.clearLe();
                field.addLegalEntry(matchingcandidate1);
                field.addLegalEntry(matchingcandidate2);
                matchingField.clearLe();
                matchingField.addLegalEntry(matchingcandidate1);
                matchingField.addLegalEntry(matchingcandidate2);
                return matchingField;
            }
        }
    }
    return null;
} 

    public Field hiddenColumnPair(Field field) {
    ArrayList<Integer> legalEntries = field.getLegalEntries();
    for (int i = 0; i < legalEntries.size() - 1; i++) {
        for (int j = i + 1; j < legalEntries.size(); j++) {
            int candidate1 = legalEntries.get(i);
            int candidate2 = legalEntries.get(j);

            Field matchingField = null;
            int matchingcandidate1 = 0;
            int matchingcandidate2 = 0;
            int columnCount = 0;

            for (Field columnEdge : field.getColumnEdges()) {
                ArrayList<Integer> columnEntries = columnEdge.getLegalEntries();
                if (columnEntries.contains(candidate1) || columnEntries.contains(candidate2)) {
                    columnCount++;
                    if (columnEntries.contains(candidate1) && columnEntries.contains(candidate2)) {
                        matchingField = columnEdge;
                        matchingcandidate1 = candidate1;
                        matchingcandidate2 = candidate2;
                    }
                }
            }

            if (columnCount == 1 && matchingField != null) {
                field.clearLe();
                field.addLegalEntry(matchingcandidate1);
                field.addLegalEntry(matchingcandidate2);
                matchingField.clearLe();
                matchingField.addLegalEntry(matchingcandidate1);
                matchingField.addLegalEntry(matchingcandidate2);
                return matchingField;
            }
        }
    }
    return null;
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
        if (hinge.getLegalEntries().size() != 2){
            return;
        }
        for (int i = 0; i < hinge.getLegalEntries().size(); i++){
            Integer legalEntry = hinge.getLegalEntries().get(i);
            int j = 0;
            if (i == 0){
                j = i+1;
            }
            else {
                j = i-1;
            }
            Integer connection = hinge.getLegalEntries().get(j);
            for (Field link : hinge.getEdges()){
                if (link == hinge){
                    continue;
                }
                ArrayList<Field> chainLinks = new ArrayList<>();
                chainLinks = XY_Chain_Link(link, connection, chainLinks, legalEntry, hinge);
                if (chainLinks != null){
                    int size = chainLinks.size();
                    Field endField = chainLinks.get(size-1);
                    removeLegalEntryFromIntersection(hinge, endField, legalEntry);
                    moves.add("Found XY chain ending between " + hinge.getStringCoords() + " " + endField.getStringCoords());
                }
            }
        }
    }
    public ArrayList<Field> XY_Chain_Link(Field field, int connection, ArrayList<Field> chainLinks, Integer intEndPoint, Field hinge){
        if (field.getLeSize() != 2){
            return null;
        }
        if (chainLinks.contains(field)){
            return null;
        }
        for (Integer i = 0; i < field.getLegalEntries().size(); i++){
            Integer legalEntry = field.getLegalEntries().get(i);
            if (legalEntry.equals(intEndPoint) && chainLinks.size() > 0){
                chainLinks.add(field);
                return chainLinks;
            }
            else if (legalEntry.equals(connection)){
                chainLinks.add(field);
                if (i > 0){
                    connection = field.getLegalEntries().get(0);
                }
                else {
                    connection = field.getLegalEntries().get(1);
                }
                for (Field link : field.getEdges()){
                    if (link == field || link == hinge){
                        continue;
                    }
                        ArrayList<Field> result = XY_Chain_Link(link, connection, chainLinks, intEndPoint, hinge);
                        if (result != null){
                            return result;
                        }
                }
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
    public int fieldsThatContainLegalEntries(ArrayList<Field> fields, ArrayList<Integer> legalEntries){
        
    }
    public boolean intersect(Field field1, Field field2){
        return field1.getEdges().contains(field2) && field2.getEdges().contains(field1);
    }
    public boolean fieldIsInIntersection(Field field1, Field field2, Field intersectField){
        return intersect(field1, intersectField) && intersect(field2, intersectField);
    }
    public void nakedSingle(Field field){
        progress = true;
        int value = field.getLegalEntries().get(0);
        moves.add("Single in field " +field.getStringCoords() + "value: " + value);
        field.updateField(value);
        Field nextEdge = chooseNextEdge(field);
        if (nextEdge != null) {
            edgeSolver(nextEdge);
        }
    }


    public ArrayList<String> getMoves(){
        return moves;
    }
    public boolean removeLegalEntryFromIntersection(Field field1, Field field2, int legalEntry){
        boolean internalProgress = false;
        for (Field edge : field1.getEdges()){
            if (fieldIsInIntersection(field1, field2, edge) && edge != field2 && edge != field1){
                int size = edge.getLeSize();
                edge.removeLE(legalEntry);
                if (edge.getLeSize() < size){
                    progress = true;
                    internalProgress = true;
                }
                if (edge.getLeSize() <= 0 && edge.getValue() == 0){
                    moves.add("I removed the last legal entry here");
                }
            }
        }
    return internalProgress;
    }
    public Field chooseNextEdge(Field field){
    for (Field edge : field.getEdges()){
        field.removeEdge(edge);
        return edge;
    }
    return null;    
    }  
}
