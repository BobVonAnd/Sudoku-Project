package com.sudoku.model;

import java.util.ArrayList;

import com.sudoku.view.Button;

public class Field {
    private int value, x, y, boardSize;
    private ArrayList<Integer> legalEntries = new ArrayList<>();
    private ArrayList<Field> Edges = new ArrayList<>();
    private ArrayList<Field> boxEdges = new ArrayList<>();
    private ArrayList<Field> yEdges = new ArrayList<>();
    private ArrayList<Field> xEdges = new ArrayList<>();
    private Button button;
    private double[] colour;

    public Field(int x, int y, int value, int size, double[] colour){

        this.x = x;
        this.y = y;
        this.value = value;
        this.boardSize = size;
        this.colour = colour;

        for(int i = 1; i <= size; i++){
            legalEntries.add(i);
        }
    }
    public void addLegalEntry(int entry){
        this.legalEntries.add(entry);
    }
    public void setValue(int value){
        this.value = value;
    }
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
    public boolean notcontainsEdge(Field field){
        if (Edges.contains(field) == true){
            return false;
        } 
        return true;
    }
    public void removeEdges(){//This method removes all out going edges
        Edges.clear();
    }
    public void clearLe(){
        this.legalEntries.clear();
    }
    public void removeLE(int LE){
        this.legalEntries.remove(Integer.valueOf(LE));
        this.legalEntries = new ArrayList<>(this.legalEntries);
    }
    public void removeLEs(ArrayList<Integer> Le){
        this.legalEntries.removeAll(Le);
    }
    public void updateLes(ArrayList<Integer> newLe){
        this.legalEntries.clear();
        this.legalEntries = newLe;
    }
    public void removeEdge(Field field){//This method removes an incoming edge
        Edges.remove(field);
    }
    public int[] getCoordinates(){
        int[] coordinates = new int[2];
        coordinates[0] = this.x;
        coordinates[1] = this.y;
        return coordinates;
    }
    public String getStringCoords(){
        return ("[" + this.x + "," + this.y + "]");
    }
    public void removeValueFromLegalEntriesOfNeighbours(){
        for (Field f : this.Edges){
            f.removeLE(this.value);
        }
    }

    public int[] getPosition(){
        int[] position = new int[2];
        position[0] = this.x % 3;
        position[1] = this.y % 3;
        return position;
    }
    public ArrayList<Field> getEdges(){
        return this.Edges;
    }
    public Integer getLeSize(){
        return legalEntries.size();
    }
    public Integer getValue(){
        return this.value;
    }
    public int getBoardSize() {
        return boardSize;
    }
    public ArrayList<Integer> getLegalEntries(){
        return legalEntries;
    }

    public Button getButton(){
        return button;
    }
    public void changeColour(double red, double green, double blue){
        this.colour = new double[]{red, green, blue};
    }
    public double getRed(){
        return this.colour[0];
    }
        public double getGreen(){
        return this.colour[1];
    }
        public double getBlue(){
        return this.colour[2];
    }
    public ArrayList<Field> getBoxEdges(){
        return this.boxEdges;
    }
    public ArrayList<Field> getRowEdges(){
        return this.xEdges;
    }
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
    public static Boolean isRowEdge(Field f, Field otherField){
        return otherField.getCoordinates()[1]!=f.getCoordinates()[1] && otherField.getCoordinates()[0] == f.getCoordinates()[0];
    }
    public static Boolean isColumnEdge(Field f, Field otherField){
        return otherField.getCoordinates()[0]!=f.getCoordinates()[0] && otherField.getCoordinates()[1] == f.getCoordinates()[1];
    }
    public void updateField(int number){
        this.setValue(number);
        this.removeValueFromLegalEntriesOfNeighbours();
        this.clearLe();
        this.removeEdges();
    }
    public static ArrayList<Integer> getUnionOfFieldsLegalEntries(ArrayList<Field> fields){
        ArrayList<Integer> union = new ArrayList<>();
        for (Field field : fields){
            union.addAll(field.getLegalEntries());
        }
        return union;
    }
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

