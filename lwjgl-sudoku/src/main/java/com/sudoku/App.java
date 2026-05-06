package com.sudoku;

import com.sudoku.view.Window;
import com.sudoku.model.SudokuBoard;
import com.sudoku.view.TerminalView;



public class App {
	public static void main(String[] args) {
		Window window = new Window();
		SudokuBoard sudokuBoard = new SudokuBoard(9);
		// sudokuBoard.populate(1);
		sudokuBoard.solve();
		TerminalView terminalView = new TerminalView(sudokuBoard);
		terminalView.printBoard();

		window.run();
	}
	
}