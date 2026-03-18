package com.sudoku.model;
import java.util.ArrayList;

public class Node {
    Node up;
    Node left;
    Node right;
    Node down;
    ColumnNode column;
    public void Node(){
        this.left = null;
        this.right = null;
        this.up = null;
        this.down = null;
        ColumnNode column;
        
    }
}
