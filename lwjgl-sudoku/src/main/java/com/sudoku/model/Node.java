package com.sudoku.model;
import java.util.ArrayList;

public class Node {
    Node up;
    Node left;
    Node right;
    Node down;
    ColumnNode column;
    int i;
    int j;
    int n;

    public Node(){
        this.left = null;
        this.right = null;
        this.up = null;
        this.down = null;
        this.i = 0;
        this.j = 0;
        this.n = 0;
        ColumnNode column;
        
    }
    public void setNodeCoordinates(int i, int j, int n){
        this.i = i;
        this.j = j;
        this.n = n;
    }
    public int getRow(){
        return i;
    }
    public int getCol(){
        return j;
    }
    public int getNum(){
        return n;
    }
}
