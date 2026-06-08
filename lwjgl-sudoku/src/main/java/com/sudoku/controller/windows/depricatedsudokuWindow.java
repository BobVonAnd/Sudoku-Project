package com.sudoku.controller.windows;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import com.sudoku.model.SudokuBoard;
import com.sudoku.controller.Window;
import com.sudoku.controller.WindowInterface;
import com.sudoku.controller.WindowManager;
import com.sudoku.model.Field;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class depricatedsudokuWindow extends Window {

	// The window handle
	private long window;
	private WindowManager wm;

	public depricatedsudokuWindow(WindowManager wm) {
		super(wm);
	}

	public void run(SudokuBoard sudokuBoard) {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");

		init(sudokuBoard);
		loop(sudokuBoard);

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	private void init(SudokuBoard sudokuBoard) {
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

		// Create the window
		window = glfwCreateWindow(900, 700, "Hello World!", NULL, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");
		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		});
		glfwSetMouseButtonCallback(window, (win, button, action, mods) -> {
		if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
			double[] xpos = new double[1];
			double[] ypos = new double[1];

			glfwGetCursorPos(win, xpos, ypos);

			int[] width = new int[1];
			int[] height = new int[1];
			glfwGetWindowSize(win, width, height);

			double x = (xpos[0] / width[0]) ;
			double y = (ypos[0] / height[0]);

			int[] sudokuCoords = translateCoordinatesToFieldCoordinates(x, y, sudokuBoard.getSize());
			Field h = sudokuBoard.getSingleField(sudokuCoords[0], sudokuCoords[1]);
			System.out.println("window size = " + width[0] + " " + height[0]);
			System.out.println("Coordinates of cursor= " + xpos[0] + " " + ypos[0]);
			System.out.println("Coordinates corresponding to window size= " + x+" "+y);
			System.out.println("Col + rows of sudoku= " + sudokuCoords[1] + " " + sudokuCoords[0]);
			h.changeColour(1, 0, 1);
		}
		});

		
		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
				window,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);
	}

	private void loop(SudokuBoard sudokuBoard) {
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		// Set the clear color
		glClearColor(1.0f, 1.0f, 1.0f, 0.0f);

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.

		double size = 0.9 / sudokuBoard.getSize();
		double gap = size * 0.1; // spacing

		double half = size - gap;
		while (!glfwWindowShouldClose(window)) {

			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			for (Field f : sudokuBoard.getFields()) {
			double row = f.getCoordinates()[0];
			double col = f.getCoordinates()[1];

			double x = -0.9 + col * size * 2 + size;
			double y = 0.7 - row * size * 2 + size;

				glBegin(GL_QUADS);

				glColor3d(f.getRed(), f.getGreen(), f.getBlue());
				glVertex2d(x - half, y - half-size/100); //Bottom left
				glVertex2d(x + half, y - half-size/100); //Bottom right
				glVertex2d(x + half, y + half); //Top Right
				glVertex2d(x - half, y + half); //Top Left

				glEnd();
			}

			glfwSwapBuffers(window);
			glfwPollEvents();
		}
	}
	public int[] translateCoordinatesToFieldCoordinates(double x, double y, int gridSize) {
		int[] coords = new int[2];

		// Convert from [0,1] → [-0.9, 0.9]
		double glX = x * 1.8 - 0.9;
		double glY = y * 1.8 - 0.9;

		double cellSize = (1.8) / gridSize;

		int col = (int)((glX + 0.9) / cellSize);
		int row = (int)((glY + 0.9) / cellSize);

		// Clamp (avoid out-of-bounds)
		col = Math.max(0, Math.min(gridSize - 1, col));
		row = Math.max(0, Math.min(gridSize - 1, row));

		coords[0] = row;
		coords[1] = col;

		return coords;
	}

}