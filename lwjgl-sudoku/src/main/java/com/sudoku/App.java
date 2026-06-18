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
		
		mainMenuWindow window = new mainMenuWindow(wm,width, height);
		algoXSolver xSolver = new algoXSolver();
		long startTime = System.nanoTime();
		SudokuBoard sudokuBoard = xSolver.algoXCreateUnique(9, 58);
		if (xSolver.algoXIsUnique(sudokuBoard)){
			System.out.println("I am unique");
		}
		long endTime = System.nanoTime();
		long totalTime = (endTime-startTime)/1000000;
		System.out.println("It took: " + totalTime + "ms");
		// sudokuBoard.populate(1);
		// sudokuBoard.solve();
		TerminalView terminalView = new TerminalView(sudokuBoard);
		terminalView.printBoard();

		// wm.run();
	}
	
}