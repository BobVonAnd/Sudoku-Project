package com.sudoku;

import com.sudoku.controller.WindowManager;
import com.sudoku.controller.windows.mainMenuWindow;
import com.sudoku.model.SudokuBoard;
import com.sudoku.view.TerminalView;


public class App {
	private static int width = 1280;
	private static int height = 720;
	private static WindowManager wm = new WindowManager(width, height);
	public static void main(String[] args) {
		mainMenuWindow window = new mainMenuWindow(wm,width, height);
		wm.run();
	}
	
}