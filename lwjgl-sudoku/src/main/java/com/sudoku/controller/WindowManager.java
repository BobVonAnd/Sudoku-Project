package com.sudoku.controller;
import com.sudoku.controller.windows.textWindow;

import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import org.lwjgl.Version;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;
import com.sudoku.controller.WindowManager;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class WindowManager {
    private WindowInterface activeWindow;
    private long window;
    private int h; // Initial
	private int w; // Initial
	private boolean fullscreen = false; // Initial

	public WindowManager(int width, int height) {
		this.h = height;
		this.w = width;
	}

    public void setActiveWindow(WindowInterface window) {
		this.activeWindow = window;
		if (this.window != NULL) { // GLFW window already exists
			this.activeWindow.create();
		}
	}
	
    public void init() {
	
		//error
		glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);

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
		// window = glfwCreateWindow(w, h, "Sudoku!", NULL, NULL);
		if (fullscreen) {
			GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			
			this.h = vidMode.height();
			this.w = vidMode.width();
		}
		
		window = glfwCreateWindow(
			this.w,
			this.h,
			"Sudoku!",
			fullscreen ? glfwGetPrimaryMonitor() : NULL,
			NULL
		);
		
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");

		// Key callbacks!
		glfwSetKeyCallback(window, (win, key, scancode, action, mods) -> {

			if (activeWindow != null) {
				activeWindow.keyCallback(key, scancode, action, mods);
			}

			if (key == GLFW_KEY_F11 && action == GLFW_PRESS) {
				toggleFullscreen();
			}

			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
				glfwSetWindowShouldClose(win, true);
			}
		});

		glfwSetCursorPosCallback(window, (win, xpos, ypos) -> {

			if (activeWindow != null) {
				activeWindow.cursorPosCallback(xpos, ypos);
			}

		});

		glfwSetMouseButtonCallback(window, (win, button, action, mods) -> {

			if (activeWindow != null) {
				activeWindow.mouseButtonCallback(button, action, mods);
			}

		});

		glfwSetWindowSizeCallback(window, (win, width, height) -> {

			this.w = width;
			this.h = height;

			glViewport(0, 0, width, height);

			if (activeWindow != null) {
				activeWindow.resizeCallback(width, height);
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
		GL.createCapabilities();
	}

    public void run() {
		System.out.println("Running with LWJGL v" + Version.getVersion() + "");
        init();
        activeWindow.create();
		loop();
	
		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
		
	}

    private void loop() {
        glViewport(0, 0, this.w, this.h);
		// Set the clear color
		glClearColor(1.0f, 1.0f, 1.0f, 0.0f);

		glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while ( !glfwWindowShouldClose(window) ) {

			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            ((Window) activeWindow).draw();
			activeWindow.step();
			
			glfwSwapBuffers(window); // swap the color buffers
			glfwPollEvents();
		}
	}

	public void toggleFullscreen() {
    fullscreen = !fullscreen;

    if (fullscreen) {
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        glfwSetWindowMonitor(
            window,
            glfwGetPrimaryMonitor(),
            0,
            0,
            vidMode.width(),
            vidMode.height(),
            vidMode.refreshRate()
        );

    } else {
        glfwSetWindowMonitor(
            window,
            NULL,
            100,
            100,
			1280,
            720,
            GLFW_DONT_CARE
        );
    }
}
}