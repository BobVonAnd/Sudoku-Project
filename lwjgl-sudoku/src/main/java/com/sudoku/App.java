package com.sudoku;

import com.sudoku.controller.WindowManager;
import com.sudoku.controller.windows.mainMenuWindow;
import com.sudoku.model.*;
import com.sudoku.view.*;


public class App {
	private static int width = 1280;
	private static int height = 720;
	// private static WindowManager wm = new WindowManager(width, height);
	public static void main(String[] args) {
		
		// mainMenuWindow window = new mainMenuWindow(wm, width, height);
		// wm.run();
		algoXSolver xSolver = new algoXSolver();
		long startTime = 0;
		long endtime = 0;
		long totalTime = 0;
		long totalTimeRecursion = 0;
		int attempts = 3;
		int fieldsToRemove = 59;
		int size = 9;
		long averageAttempt = 0;
		// for (int i = 0; i < attempts; i++){
		// 	startTime = System.currentTimeMillis();
		// 	xSolver.algoXCreateUnique(size, fieldsToRemove);
		// 	endtime = System.currentTimeMillis();
		// 	long withoutRecursion = (endtime-startTime)/1000;
		// 	totalTime = withoutRecursion + totalTime;
		// }
		// for (Integer i : xSolver.attempts){
		// 	averageAttempt = averageAttempt + i;
		// }
		// averageAttempt = averageAttempt / xSolver.attempts.size();
		// for (int j = 0; j < attempts; j++){
		// 	startTime = System.currentTimeMillis();
		// 	xSolver.algoXCreateUniqueRecursive(size, fieldsToRemove);
		// 	endtime = System.currentTimeMillis();
		// 	long withRecursion = (endtime-startTime)/1000;
		// 	totalTimeRecursion = totalTimeRecursion+withRecursion;
		// }
		SudokuBoard sudokuBoard = new SudokuBoard(9);
		xSolver.algoXManager(sudokuBoard);
		TerminalView terminalView = new TerminalView(sudokuBoard);
		terminalView.printBoard();
		totalTime = totalTime / attempts;
		totalTimeRecursion = totalTimeRecursion / attempts;

		int givens = size*size-fieldsToRemove;
		System.out.println("Time to generate a " + givens +  " givens sudoku without recursion " + totalTime + " miliseconds");
		System.out.println("The average attempt without recursion is: " + averageAttempt);
		System.out.println("Time to generate a " + givens +  " givens sudoku with recursion " + totalTimeRecursion + " miliseconds");
		 
	}
	
}