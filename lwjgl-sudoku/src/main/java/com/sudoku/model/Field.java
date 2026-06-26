package com.sudoku.model;

import java.util.ArrayList;

public class Field {
    private int value, x, y, boardSize; // just some init int variables
    private ArrayList<Integer> legalEntries = new ArrayList<>(); // all legal entries into the field
    private ArrayList<Field> Edges = new ArrayList<>(); // edge to fields
    private ArrayList<Field> boxEdges = new ArrayList<>(); // box references to other fields
    private ArrayList<Field> yEdges = new ArrayList<>(); // y references to other fields
    private ArrayList<Field> xEdges = new ArrayList<>(); // x references to other fields
    private double[] colour; // color of field

    // what notes are active
    private boolean[] notesFields = new boolean[] {false, false, false, false, false, false, false, false, false};

    //if value is correct then it can't be changed
    private boolean locked = true;


    public Field(int x, int y, int value, int size, double[] colour){

        this.x = x;
        this.y = y;
        this.value = value;
        this.boardSize = size;
        this.colour = colour;

        for(int i = 1; i <= size; i++){
            legalEntries.add(i); // add every single other to legal entries, when initted
        }
    }

    // activates a note
    public void setNote(int noteIndex, boolean isOn){
        notesFields[noteIndex-1] = isOn;
    }

    // returns the notes
    public boolean[] getNote(){
        return notesFields;
    }

    // set if the field is locked
    public void setLocked(boolean isLocked){
        this.locked = isLocked;
    }

    // gets if the field has been locked
    public boolean getLocked(){
        return locked;
    }

    // adds a legal entry to the fields legal entries
    public void addLegalEntry(int entry){
        this.legalEntries.add(entry);
    }

    // sets the value of a field
    public void setValue(int value){
        this.value = value;
    }

    // adds edge between fields
    public void addEdge(Field field){
        if (Edges.contains(field) == false){
            Edges.add(field);
        }
        if (isBoxEdge(this, field)){
            boxEdges.add(field);
        }
        if (isColumnEdge(this, field)){
            yEdges.add(field);
        }
        else if (isRowEdge(this, field)){
            xEdges.add(field);
        }
        
    }
    // returns if this field has an edge to the other field
    public boolean notcontainsEdge(Field field){
        if (Edges.contains(field) == true){
            return false;
        } 
        return true;
    }
    
    //This method removes all out going edges
    public void removeEdges(){ 
        Edges.clear();
    }

    // clear the legal entries
    public void clearLe(){
        this.legalEntries.clear();
    }

    // removes a legal entry
    public void removeLE(int LE){
        this.legalEntries.remove(Integer.valueOf(LE));
        this.legalEntries = new ArrayList<>(this.legalEntries);
    }

    // remove all legal entries
    public void removeLEs(ArrayList<Integer> Le){
        this.legalEntries.removeAll(Le);
    }

    // update legal entries
    public void updateLes(ArrayList<Integer> newLe){
        this.legalEntries.clear();
        this.legalEntries = newLe;
    }

    // removes edge
    public void removeEdge(Field field){//This method removes an incoming edge
        Edges.remove(field);
    }

    // gets coordinates from this
    public int[] getCoordinates(){
        int[] coordinates = new int[2];
        coordinates[0] = this.x;
        coordinates[1] = this.y;
        return coordinates;
    }

    // coords in string form
    public String getStringCoords(){
        return ("[" + this.x + "," + this.y + "]");
    }

    // removes a value from a legal entry of a neighbours
    public void removeValueFromLegalEntriesOfNeighbours(){
        for (Field f : this.Edges){
            f.removeLE(this.value);
        }
    }

    // gets position
    public int[] getPosition(){
        int[] position = new int[2];
        position[0] = this.x % 3;
        position[1] = this.y % 3;
        return position;
    }

    // get edges
    public ArrayList<Field> getEdges(){
        return this.Edges;
    }

    // get legal entry size
    public Integer getLeSize(){
        return legalEntries.size();
    }

    // get value of field
    public Integer getValue(){
        return this.value;
    }

    // get board size
    public int getBoardSize() {
        return boardSize;
    }

    // get legal entries
    public ArrayList<Integer> getLegalEntries(){
        return legalEntries;
    }
    
    // change color of field
    public void changeColour(double red, double green, double blue){
        this.colour = new double[]{red, green, blue};
    }
    
    // get r coordinate from color
    public double getRed(){
        return this.colour[0];
    }
    
    // get g coordinate from color
    public double getGreen(){
        return this.colour[1];
    }

    // get b coordinate from color
    public double getBlue(){
        return this.colour[2];
    }

    // get box edges
    public ArrayList<Field> getBoxEdges(){
        return this.boxEdges;
    }

    // get row edges
    public ArrayList<Field> getRowEdges(){
        return this.xEdges;
    }

    // get column edges
    public ArrayList<Field> getColumnEdges(){
        return this.yEdges;
    }

    
    public static Boolean isBoxEdge(Field f, Field otherField){
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

    // is row edge
    public static Boolean isRowEdge(Field f, Field otherField){
        return otherField.getCoordinates()[1]!=f.getCoordinates()[1] && otherField.getCoordinates()[0] == f.getCoordinates()[0];
    }

    // is column edge
    public static Boolean isColumnEdge(Field f, Field otherField){
        return otherField.getCoordinates()[0]!=f.getCoordinates()[0] && otherField.getCoordinates()[1] == f.getCoordinates()[1];
    }

    // update field with a number
    public void updateField(int number){
        this.setValue(number);
        this.removeValueFromLegalEntriesOfNeighbours();
        this.clearLe();
    }

    // get complement of legal entries
    public static ArrayList<Integer> getComplementOfLegalEntries(Integer legalEntry, Field field){
        ArrayList<Integer> legalEntries = new ArrayList<>(field.getLegalEntries());
        legalEntries.remove(legalEntry);
        return legalEntries;
    }

    // get other legal entry
    public static Integer getOtherLegalEntry(Integer legalEntry, Field field){
        if (field.getLeSize() != 2){
            return null;
        }
        ArrayList<Integer> legalEntries = new ArrayList<>(field.getLegalEntries());
        legalEntries.remove(legalEntry);
        return legalEntries.get(0);
    }

    // getUnionOfFieldsLegalEntries
    public static ArrayList<Integer> getUnionOfFieldsLegalEntries(ArrayList<Field> fields){
        ArrayList<Integer> union = new ArrayList<>();
        for (Field field : fields){
            union.addAll(field.getLegalEntries());
        }
        return union;
    }
    
    // getIntersectionOfFieldsLegalEntries
    public static ArrayList<Integer> getIntersectionOfFieldsLegalEntries(ArrayList<Field> fields){
        ArrayList<Integer> intersection = new ArrayList<>();
        int size = fields.size();
        intersection.addAll(fields.get(0).getLegalEntries());
        for (int i = 1; i< size; i++){
            ArrayList<Integer> legalEntries = fields.get(i).getLegalEntries();
            intersection.retainAll(legalEntries);
        }
        return intersection;
    }
}

