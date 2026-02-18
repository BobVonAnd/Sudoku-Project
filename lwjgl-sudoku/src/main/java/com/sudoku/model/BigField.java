package com.sudoku.model;

import java.util.ArrayList;
import java.util.List;

public class BigField {
    
    private SmallField[][] Fields;
    private List<Integer> legalEntries = new ArrayList<>();


    public BigField(int size){
        Fields = new SmallField[size][size];

        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                Fields[j][i] = new SmallField(size);
            }
        }
    }

}
