package com.sudoku.model;

import java.util.ArrayList;

public class Field {
    private int value, x, y;
    private ArrayList<Integer> legalEntries = new ArrayList<>();
    private ArrayList<Field> Edges = new ArrayList<>();


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
        Edges.add(field);
    }
    public void removeEdges(Field field){//This method removes all out going edges
        Edges.clear();
    }
    public void updateLE(int LE){
        legalEntries.remove(Integer.valueOf(LE));
    }
    public void removeEdge(Field field){//This method removes an incoming edge
        Edges.remove(field);
    }
    public ArrayList<Integer> getCoordinates(){
        ArrayList<Integer> coordinates = new ArrayList<>();
        coordinates.add(this.x);
        coordinates.add(this.y);
        return coordinates;
    }
    public ArrayList<Integer> getPosition(){
        ArrayList<Integer> position = new ArrayList<>();
        position.add(this.x % 3);
        position.add(this.y % 3);
        return position;
    }
    public ArrayList<Field> getEdges(){
        return this.Edges;
    }
    public Integer getLeSize(){
        return legalEntries.size();
    }
}


