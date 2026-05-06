package com.sudoku.view;

import com.sudoku.model.Field;
import com.sudoku.model.Solver;
import com.sudoku.model.SudokuBoard;
import com.sudoku.model.algoXSolver;

public class Scenes {
    private SudokuBoard sudokuBoard;
    
    public void meny(){
        //needs to be in a Button for Start Game
        initSudokuBoard();
    }



    public void playing(){
    
        
        //Shows board
        initButton();
    }

     public void initButton(){
        Button button;
			for (Field[] arrayField : sudokuBoard.getWholeBoard()) {
				for (Field field : arrayField) {
					int [] coordinates = field.getCoordinates();
					button = new Button(coordinates[0], coordinates[1]);
					button.rendering();
				}
			}
    }

    public void initSudokuBoard(){

        sudokuBoard = new SudokuBoard(9);
		//sudokuBoard.populate(1);
		long startTime = System.nanoTime();
		algoXSolver algoX = new algoXSolver(); 
		algoX.algoXManager(sudokuBoard);
		long endTime = System.nanoTime();
		long durationOfAlgoX = (endTime - startTime)/1000000;
	
		TerminalView terminalView = new TerminalView(sudokuBoard);
		terminalView.printBoard();
		
		System.out.println("The algoX took " + durationOfAlgoX + " miliseconds");


        // sudokuBoard = new SudokuBoard(9);
		// TerminalView terminalView = new TerminalView(sudokuBoard);
		// sudokuBoard.populate(1);
		// for (int i = 0; i<sudokuBoard.getSize(); i++){//Change method
		// 	for (int j = 0; j<sudokuBoard.getSize(); j++){
		// 		Field f = sudokuBoard.getSingleField(i, j);
		// 		sudokuBoard.makeEdges(f);
		// 		sudokuBoard.updateLegalEntriesOfField(f);
		// 	}
		// }
		// Solver solver = new Solver();
		// solver.solves(sudokuBoard);
		
		// terminalView.printBoard();
    }




    public void end(){

    }

}
