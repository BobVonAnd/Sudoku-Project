package com.sudoku.model;

import java.util.ArrayList;

public class Field {
    private int value, x, y;
    private ArrayList<Integer> legalEntries = new ArrayList<>();
    private ArrayList<Field> Edges = new ArrayList<>();
    public Object getLegalEntries;


    public Field(int x, int y, int value, int size){
        this.x = x;
        this.y = y;
        this.value = value;

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
    public ArrayList<Integer> getLegalEntries(){
        return legalEntries;
    }
}

