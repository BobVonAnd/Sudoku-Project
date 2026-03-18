package com.sudoku.model;

import java.util.ArrayList;

import com.sudoku.view.Button;

public class Field {
    private int value, x, y, boardSize;
    private ArrayList<Integer> legalEntries = new ArrayList<>();
    private ArrayList<Field> Edges = new ArrayList<>();
    private Button button;
    public Object getLegalEntries;


    public Field(int x, int y, int value, int size){
        
        button = new Button(x, y);

        this.x = x;
        this.y = y;
        this.value = value;
        this.boardSize = size;

        for(int i = 1; i <= size; i++){
            legalEntries.add(i);
        }
    }
    public void setValue(int value){
        this.value = value;
    }
    public void addEdge(Field field){
        if (Edges.contains(field) == false){
            Edges.add(field);
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
    public void removeValueFromLegalEntriesOfNeighbours(){
        for (Field f : Edges){
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
}

