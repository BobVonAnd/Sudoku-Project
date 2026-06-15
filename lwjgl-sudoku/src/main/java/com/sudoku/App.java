package com.sudoku;

import com.sudoku.model.SudokuBoard;
import com.sudoku.model.algoAnalyser;
import com.sudoku.view.TerminalView;

import java.util.ArrayList;

import com.sudoku.controller.WindowManager;
import com.sudoku.controller.windows.sudokuWindow;
import com.sudoku.controller.windows.textWindow;
import com.sudoku.model.Solver;
import com.sudoku.model.algoXSolver;
import com.sudoku.model.humanSolverBoardHelper;


public class App {
	//private static WindowManager wm = new WindowManager();
	private static Solver solver = new Solver();
	private static algoXSolver xSolver = new algoXSolver();
	private static humanSolverBoardHelper boardHelper = new humanSolverBoardHelper();
	public static void main(String[] args) {
		// textWindow window = new textWindow(wm);
		SudokuBoard sudokuBoard = new SudokuBoard(9);
		SudokuBoard sudokuBoard2 = new SudokuBoard(9);
		sudokuBoard.readIntoBoard(new int[][] {
			{0,8,3,0,2,0,0,9,0},
			{0,0,0,8,0,0,1,0,0},
			{0,2,9,3,0,0,0,0,8},

			{0,0,0,0,9,8,7,0,0},
			{0,7,0,0,0,0,0,6,0},
			{0,0,6,7,4,0,0,0,0},

			{3,0,0,0,0,6,9,8,0},
			{0,0,2,0,0,5,0,0,0},
			{0,1,0,0,3,0,5,4,0}
		});
		sudokuBoard2.readIntoBoard(new int[][] {
			{0,8,3,0,2,0,0,9,0},
			{0,0,0,8,0,0,1,0,0},
			{0,2,9,3,0,0,0,0,8},

			{0,0,0,0,9,8,7,0,0},
			{0,7,0,0,0,0,0,6,0},
			{0,0,6,7,4,0,0,0,0},

			{3,0,0,0,0,6,9,8,0},
			{0,0,2,0,0,5,0,0,0},
			{0,1,0,0,3,0,5,4,0}
		});
		boolean bool = xSolver.algoXIsUnique(sudokuBoard);
		long startTime = System.nanoTime();
		solver.solves(sudokuBoard2);
		long endTime = System.nanoTime();
		long totaltime = endTime-startTime;
		System.out.println("The human solver took " + totaltime + "ns");
		startTime = System.nanoTime();
		xSolver.algoXManager(sudokuBoard);
		endTime = System.nanoTime();
		long algoXtotaltime = endTime-startTime;
		System.out.println("The algoX solver took " + algoXtotaltime + "ns");
		System.out.println("Algox is " + totaltime / algoXtotaltime + " faster");
		TerminalView terminalView2 = new TerminalView(sudokuBoard2);
		TerminalView terminalView = new TerminalView(sudokuBoard);
		ArrayList<String> moves = solver.getMoves();
		for (String move : moves){
			System.out.println(move);
		}
		if (bool) {
			System.out.println("It is unique");
		}
		else {
			System.out.println("It is not unique");
		}
		terminalView2.printBoard();
		System.out.println("________________________________________");
		System.out.println("AlgoX:");
		terminalView.printBoard();
		if (boardHelper.sameBoardCheck(sudokuBoard, sudokuBoard2)){
			System.out.println("The sudokus are the same");
		}
		else{
			System.out.println("The sudokus are not the same");
		}
		algoAnalyser.algoAnalysisManager();
		// wm.run();
	}
	
}