package com.sudoku.model;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

public class algoXSolver {
    private ColumnNode root;
    private ArrayList<Node> solution = new ArrayList<>();
    private int solutionCounter = 0;
    private final Random random = new Random();
    private Node[] rowHeads;

    public void algoXManager(SudokuBoard sudokuBoard){
        //We initialize the size of the board and the solution arraylist and the nodes based on the size of the sudoku
        int size = sudokuBoard.getSize();
        solution = new ArrayList<>();
        root = initializeNodes(size);
        //Then we loop through the sudoku board and get the values that are already present on the board. These we add to the solution
        readCluesIntoRoot(sudokuBoard, solution);

        //We start the solving of the sudoku using the search method. 
        long startTime = System.nanoTime();
        solution = search(root, solution);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        System.out.println("The algo itself took "+ duration + "ms");
        //Each node of the solution has the coordinates of it's corresponding field and the value attached. 
        readNodesToBoard(sudokuBoard, solution);
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
        rowHeads = new Node[size*size*size];
        for (int i = 0; i < size; i++){
            for (int j = 0; j<size; j++){
                for (int n = 0; n < size ; n++){
                    int box = (i/boxSize)*boxSize + (j/boxSize);
                    //cell constraint
                    Node cellNode = new Node();
                    cellNode.setNodeCoordinates(i, j, n);

                    int rowIndex = i * size * size + j * size + n;
                    
                    rowHeads[rowIndex] = cellNode;

                    Node rowNode = new Node();
                    rowNode.setNodeCoordinates(i, j, n);
                    Node colNode = new Node();
                    colNode.setNodeCoordinates(i, j, n);
                    Node boxNode = new Node();
                    boxNode.setNodeCoordinates(i, j, n);

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

    public ArrayList<Node> search(ColumnNode root, ArrayList<Node> solution){
        //If the matrix is empty, we have found a solution
        if (root.right == root ){
            return new ArrayList<>(solution);
        }
        else {
            //Start with the column right of the root
            ColumnNode columnNode = getBestColumnNode(root);
            if (columnNode.size == 0){
                return null;
            }
            //Cover the first column to start
            cover(columnNode);
            //Go down into the matrix
            Node firstNode = columnNode.down;
            //While the node we went into isn't the original node
            while (firstNode != columnNode){
                //We try to add the row to the solution
                solution.add(firstNode);
                Node rightNode = firstNode.right;
                //We cover the entire row
                while (rightNode != firstNode){
                    cover(rightNode.column);
                    rightNode = rightNode.right;
                }
                //We search for a solution one depth further in
                ArrayList<Node> result = search(root, solution);
                //As we are looking for one solution we exit if we have gotten a solution
                if (result != null) {
                    uncover(columnNode);
                    return result;
                }
                solution.remove(solution.size() - 1);
                //We get ready to uncover the nodes that were covered
                for (Node j = firstNode.left; j != firstNode; j = j.left){
                    uncover(j.column);
                }
                //We go down to the next row
                firstNode = firstNode.down;
            }
            uncover(columnNode);
        }
        return null;
    }
    //Cover a node to test the system without the node
    public void cover(ColumnNode node){
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
                downRow.column.size --;
                downRow = downRow.right;
            }
            //Go to next node as it is circular, at some point it will reach the original node
            down = down.down;
        }
    }
    //Do the reverse of cover
    public void uncover(ColumnNode node){
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

    public Node findRowInSolution(int i, int j, int n){
        //Start with the column right of the root
        ColumnNode columnNode = (ColumnNode) root.right;
        while (columnNode != root){
            Node row = columnNode.down;
            while (row != columnNode){
                if(row.getRow() == i && row.getCol() == j && row.getNum() == n){
                    return row;
                }
                row = row.down;
            }
            columnNode =(ColumnNode) columnNode.right;
        }
        return null;
    }
    public ColumnNode getBestColumnNode(ColumnNode root) {
        ColumnNode current = (ColumnNode) root.right;
        ColumnNode best = current;

        while (current != root) {
            if (current.size == 0) {
                return current;
            }
            if (current.size < best.size) {
                best = current;
                if (best.size == 1) {
                    return best;
                }
            }
            current = (ColumnNode) current.right;
        }
        return best;
    }

    public void algoXUniqueTest(ColumnNode root, ArrayList<Node> solution){
        if (solutionCounter > 1){
            return;
        }
        if (root.right == root ){
            solutionCounter++;
            return;
        }
        else {
            //Start with the column right of the root
            ColumnNode columnNode = getBestColumnNode(root);
            //Cover the first column to start
            cover(columnNode);
            //Go down into the matrix
            Node firstNode = columnNode.down;
            //While the node we went into isn't the original node
            while (firstNode != columnNode){
                //We try to add the row to the solution
                solution.add(firstNode);
                Node rightNode = firstNode.right;
                //We cover the entire row
                while (rightNode != firstNode){
                    cover(rightNode.column);
                    rightNode = rightNode.right;
                }
                //We search for a solution one depth further in
                algoXUniqueTest(root, solution);
                //We uncover the nodes that were covered 
                for (Node j = firstNode.left; j != firstNode; j = j.left){
                    uncover(j.column);
                }
                solution.remove(solution.size() - 1);
                if (solutionCounter > 1){
                    break;
                }
                //We go down to the next row and continue looking for multiple solutions
                firstNode = firstNode.down;
            }
            uncover(columnNode);
        }
    }
    public boolean algoXIsUnique(SudokuBoard sudokuBoard){
        solutionCounter = 0;
        solution = new ArrayList<>();
        int size = sudokuBoard.getSize();
        root = initializeNodes(size);
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                int value = sudokuBoard.getSingleField(i,j).getValue();
                if (value != 0 ){
                    int n = value - 1;
                    Node node = findRowInSolution(i, j, n);
                    solution.add(node);

                    //Now covering the node so the solver is more efficient and so the solver actually solves the sudoku given
                    Node tempNode = node;
                    do {
                        cover(tempNode.column);
                        tempNode = tempNode.right;
                    } while (tempNode != node);
                }
            }
        }
        algoXUniqueTest(root, solution);
        return solutionCounter == 1;
    }
    public SudokuBoard algoXCreateUnique(int size, int fieldsToRemove){
        int removed = 0;
        int counter = 0;
        int totalcounter = 0;
        int startCounter = 0;

        SudokuBoard sudokuBoard = new SudokuBoard(size);
        solution = new ArrayList<>();
        root = initializeNodes(size);
        shuffleNodes(root, size);
        search(root, solution);
        java.util.Collections.shuffle(solution);
        while (removed < fieldsToRemove){
            while (removed < size*4){
                solution.remove(solution.size()-1);
                removed++;
            }
            if (counter > size*size*4){
                totalcounter++;
                System.out.println("Trying new sudoku number " + totalcounter);
                solution = new ArrayList<>();
                root = initializeNodes(size);
                shuffleNodes(root, size);
                search(root, solution);
                java.util.Collections.shuffle(solution);
                removed = 0;
                counter = 0;
                startCounter = 0;
            }
            counter ++;
            solutionCounter = 0;
            Node candidate = solution.get(solution.size()-1);
            solution.remove(solution.size()-1);

            root = initializeNodes(size);
            coverCluesInRoot(root, solution);
            algoXUniqueTest(root, solution);
            if (solutionCounter == 1){
                removed++;
            }
            else {
                solution.add(candidate);
                java.util.Collections.shuffle(solution);
            }
        }
        readNodesToBoard(sudokuBoard, solution);
        return sudokuBoard;
    }
    public void shuffleNodes(ColumnNode root, int size){
        Node[][] rowNodes = new Node[size * size * size][4];

        for (int i = 0; i < rowHeads.length; i++) {
            Node currentNode = rowHeads[i];
            for (int j = 0; j < 4; j++) {
                rowNodes[i][j] = detachFromColumn(currentNode);
                currentNode = currentNode.right;
            }
        }
        shuffleRows(rowNodes);
        for (int i = 0; i < rowHeads.length; i++){
            for (int j = 0; j < 4; j++){
                appendToColumn(rowNodes[i][j].column, rowNodes[i][j]);
            }
        }
    }
    public void shuffleRows(Node[][] rowNodes) {
        List<Node[]> rows = Arrays.asList(rowNodes);
        Collections.shuffle(rows, random);
    }

    public Node detachFromColumn(Node node) {
        node.up.down = node.down;
        node.down.up = node.up;

        node.column.size--;

        return node;
    }
    public void coverCluesInRoot(ColumnNode root, ArrayList<Node> clues){
        for (Node clue : clues) {
            Node node = findRowInSolution(
                clue.getRow(),
                clue.getCol(),
                clue.getNum()
            );

            cover(node.column);

            for (Node temp = node.right; temp != node; temp = temp.right) {
                cover(temp.column);
            }
        }
    }
    public void readCluesIntoRoot(SudokuBoard sudokuBoard, ArrayList<Node> clues){
        int size = sudokuBoard.getSize();
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                int value = sudokuBoard.getSingleField(i,j).getValue();
                if (value != 0 ){
                    int n = value - 1;
                    Node node = findRowInSolution(i, j, n);
                    clues.add(node);

                    //Now covering the node so the solver is more efficient and so the solver actually solves the sudoku given
                    Node tempNode = node;
                    do {
                        cover(tempNode.column);
                        tempNode = tempNode.right;
                    } while (tempNode != node);
                }
            }
        }
    }
    public void readNodesToBoard(SudokuBoard sudokuBoard,ArrayList<Node> solution){
        for (Node n : solution){
            int i = n.getRow();
            int j = n.getCol();
            int value = n.getNum() + 1; //We add one to the value as the int n is 0 indexed
            //Then we update the sudokuboards field
            sudokuBoard.changeField( i, j , value);
        }
    }   
}
