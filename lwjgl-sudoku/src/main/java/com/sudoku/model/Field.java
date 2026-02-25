package com.sudoku.model;

import java.util.ArrayList;
import java.util.List;

public class Field {
    private int value, x, y;
    private List<Integer> legalEntries = new ArrayList<>();


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


}


