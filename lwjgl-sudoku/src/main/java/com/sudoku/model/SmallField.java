package com.sudoku.model;

import java.util.ArrayList;
import java.util.List;

public class SmallField {
    private int value;
    private List<Integer> legalEntries = new ArrayList<>();


    public SmallField(int size){
        size = size*size;
        for(int i = 1; i <= size; i++){
            legalEntries.add(i);
        }
    }

    public void setValue(int value){
        this.value = value;
    }


}


