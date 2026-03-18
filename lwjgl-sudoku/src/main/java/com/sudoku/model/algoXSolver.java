package com.sudoku.model;
import java.util.ArrayList;

public class algoXSolver {
    private ColumnNode root;
    private ArrayList<Node> solution = new ArrayList<>();

    public void algoXManager(SudokuBoard sudokuBoard){
        int size = sudokuBoard.getSize();

        int[][] sparseMatrix = sparseMatrixCreate(size);

    }
    public void initializeNodes(int[][] sparseMatrix){
        int rows = sparseMatrix.length;
        int cols = sparseMatrix[0].length;
        root = new ColumnNode();
        ColumnNode prev = root;
        for (int h = 0; h<cols; h++){
            ColumnNode c = new ColumnNode();
            prev.right = c;
            c.left = prev;
            prev = c;
        }
        //Start with first column
        ColumnNode rightColumnNode =(ColumnNode) root.right;
        for (int i = 0; i < rows; i++){
            //Initialize the start of the new column
            Node lastColNode = rightColumnNode;
            for (int j = 0; j < cols; j++){
                if (sparseMatrix[i][j] == 1){
                    Node node = new Node();
                    node.up=lastColNode;
                    lastColNode.down = node;

                    lastColNode = node;
                }
            }
            rightColumnNode = (ColumnNode) rightColumnNode.right;
        }
        
    }
    //Cover a node to test the system without the node
    public void cover(Node node){
        //Removing the node from the system by assigning it's neighbours as neighbours
        node.right.left = node.left;
        node.left.right = node.right;
        //Go to the node under the current node to remove the row
        Node down = node.down;
        while (down != node){
            //Go to the right of the node under to remove the rows through the columns
            Node downRow = down.right;
            while (downRow != down){
                //Through the row called downRow, set the up and down neighbours to as neighbours to "remove" the row
                downRow.down.up = downRow.up;
                downRow.up.down = downRow.down;
                //Set columns size down by one and continue through the row
                downRow.column.size = downRow.column.size - 1;
                downRow = downRow.right;
            }
            //Go to next node as it is circular, at some point it will reach the original node
            down = node.down;
        }
    }
    //Do the reverse of cover
    public void uncover(Node node){
        //Go through the column going up
        Node upNode = node.up;
        while (upNode != node){
            //Go to the left to go through the row
            Node leftNode = upNode.left;
            while (leftNode != upNode){
                //We add the node back, so increase the column size and restore the nodes neighbours
                leftNode.column.size = leftNode.column.size + 1;
                leftNode.up.down = leftNode;
                leftNode.down.up = leftNode;
                //Continue going through the row
               leftNode = leftNode.left;
            }
            //Continue going through the column
            upNode = upNode.up;
        }
        //In the end restore the nodes neighbours.
        node.left.right = node;
        node.right.left = node;
    }
    public int[][] sparseMatrixCreate(int size){  
        int constraints = 4;
        int[][] sparseMatrix = new int[size*size*size][constraints*size*size];
        int rowIndex = 0;

        int boxSize = (int)Math.sqrt(size);
        for (int i = 0; i < size; i++){
            for (int j = 0; j<size; j++){
                for (int n = 0; n < size ; n++){

                    //cell constraint
                    sparseMatrix[rowIndex][i*size + j] = 1;

                    //row constraint
                    sparseMatrix[rowIndex][size*size+i*size+n] = 1;

                    //column constraint
                    sparseMatrix[rowIndex][2*size*size + j*size + n] = 1;

                    //Box constraint
                    int box = (i/boxSize)*boxSize + (j/boxSize);
                    sparseMatrix[rowIndex][3*size*size + box*size + n] = 1;

                    rowIndex++;
                }
            }
        }
        return sparseMatrix;
    }
}
