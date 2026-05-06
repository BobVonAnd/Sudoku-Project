package com.sudoku;

import com.sudoku.model.SudokuBoard;
import com.sudoku.view.TerminalView;
import com.sudoku.controller.WindowManager;
import com.sudoku.controller.windows.textWindow;


public class App {
	private static WindowManager wm = new WindowManager();
	public static void main(String[] args) {
		textWindow window = new textWindow(wm);
		SudokuBoard sudokuBoard = new SudokuBoard(9);
		// sudokuBoard.populate(1);
		sudokuBoard.solve();
		TerminalView terminalView = new TerminalView(sudokuBoard);
		terminalView.printBoard();

		wm.run();
	}
	
}