package com.sudoku;

import com.sudoku.model.SudokuBoard;
import com.sudoku.model.algoXSolver;
import com.sudoku.view.TerminalView;
import com.sudoku.view.Window;
import com.sudoku.view.fonts.CreateFont;

public class App {

	// The window handle
	private long window;
	private SudokuBoard sudokuBoard;

	public void run() {

		sudokuBoard = new SudokuBoard(9);
		sudokuBoard.populate(1);
		long startTime = System.nanoTime();
		sudokuBoard.solve();
		long endTime = System.nanoTime();
		long durationOfAlgoX = (endTime - startTime)/1000000;
		System.out.println("The algoX took " + durationOfAlgoX + " miliseconds");
		TerminalView terminalView = new TerminalView(sudokuBoard);
		terminalView.printBoard();
		System.out.println("Running with LWJGL v" + Version.getVersion() + "");

		init();
		loop();

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	private void init() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

public class App {

	public static void main(String[] args) {
		CreateFont font = new CreateFont("Sudoku-Project/lwjgl-sudoku/assets/fonts/ARIAL.TTF", 512);
		
		Window window = new Window();

			glfwSwapBuffers(window); // swap the color buffers
			glfwPollEvents();
		}
	}

	public static void main(String[] args) {
		
		new App().run();
		
	}
	
}