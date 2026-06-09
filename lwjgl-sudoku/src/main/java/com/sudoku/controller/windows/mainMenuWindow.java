package com.sudoku.controller.windows;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

import com.sudoku.controller.Window;
import com.sudoku.controller.WindowInterface;
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

    public mainMenuWindow(WindowManager wm, int width, int height) {
        super(wm);
        this.wm = wm;
        wm.setActiveWindow(this);
        this.width = width;
        this.height = height;
    }

    public void create() {
        // This code runs once
        font = wm.getFont();
		//creates a shader and a class that can display strings
		fontShader = wm.getFontShader();
		text = new CreateString(fontShader, font);

        playButton = new MenuButton(0,0,0.4,text,fontShader,"Play");
        addElement(playButton);
        Buttons[0] = playButton;

        createButton = new MenuButton(0,0.6,0.4,text,fontShader,"Create");
        addElement(createButton);
        Buttons[1] = createButton;

        exitButton = new MenuButton(0,-.6,0.4,text,fontShader,"Exit");
        addElement(exitButton);
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
    }

    public void step() {
        // This code runs every frame
        
        // Check every menu button if we're holding over
        double mouseXt = mouseX/(width/2)-1;
        double mouseYt = -mouseY/(height/2)+1;
        for (int i = 0 ; i < Buttons.length ; i++) {
            if (Buttons[i].getPos()[0] - Buttons[i].getSize()/2 < mouseXt & 
                Buttons[i].getPos()[0] + Buttons[i].getSize()/2 > mouseXt &

                Buttons[i].getPos()[1] - Buttons[i].getSize()/2 < mouseYt & 
                Buttons[i].getPos()[1] + Buttons[i].getSize()/2 > mouseYt) {
                Buttons[i].heldOver(true);
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
                if (Buttons[i].isHeldOver()) {
                    if (Buttons[i] == playButton) {
                        new playSudokuWindow(wm);
                    } else if (Buttons[i] == createButton) {
                        new CreateMenuWindow(wm);
                    } else if (Buttons[i] == exitButton) {
                        System.exit(0);
                    }
                }
            }
        }
    }

}
