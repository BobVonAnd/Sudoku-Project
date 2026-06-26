package com.sudoku;

import com.sudoku.controller.WindowManager;
import com.sudoku.controller.windows.mainMenuWindow;


public class App {
	private static int width = 1280;
	private static int height = 720;
	private static WindowManager wm = new WindowManager(width, height);
	public static void main(String[] args) {
		mainMenuWindow window = new mainMenuWindow(wm, width, height); // initialize the first window, in this instance its main menu
		wm.run(); // run the window manager, aka start up the program
	}
	
}