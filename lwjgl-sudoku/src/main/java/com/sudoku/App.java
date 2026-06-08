package com.sudoku;

import com.sudoku.model.SudokuBoard;
import com.sudoku.view.TerminalView;
import com.sudoku.controller.WindowManager;
import com.sudoku.controller.windows.mainMenuWindow;
import com.sudoku.controller.windows.textWindow;


public class App {
	private static int width = 1280;
	private static int height = 720;
	private static WindowManager wm = new WindowManager(width, height);
	public static void main(String[] args) {
		mainMenuWindow window = new mainMenuWindow(wm,width, height);
		SudokuBoard sudokuBoard = new SudokuBoard(9);
		sudokuBoard.populate(1);
		sudokuBoard.solve();
		TerminalView terminalView = new TerminalView(sudokuBoard);
		terminalView.printBoard();

		wm.run();
	}
	
}