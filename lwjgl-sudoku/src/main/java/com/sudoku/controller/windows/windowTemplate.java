package com.sudoku.controller.windows;

import static org.lwjgl.glfw.GLFW.*;
import com.sudoku.controller.Window;
import com.sudoku.controller.WindowInterface;
import com.sudoku.controller.WindowManager;
import com.sudoku.model.Gamepad;

/// THIS IS PURELY FOR THE DEVELOPERS TO BE ABLE TO MAKE A WINDOW
public class windowTemplate extends Window implements WindowInterface {
    
    private WindowManager wm; // window manager, so we can reuse it
    private Gamepad gpad; // gamepad support

    public windowTemplate(WindowManager wm) {
        super(wm); // init parent
        this.wm = wm; // get the wm
        wm.setActiveWindow(this); // set active window to this window
    }

    public void create() {
        // This code runs once
        gpad = new Gamepad(); // init gamepad
    }

    public void step() {
        // This code runs every frame
        gpad.step(); // step func in gamepad
    }

    @Override // If you don't need a key callback, just delete this - test function
    public void keyCallback(int key, int scancode, int action, int mods) {
        if (key == GLFW_KEY_SPACE && action == GLFW_PRESS) {
            System.out.println("Space pressed!");
        }
    }
    
    @Override // If you don't need a resize callback, just delete this - test function
    public void resizeCallback(int width, int height) {
        System.out.println("New size: " + width + "x" + height);
    }

    @Override // If you don't need a mouse button callback, just delete this - test function
    public void mouseButtonCallback(int button, int action, int mods) {

        if (button == GLFW_MOUSE_BUTTON_LEFT &&
            action == GLFW_PRESS) {

            System.out.println("Left click!");
        }

        if (button == GLFW_MOUSE_BUTTON_RIGHT &&
            action == GLFW_PRESS) {

            System.out.println("Right click!");
        }
    }

    private double mouseX; // cursor x position
    private double mouseY; // cursor y position

    @Override
    public void cursorPosCallback(double x, double y) {
        // update cursor positions
        this.mouseX = x;
        this.mouseY = y;
    }

}
