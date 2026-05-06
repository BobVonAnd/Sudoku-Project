package com.sudoku;

import com.sudoku.model.SudokuBoard;
import com.sudoku.view.TerminalView;
import com.sudoku.controller.Window;
import com.sudoku.controller.WindowManager;


public class App {
	private static WindowManager wm = new WindowManager();
	public static void main(String[] args) {
		Window window = new Window(wm);
		SudokuBoard sudokuBoard = new SudokuBoard(9);
		// sudokuBoard.populate(1);
		sudokuBoard.solve();
		TerminalView terminalView = new TerminalView(sudokuBoard);
		terminalView.printBoard();

		window.run();
	}
	
}