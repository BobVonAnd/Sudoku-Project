package com.sudoku;

import com.sudoku.controller.WindowManager;
import com.sudoku.model.SudokuBoard;
import com.sudoku.model.algoXSolver;
import com.sudoku.view.TerminalView;
import com.sudoku.view.Window;
//import com.sudoku.view.fonts.CreateFont;
import com.sudoku.view.sudokuWindow;


public class App {

	private static WindowManager wm = new WindowManager();
	public static void main(String[] args) {
		//CreateFont font = new CreateFont("Sudoku-Project/lwjgl-sudoku/assets/fonts/ARIAL.TTF", 512);
		
		sudokuWindow window = new sudokuWindow(wm);


		SudokuBoard sudokuBoard = new SudokuBoard(9);
		// sudokuBoard.populate(1);
		//sudokuBoard.solve();
		TerminalView terminalView = new TerminalView(sudokuBoard);
		terminalView.printBoard();

		window.run(sudokuBoard);
	}
	
}