package com.sudoku.model;

import java.util.ArrayList;
import java.util.List;

public class Field {
    private int value, x, y;
    private List<Integer> legalEntries = new ArrayList<>();
    private List<Field> Edges = new ArrayList<>();


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
    public void removeEdge(Field field){
        Edges.remove(field);
    }


}


