package com.sudoku.controller.windows;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

import com.sudoku.controller.Window;
import com.sudoku.controller.WindowInterface;
import com.sudoku.controller.WindowManager;
import com.sudoku.model.Gamepad;
import com.sudoku.model.SudokuBoard;
import com.sudoku.view.CreateString;
import com.sudoku.view.Shader;
import com.sudoku.view.elements.MenuButton;
import com.sudoku.view.elements.Sudoku;
import com.sudoku.view.font.CreateFont;

public class EndScreenWindow extends Window implements WindowInterface {
    
    private WindowManager wm;
    private Gamepad gpad;
    private SudokuBoard sudokuBoard;
    private Sudoku sudokuUI;
    private MenuButton returnButton;

    private int width;
    private int height;

    private CreateFont font; // font
    private CreateString text; // text
    private Shader fontShader; // font shader

    private String endCondition;

  

    public EndScreenWindow(WindowManager wm, SudokuBoard sb, int width, int height, String endCondition) {
        super(wm); // initialize parent
        this.wm = wm; // wm
        this.sudokuBoard = sb; // sudokuboard
        this.width = width; // width
        this.height = height; // height
        this.endCondition = endCondition; // end condition
        wm.setActiveWindow(this); // sets the active window this end screen window
    }

    // when the end screen window is created
    public void create() {
        // This code runs once
        font = wm.getFont(); // font
        fontShader = wm.getFontShader(); // shader
        text = new CreateString(fontShader, font, width, height); // string
        gpad = new Gamepad(); // init gamepad

        // make the return button
        returnButton = new MenuButton(-0.7, 0.75, 0.2, text, fontShader, "Return");
        addElement(returnButton, 0); // add the retun button to the ui

        // make the sudoku ui
        sudokuUI = new Sudoku(width, height, 1.6, 0,0, sudokuBoard, font, fontShader, this);
        addElement(sudokuUI, 0); // add the sudoku ui to the ui
    }

    public void step() {
        // This code runs every frame
        gpad.step(); // gamepad step evetn

        // end condition, just read it, you'll get it
        switch (endCondition) {
            case "win":
                text.makeText("You Finished The Sudoku!", -0.3f, 0.83f, 0.5f, new float[]{0f,0f,0f});
                break;
            case "lost":
                text.makeText("You Lost The Sudoku", -0.28f, 0.83f, 0.5f, new float[]{1f,0f,0f});
                break;
            //add cases like timeconstrain if needed :)
            default:
                throw new AssertionError();
        }
        text.flush();
        

        holdOver(returnButton);
    }

    // mouse collision
    private void holdOver(MenuButton button) {
        double mouseXt = mouseX / (width / 2) - 1;
        double mouseYt = -mouseY / (height / 2) + 1;
        if (button.getPos()[0] - returnButton.getSize() / 2 < mouseXt &
                button.getPos()[0] + returnButton.getSize() / 2 > mouseXt &

                button.getPos()[1] - button.getSize() / 2 < mouseYt &
                button.getPos()[1] + button.getSize() / 2 > mouseYt) {
            button.heldOver(true);
        } else {
            button.heldOver(false);
        }
    }

    @Override
    public void mouseButtonCallback(int button, int action, int mods) {

        if (button == GLFW_MOUSE_BUTTON_LEFT &&
            action == GLFW_PRESS) {
                // go to main menu if click on button
            if(returnButton.isHeldOver()){
                new mainMenuWindow(wm, width, height);
            }
        }
    }

    private double mouseX;
    private double mouseY;

    @Override // cursor pos updater
    public void cursorPosCallback(double x, double y) {
        this.mouseX = x;
        this.mouseY = y;
    }

}
