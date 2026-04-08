package com.sudoku;

import com.sudoku.model.SudokuBoard;
import com.sudoku.model.algoXSolver;
import com.sudoku.view.Fonts.CreateFont;
import com.sudoku.view.TerminalView;
import com.sudoku.view.Window;


public class App {

	public static void main(String[] args) {
		CreateFont font = new CreateFont(null, 32)
		
		Window window = new Window();


		SudokuBoard sudokuBoard = new SudokuBoard(9);
		sudokuBoard.populate();
		long startTime = System.nanoTime();
		algoXSolver algoX = new algoXSolver(); 
		algoX.algoXManager(sudokuBoard);
		long endTime = System.nanoTime();
		long durationOfAlgoX = (endTime - startTime)/1000000;
	
		TerminalView terminalView = new TerminalView(sudokuBoard);
		terminalView.printBoard();
		
		System.out.println("The algoX took " + durationOfAlgoX + " miliseconds");
		window.run();
	}
	
}