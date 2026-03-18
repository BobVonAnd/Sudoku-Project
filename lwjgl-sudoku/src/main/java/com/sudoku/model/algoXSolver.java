package com.sudoku.model;
import java.util.ArrayList;

public class algoXSolver {
    private ColumnNode root;
    private ArrayList<Node> solution = new ArrayList<>();

    public void algoXManager(SudokuBoard sudokuBoard){
        int size = sudokuBoard.getSize();
        ArrayList<Node> solution = new ArrayList<Node>();
        ColumnNode root = initializeNodes(size);

        solution = search(root, size, solution);
        for (Node solNode : solution){
            System.out.println(solNode.column.label);
        }

    }
    public ColumnNode initializeNodes(int size){
        int constraints = 4;
        int cols = size * size * constraints;
        root = new ColumnNode();
        root.left = root;
        root.right = root;
        ColumnNode prev = root;
        ColumnNode[] columnNodes = new ColumnNode[cols];
        for (int h = 0; h<cols; h++){
            ColumnNode c = new ColumnNode();
            prev.right = c;
            c.left = prev;
            prev = c;
            columnNodes[h] = c;

            c.up = c;
            c.down = c;

            c.label = "C" + h;
        }
        prev.right = root;
        root.left = prev;
        //Start with first column
        int boxSize = (int)Math.sqrt(size);
        for (int i = 0; i < size; i++){
            for (int j = 0; j<size; j++){
                for (int n = 0; n < size ; n++){
                    int box = (i/boxSize)*boxSize + (j/boxSize);
                    //cell constraint
                    Node cellNode = new Node();
                    Node rowNode = new Node();
                    Node colNode = new Node();
                    Node boxNode = new Node();

                    // vertical insert
                    appendToColumn(columnNodes[i*size + j], cellNode);
                    appendToColumn(columnNodes[size*size+i*size+n], rowNode);
                    appendToColumn(columnNodes[2*size*size + j*size + n], colNode);
                    appendToColumn(columnNodes[3*size*size + box*size + n], boxNode);

                    // horizontal circular link
                    cellNode.right = rowNode;
                    rowNode.right = colNode;
                    colNode.right = boxNode;
                    boxNode.right = cellNode;

                    cellNode.left = boxNode;
                    rowNode.left = cellNode;
                    colNode.left = rowNode;
                    boxNode.left = colNode;
                }
            }
        }
        return root;
    }
    public void appendToColumn(ColumnNode col, Node newNode) {
    newNode.column = col;

    newNode.down = col;
    newNode.up = col.up;
    col.up.down = newNode;
    col.up = newNode;

    col.size++;
}

    public ArrayList<Node> search(ColumnNode root, int k, ArrayList<Node> solution){
        //If the matrix is empty, we have found a solution
        if (root.right == root ){
            return solution;
        }
        else {
            //Start with the column right of the root
            ColumnNode columnNode = (ColumnNode) root.right;
            //Go down into the matrix
            Node firstNode = columnNode.down;
            //While the node we went into isn't 
            while (firstNode != columnNode){
                solution.add(firstNode);
                Node rightNode = firstNode.right;
                while (rightNode != firstNode){
                    cover(rightNode.column);
                    rightNode = rightNode.right;
                }
                search(root, k+1, solution);
                firstNode = solution.get(k);
                columnNode = firstNode.column;
                rightNode = rightNode.left;
                while (rightNode != firstNode){
                    uncover(rightNode.column);
                    rightNode = rightNode.left;
                }
                firstNode = firstNode.down;
            }
            uncover(columnNode);
        }
        return solution;
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
    //Solution just using sparseMatrix


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
