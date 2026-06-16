package com.sudoku.controller.windows;

import static org.lwjgl.glfw.GLFW.*;

import java.rmi.server.Operation;

import com.sudoku.controller.Window;
import com.sudoku.controller.WindowInterface;
import com.sudoku.model.Gamepad;
import com.sudoku.controller.WindowManager;
import com.sudoku.view.CreateString;
import com.sudoku.view.Shader;
import com.sudoku.view.elements.MenuButton;
import com.sudoku.view.fonts.CreateFont;

/// THIS IS PURELY FOR THE DEVELOPERS TO BE ABLE TO MAKE A WINDOW
public class mainMenuWindow extends Window implements WindowInterface {
    
    private WindowManager wm;
    private MenuButton playButton, createButton, exitButton;
    private MenuButton[] Buttons = new MenuButton[3];
    private double mouseX;
    private double mouseY;
    private int width, height;
    private CreateFont font;
	private CreateString text;
    private Shader fontShader;
    private Gamepad gpad;

    public mainMenuWindow(WindowManager wm, int width, int height) {
        super(wm);
        this.wm = wm;
        this.width = width;
        this.height = height;
        wm.setActiveWindow(this);
    }

    public void create() {
        gpad = new Gamepad();
        // This code runs once
        font = wm.getFont();
		//creates a shader and a class that can display strings
		fontShader = wm.getFontShader();
		text = new CreateString(fontShader, font, width, height);

        createButton = new MenuButton(0,0.6,0.4,text,fontShader,"Create");
        gpad.addElement(createButton,0,1);
        addElement(createButton,0);
        Buttons[1] = createButton;

        playButton = new MenuButton(0,0,0.4,text,fontShader,"Play");
        gpad.addElement(playButton,0,2);
        addElement(playButton,0);
        Buttons[0] = playButton;

        exitButton = new MenuButton(0,-.6,0.4,text,fontShader,"Exit");
        gpad.addElement(exitButton,0,3);
        addElement(exitButton,0);
        Buttons[2] = exitButton;
    }

    @Override
    public void cursorPosCallback(double x, double y) {
        this.mouseX = x;
        this.mouseY = y;
    }

    @Override
    public void resizeCallback(int width, int height) {
        this.width = width;
        this.height = height;
        text.setXY(width, height);
    }

    public void step() {
        // This code runs every frame
        gpad.step();
        // Check every menu button if we're holding over
        double mouseXt = mouseX/(width/2)-1;
        double mouseYt = -mouseY/(height/2)+1;
        for (int i = 0 ; i < Buttons.length ; i++) {
            if ((Buttons[i].getPos()[0] - Buttons[i].getSize()/2 < mouseXt & 
                Buttons[i].getPos()[0] + Buttons[i].getSize()/2 > mouseXt &
                !gpad.isConnected()&

                Buttons[i].getPos()[1] - Buttons[i].getSize()/2 < mouseYt & 
                Buttons[i].getPos()[1] + Buttons[i].getSize()/2 > mouseYt) || gpad.isSelected(Buttons[i])) {
                Buttons[i].heldOver(true);
                windowTransition(Buttons[i],false);
            } else {
                Buttons[i].heldOver(false);
            }
        }
    }

    @Override // If you don't need a key callback, just delete this
    public void keyCallback(int key, int scancode, int action, int mods) {
        if (key == GLFW_KEY_SPACE && action == GLFW_PRESS) {
            System.out.println("Space pressed!");
        }
    }

    @Override // If you don't need a mouse button callback, just delete this
    public void mouseButtonCallback(int button, int action, int mods) {

        if (button == GLFW_MOUSE_BUTTON_LEFT &&
            action == GLFW_PRESS) {

            // Button Action
            for (int i = 0 ; i < Buttons.length ; i++) {
                windowTransition(Buttons[i], true);
            }
        }
    }

    public void windowTransition(MenuButton b, boolean mouseClick) {
        if (mouseClick || gpad.isEntered()) {
            if (b.isHeldOver() && elementExists(b)) {
                if (b == playButton) {
                    new PlaySudokuSettingsWindow(wm, width, height);
                } else if (b == createButton) {
                    new CreateMenuWindow(wm, width, height);
                } else if (b == exitButton) {
                    System.exit(0);
                }
            }
        }
    }
}
