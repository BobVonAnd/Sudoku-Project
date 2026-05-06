package com.sudoku;

import com.sudoku.view.Window;


public class App {


	 

		SudokuBoard sudokuBoard = new SudokuBoard(9);
		// sudokuBoard.populate(1);
		sudokuBoard.solve();
		TerminalView terminalView = new TerminalView(sudokuBoard);
		terminalView.printBoard();

		window.run(sudokuBoard);
	}
	
	
}