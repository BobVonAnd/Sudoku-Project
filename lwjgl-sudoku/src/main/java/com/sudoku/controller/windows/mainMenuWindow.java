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
import com.sudoku.view.font.CreateFont;

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
        super(wm); // initialize parent
        this.wm = wm;
        this.width = width;
        this.height = height;
        wm.setActiveWindow(this); // set the active window to this
    }

    public void create() {
        gpad = new Gamepad(); // init controller support
        // This code runs once
        font = wm.getFont();
		//creates a shader and a class that can display strings
		fontShader = wm.getFontShader();
		text = new CreateString(fontShader, font, width, height);

        // create the create button
        createButton = new MenuButton(0,0.6,0.4,text,fontShader,"Create");
        gpad.addElement(createButton,0,1); // add button to controller
        addElement(createButton,0); // add button to the ui
        Buttons[1] = createButton; // add the button to the collission array

        // create the play button
        playButton = new MenuButton(0,0,0.4,text,fontShader,"Play");
        gpad.addElement(playButton,0,2); // add button to controller
        addElement(playButton,0); // add button to the ui
        Buttons[0] = playButton; // add the button to the collission array

        // create the exit button
        exitButton = new MenuButton(0,-.6,0.4,text,fontShader,"Exit");
        gpad.addElement(exitButton,0,3); // add button to controller
        addElement(exitButton,0); // add button to the ui
        Buttons[2] = exitButton; // add the button to the collission array
    }

    @Override
    public void cursorPosCallback(double x, double y) { 
        // update the current mouse position every cursor callback
        this.mouseX = x;
        this.mouseY = y;
    }

    @Override
    public void resizeCallback(int width, int height) {
        // update width and height every time you resize
        this.width = width;
        this.height = height;
        text.setXY(width, height);
    }

    public void step() {
        // This code runs every frame
        gpad.step(); // init gamepad
        // Check every menu button if we're holding over
        double mouseXt = mouseX/(width/2)-1; // translate x mouse position to LWJGL compatible coordinates
        double mouseYt = -mouseY/(height/2)+1; // translate y mouse position to LWJGL compatible coordinates
        for (int i = 0 ; i < Buttons.length ; i++) {
            // Collision checking
            if ((Buttons[i].getPos()[0] - Buttons[i].getSize()/2 < mouseXt & 
                Buttons[i].getPos()[0] + Buttons[i].getSize()/2 > mouseXt &
                !gpad.isConnected()&

                Buttons[i].getPos()[1] - Buttons[i].getSize()/2 < mouseYt & 
                Buttons[i].getPos()[1] + Buttons[i].getSize()/2 > mouseYt) || gpad.isSelected(Buttons[i])) {
                Buttons[i].heldOver(true); // held over on
                windowTransition(Buttons[i],false); // universal window transition function
            } else {
                Buttons[i].heldOver(false); // held over off
            }
        }
    }

    @Override 
    public void mouseButtonCallback(int button, int action, int mods) {
        // if button is pressed, do what it needs
        if (button == GLFW_MOUSE_BUTTON_LEFT &&
            action == GLFW_PRESS) {

            // Button Action
            for (int i = 0 ; i < Buttons.length ; i++) {
                windowTransition(Buttons[i], true);
            }
        }
    }

    // transition between windows
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