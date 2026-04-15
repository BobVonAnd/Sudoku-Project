package com.sudoku;

import com.sudoku.model.SudokuBoard;
import com.sudoku.model.algoXSolver;
import com.sudoku.view.TerminalView;
import com.sudoku.view.Window;
//import com.sudoku.view.fonts.CreateFont;


public class App {

	public static void main(String[] args) {
		//CreateFont font = new CreateFont("Sudoku-Project/lwjgl-sudoku/assets/fonts/ARIAL.TTF", 512);
		
		// Window window = new Window();


		SudokuBoard sudokuBoard = new SudokuBoard(9);
		sudokuBoard.populate(1);
		sudokuBoard.solve();
		TerminalView terminalView = new TerminalView(sudokuBoard);
		terminalView.printBoard();

		// window.run();
	}
	
}