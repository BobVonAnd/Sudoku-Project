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
    long seed = System.currentTimeMillis();
    private Random random = new Random(seed);
    private Node[] rowHeads;
    private int recursiveChecks;
    private static final int MAX_RECURSIVE_CHECKS = 500;
    public boolean isInvalid = false;

    public void algoXManager(SudokuBoard sudokuBoard){
        //We initialize the size of the board and the solution arraylist and the nodes based on the size of the sudoku
        int size = sudokuBoard.getSize();
        solution = new ArrayList<>();
        root = initializeNodes(size);
        //We read the fields already in the sudokuBoard into the ArrayList as Nodes and then cover them in the linked list 
        solution = readSudokuBoardToNodes(root, sudokuBoard, solution);
        ArrayList<Node> coveredClues = coverCluesInRoot(root, solution);
        //We start the solving of the sudoku using the search method. 
        solution = search(root, solution);
        //Each node of the solution has the coordinates of it's corresponding field and the value attached. 
        //We can then use the readNodesToBoard method to update the sudoku board's fields and then we uncover the nodes we covered
        readNodesToBoard(sudokuBoard, solution);
        uncoverCluesInRoot(coveredClues);
    }

    //This methods constructs our linked list
    public ColumnNode initializeNodes(int size){
        //The amount of constraints of a sudoku is constant but the amount of columns is depended on the size of the sudoku
        int constraints = 4;
        int cols = size * size * constraints;

        //We create the first ColumnNode which is the root of our linked list. We update it's neighbors to be itself and 
        //prepare to start creating a adding columnNodes to the linked List
        root = new ColumnNode();
        root.left = root;
        root.right = root;
        ColumnNode prev = root;
        ColumnNode[] columnNodes = new ColumnNode[cols];
        //We create size*size*4 columnNodes and link them to each other so they form a linked list.
        for (int h = 0; h<cols; h++){
            ColumnNode c = new ColumnNode();
            prev.right = c;
            c.left = prev;
            prev = c;
            columnNodes[h] = c;

            c.up = c;
            c.down = c;
            //The label is not something that is used by the algorithm but has helped us in debugging. 
            c.label = "C" + h;
        }
        prev.right = root;
        root.left = prev;

        //Now we create the rows that will fill the columns. Rowheads is used so that shuffling nodes later becomes much easier
        int boxSize = (int)Math.sqrt(size);
        rowHeads = new Node[size*size*size];
        for (int i = 0; i < size; i++){
            for (int j = 0; j<size; j++){
                for (int n = 0; n < size ; n++){
                    //box is the box that the node will be a part of
                    int box = (i/boxSize)*boxSize + (j/boxSize);
                    //We create the four row Nodes representing each their constraint with the same coordinates because they
                    //symbolize the same number in the same position
                    Node cellNode = new Node();
                    cellNode.setNodeCoordinates(i, j, n);
                    Node rowNode = new Node();
                    rowNode.setNodeCoordinates(i, j, n);
                    Node colNode = new Node();
                    colNode.setNodeCoordinates(i, j, n);
                    Node boxNode = new Node();
                    boxNode.setNodeCoordinates(i, j, n);
                    //We take the cellNode which is our row heads
                    int rowIndex = i * size * size + j * size + n;
                    rowHeads[rowIndex] = cellNode;

                    //We insert the nodes into their columns, incremented by size*size because each section of the size*size
                    //columns represented one type of constraint. 
                    appendToColumn(columnNodes[i*size + j], cellNode);
                    appendToColumn(columnNodes[size*size+i*size+n], rowNode);
                    appendToColumn(columnNodes[2*size*size + j*size + n], colNode);
                    appendToColumn(columnNodes[3*size*size + box*size + n], boxNode);

                    //We link the nodes to each other first their right neighbors and then their left neighbors
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
        //We return the root, so when we want to use the linked list, we just know that we should start from the root
        return root;
    }
    //Appends the node to the column
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
            //Start with column that has fewest nodes but if there is a column with no nodes we are at a dead end
            ColumnNode columnNode = getBestColumnNode(root);
            if (columnNode.size == 0){
                return null;
            }
            //Cover the first column to start
            cover(columnNode);
            //Go down into the linked list
            Node firstNode = columnNode.down;
            //While the node we went into isn't the column header
            while (firstNode != columnNode){
                //We try to add the node to the solution
                solution.add(firstNode);
                Node rightNode = firstNode.right;
                //We cover the entire row of the node
                while (rightNode != firstNode){
                    cover(rightNode.column);
                    rightNode = rightNode.right;
                }
                //We search for a solution in the rest of the linked list
                ArrayList<Node> result = search(root, solution);
                //We uncover the row of the node we added
                for (Node j = firstNode.left; j != firstNode; j = j.left){
                    uncover(j.column);
                }
                //If we have a solution, we just uncover the node and it's column headed and return the solution
                if (result != null) {
                    uncover(columnNode);
                    return result;
                }
                //If we are here we had no solution so the node must be removed from the solution
                solution.remove(solution.size() - 1);
                //We go down to the next row and try again
                firstNode = firstNode.down;
            }
            //If we are here we have failed to find a solution, we uncover the column and return null
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
            //Go to the right of the node under to remove the row through the columns
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
    //Used to find the node in the root.
    public Node findRowInSolution(ColumnNode root ,int i, int j, int n){
        //Start with the column right of the root
        ColumnNode columnNode = (ColumnNode) root.right;
        while (columnNode != root){
            //we go down through the column
            Node row = columnNode.down;
            while (row != columnNode){
                //If it matches we return it otherwise we continue searching
                if(row.getRow() == i && row.getCol() == j && row.getNum() == n){
                    return row;
                }
                row = row.down;
            }
            //Next column
            columnNode =(ColumnNode) columnNode.right;
        }
        //We return null if we couldn't find it. 
        return null;
    }
    //We look for the columnNode with fewest node inside
    public ColumnNode getBestColumnNode(ColumnNode root) {
        //We go to the node right of the root
        ColumnNode current = (ColumnNode) root.right;
        ColumnNode best = current;
        //We check the size of the column, if the column is size 0 or 1 we return it immeadiately
        while (current != root) {
            if (current.size == 0) {
                return current;
            }
            //We update the best node so that we remember the node that has fewest Nodes
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
    //Same code as search but instead of return a solution it increments a counter
    public void algoXUniqueTest(ColumnNode root, ArrayList<Node> solution){
        if (solutionCounter > 1){
            return;
        }
        if (root.right == root ){
            solutionCounter++;
            return;
        }
        else {
            ColumnNode columnNode = getBestColumnNode(root);
            if (columnNode.size == 0){
                return;
            }
            cover(columnNode);
            Node firstNode = columnNode.down;
            while (firstNode != columnNode){
                solution.add(firstNode);
                Node rightNode = firstNode.right;
                while (rightNode != firstNode){
                    cover(rightNode.column);
                    rightNode = rightNode.right;
                }
                algoXUniqueTest(root, solution);
                for (Node j = firstNode.left; j != firstNode; j = j.left){
                    uncover(j.column);
                }
                solution.remove(solution.size() - 1);
                if (solutionCounter > 1){
                    uncover(columnNode);
                    return;
                }
                firstNode = firstNode.down;
            }
            uncover(columnNode);
        }
    }
    //Method that return true if the sudoku board is unique
    public boolean algoXIsUnique(SudokuBoard sudokuBoard){
        //is invalid is used to check for sudokuBoards that break the rules of sudoku or have unsolvable boards
        isInvalid = false;
        solutionCounter = 0;
        solution = new ArrayList<>();
        ArrayList<Node> coveredClues = new ArrayList<>();
        int size = sudokuBoard.getSize();
        root = initializeNodes(size);
        solution = readSudokuBoardToNodes(root, sudokuBoard, solution);
        //We try to cover the nodes, it is here that the code will get an error if the sudokuBoard is invalid
        try {
            coveredClues = coverCluesInRoot(root, solution);
        } catch (Exception E){
            isInvalid = true;
            solutionCounter = 0;
            return false;
        }
        //We run the uniqueness test and uncover the clues. The sudoku is only unique if it has one solution.
        algoXUniqueTest(root, solution);
        uncoverCluesInRoot(coveredClues);
        return solutionCounter == 1;
    }
    //This does the same as algoXIsUnique but here it just takes an arrayList of nodes
    public boolean hasUniqueSolution(ColumnNode root, ArrayList<Node> clues){
        solutionCounter = 0;
        ArrayList<Node> coveredClues = coverCluesInRoot(root, clues);
        algoXUniqueTest(root, clues);
        uncoverCluesInRoot(coveredClues);
        return solutionCounter == 1;
    }
    //seed for random sudoku generation
    public long getSeed() {
        return seed;
    }
    //update the seed
    public long newSeed() {
        seed = System.currentTimeMillis();
        random = new Random(seed);
        return seed;
    }
    //This lets you call algoXCreateUnique where you can set the seed
    public SudokuBoard algoXCreateUnique(int size, int fieldsToRemove, long seed) {
        random = new Random(seed);
        return algoXCreateUnique(size, fieldsToRemove);
    }
    //This method creates a sudoku board with a unique puzzle
    public SudokuBoard algoXCreateUnique(int size, int fieldsToRemove){
        int removed = 0;
        int counter = 0;
        boolean recursiveAttempt = false;
        //We create a random board using shufflenodes to create random placements of rows in the linked list
        SudokuBoard sudokuBoard = new SudokuBoard(size);
        solution = new ArrayList<>();
        root = initializeNodes(size);
        shuffleNodes(root, size);
        search(root, solution);
        java.util.Collections.shuffle(solution,random);
        //We start removing fields
        while (removed < fieldsToRemove){
            //We check if we should do recursion, the limits have been decided based on the limits of the greedy algorithm
            if ((!recursiveAttempt) && ( (fieldsToRemove > 56 && removed > 56 && size == 9) || (fieldsToRemove > 150 && removed > 150 && size == 16) 
            || (fieldsToRemove > 270 && removed > 270 && size == 25 )
            || (fieldsToRemove > 350 && removed > 350 && size == 36)
            )){
                //We check if we can recursively reach the desired amount of field removed
                recursiveAttempt = true;                

                ArrayList<Node> clues = new ArrayList<>(solution);
                ArrayList<Node> candidates = new ArrayList<>(solution);

                boolean success = removeRecursive(root, clues, candidates, fieldsToRemove-removed);

                if (success){
                    readNodesToBoard(sudokuBoard, clues);
                    return sudokuBoard;
                }
            }
            //If we have tried a recursive attempt and failed or we have tried the greedy algorithm enough times we generate a 
            //new random sudoku
            if (counter > size*size*3 || recursiveAttempt){
                solution = new ArrayList<>();
                root = initializeNodes(size);

                shuffleNodes(root, size);
                search(root, solution);
                java.util.Collections.shuffle(solution,random);

                removed = 0;
                counter = 0;
                recursiveChecks = 0;
                recursiveAttempt = false;
                continue;
            }
            //We remove a candidate from the shuffled arrayList and check if the sudoku is still unique afterwards
            counter ++;
            solutionCounter = 0;
            Node candidate = solution.get(solution.size()-1);
            solution.remove(solution.size()-1);

            ArrayList<Node> coveredNodes = coverCluesInRoot(root, solution);
            ArrayList<Node> tempSolution = new ArrayList<>(solution);

            algoXUniqueTest(root, tempSolution);
            uncoverCluesInRoot(coveredNodes);
            //If it is unique we increment removed and we let the node stay removed, otherwise we add it back and rotate the arrayList
            if (solutionCounter == 1){
                removed++;
            }
            else {
                solution.add(candidate);
                java.util.Collections.rotate(solution, 1);
            }
            
        }
        readNodesToBoard(sudokuBoard, solution);
        return sudokuBoard;
    }


    //The recursive removal function, it checks if it can find a path that is fieldLeftToRemove long of removals that keep the
    //sudoku unique
    public boolean removeRecursive(ColumnNode root, ArrayList<Node> clues, ArrayList<Node> candidates, int fieldLeftToRemove){
        //We have succeded if fieldLeftToRemove is 0
        if (fieldLeftToRemove == 0){
            return true;
        }
        //If there are no more candidates to try or it has checked candidates over max_recursive_checks times it has failed
        if (candidates.isEmpty()) {
            return false;
        }
        if (recursiveChecks >= MAX_RECURSIVE_CHECKS){
            return false;
        }
        ArrayList<Node> shuffledCandidates = new ArrayList<>(candidates);
        java.util.Collections.shuffle(shuffledCandidates, random);
        //We loop through the nodes that are left to be removed
        for (Node candidate : shuffledCandidates) {
            recursiveChecks++;
            clues.remove(candidate);
            //We remove the candidate node and check if the still has a unique solution
            if (hasUniqueSolution(root, clues)){
                //if it does, we remove the candidate from the list of candidates and recurse one level deeper
                ArrayList<Node> otherCandidates = new ArrayList<>(candidates);
                otherCandidates.remove(candidate);

                if (removeRecursive(root, clues, otherCandidates, fieldLeftToRemove-1)){
                    //If the steps previously have returned true, this removal is part of a path that was long enough
                    return true;
                }
            }
            //This removal failed and is readded to the clues
            clues.add(candidate);
        }
        return false;
    }

    //This method is used to shuffle the nodes around while holding them together as rows. This enables random sudoku generation
    public void shuffleNodes(ColumnNode root, int size){
        Node[][] rowNodes = new Node[size * size * size][4];
        //We detach all nodes from their columns
        for (int i = 0; i < rowHeads.length; i++) {
            Node currentNode = rowHeads[i];
            for (int j = 0; j < 4; j++) {
                rowNodes[i][j] = detachFromColumn(currentNode);
                currentNode = currentNode.right;
            }
        }
        //We shuffle the rows and re-attach them to their columns
        shuffleRows(rowNodes);
        for (int i = 0; i < rowHeads.length; i++){
            for (int j = 0; j < 4; j++){
                appendToColumn(rowNodes[i][j].column, rowNodes[i][j]);
            }
        }
    }
    //This methods shuffles the rows
    public void shuffleRows(Node[][] rowNodes) {
        List<Node[]> rows = Arrays.asList(rowNodes);
        Collections.shuffle(rows, random);
    }
    //This method detaches nodes from their columns
    public Node detachFromColumn(Node node) {
        node.up.down = node.down;
        node.down.up = node.up;

        node.column.size--;

        return node;
    }
    //This reads a sudoku board into the arrayList of nodes called clues
    public ArrayList<Node> readSudokuBoardToNodes(ColumnNode root, SudokuBoard sudokuBoard, ArrayList<Node> clues){
        Field[][] wholeBoard = sudokuBoard.getWholeBoard();
        for (Field[] row : wholeBoard){
            for (Field f : row){
                if (f.getValue() != 0){
                    int rowCoord = f.getCoordinates()[0];
                    int columnCoord = f.getCoordinates()[1];
                    int value = f.getValue()-1;
                    Node node = findRowInSolution(root, rowCoord, columnCoord, value);
                    
                    clues.add(node);   
                    
                }
            }
        }
        return clues;
    }
    //we find the nodes in the ArrayList in the linked list and cover them. This treats them like they have been found by algorithm X
    public ArrayList<Node> coverCluesInRoot(ColumnNode root, ArrayList<Node> clues) {
        ArrayList<Node> coveredNodes = new ArrayList<>();
        for (Node clue : clues) {
            Node node = findRowInSolution(
                root,
                clue.getRow(),
                clue.getCol(),
                clue.getNum()
            );
                coveredNodes.add(node);

                cover(node.column);

                for (Node temp = node.right; temp != node; temp = temp.right) {
                    cover(temp.column);
                }
            
        }

        return coveredNodes;
    }
    //Does the opposite of coverCluesInRoot, for each node in coveredNodes it reattaches it to it's neighbor in the linked list
   public void uncoverCluesInRoot(ArrayList<Node> coveredNodes) {
        for (int i = coveredNodes.size() - 1; i >= 0; i--) {
            Node node = coveredNodes.get(i);

            for (Node temp = node.left; temp != node; temp = temp.left) {
                uncover(temp.column);
            }

            uncover(node.column);
        }
    }
    //This reads the Nodes of an ArrayList of nodes into their corresponding places and values in a sudoku board
    public void readNodesToBoard(SudokuBoard sudokuBoard, ArrayList<Node> solution){
        if (solution == null){
        }
        else {
            for (Node n : solution){
                int i = n.getRow();
                int j = n.getCol();
                int value = n.getNum() + 1; //We add one to the value as the int n is 0 indexed
                //Then we update the sudokuboards field
                sudokuBoard.changeField( i, j , value);
            }
        }
    }  
    //get the invalid boolean
    public boolean getIsInvalid(){
        return this.isInvalid;
    }
    //get the solution counter, which is either 0, 1 or 2 where 2 means that there are at least 2 solutions
    public int getSolutionCounter() {
        return solutionCounter;
    }

}