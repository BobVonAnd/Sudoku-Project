package com.sudoku;

import com.sudoku.controller.WindowManager;
import com.sudoku.controller.windows.mainMenuWindow;
import com.sudoku.model.SudokuBoard;
import com.sudoku.model.algoXSolver;
import com.sudoku.view.TerminalView;


public class App {
	private static int width = 1280;
	private static int height = 720;
	private static WindowManager wm = new WindowManager(width, height);
	public static void main(String[] args) {
		
		mainMenuWindow window = new mainMenuWindow(wm, width, height);
		wm.run();
		// algoXSolver xSolver = new algoXSolver();
		// long startTime;
		// int size = 36;
		// SudokuBoard sudokuBoard = new SudokuBoard(size);
		// int attempts = 10;
		// double times = 0;
		// for (int i = 0; i < attempts; i++){
		// 	startTime = System.nanoTime();
		// 	xSolver.newSeed();
		// 	sudokuBoard = xSolver.algoXCreateUnique(size,sudokuBoard.getFieldsToRemove(0.3));
		// 	if (xSolver.algoXIsUnique(sudokuBoard)){
		// 		System.out.println("I am unique");
		// 	}
		// 	double endTime = System.nanoTime();
		// 	double totalTime = (endTime-startTime)/1000000;
		// 	System.out.println("It took: " + totalTime + "ms");
		// 	times += totalTime;
		// 	// sudokuBoard.populate(1);
		// 	// sudokuBoard.solve();
		// 	TerminalView terminalView = new TerminalView(sudokuBoard);
		// 	terminalView.printBoard();
		// }
		// System.out.println("It took: " + times + "ms and " + times/attempts + "ms per attempt");
		 
	}
	
}