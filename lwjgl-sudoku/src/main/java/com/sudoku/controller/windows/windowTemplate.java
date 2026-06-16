package com.sudoku.controller.windows;

import static org.lwjgl.glfw.GLFW.*;
import com.sudoku.controller.Window;
import com.sudoku.controller.WindowInterface;
import com.sudoku.controller.WindowManager;
import com.sudoku.model.Gamepad;

/// THIS IS PURELY FOR THE DEVELOPERS TO BE ABLE TO MAKE A WINDOW
public class windowTemplate extends Window implements WindowInterface {
    
    private WindowManager wm;
    private Gamepad gpad;

    public windowTemplate(WindowManager wm) {
        super(wm);
        this.wm = wm;
        wm.setActiveWindow(this);
    }

    public void create() {
        // This code runs once
        gpad = new Gamepad();
    }

    public void step() {
        // This code runs every frame
        gpad.step();
    }

    @Override // If you don't need a key callback, just delete this
    public void keyCallback(int key, int scancode, int action, int mods) {
        if (key == GLFW_KEY_SPACE && action == GLFW_PRESS) {
            System.out.println("Space pressed!");
        }
    }
    
    @Override // If you don't need a resize callback, just delete this
    public void resizeCallback(int width, int height) {
        System.out.println("New size: " + width + "x" + height);
    }

    @Override // If you don't need a mouse button callback, just delete this
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

    private double mouseX;
    private double mouseY;

    @Override
    public void cursorPosCallback(double x, double y) {
        this.mouseX = x;
        this.mouseY = y;
    }

}
