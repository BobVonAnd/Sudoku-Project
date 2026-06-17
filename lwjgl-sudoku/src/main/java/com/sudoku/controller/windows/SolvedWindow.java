package com.sudoku.controller.windows;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;
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
import com.sudoku.view.fonts.CreateFont;

public class SolvedWindow extends Window implements WindowInterface {
    
    private WindowManager wm;
    private Sudoku sudokuUI;
    private SudokuBoard sudokuBoard;
    private SudokuBoard solvedSudokuBoard;
    private playSudokuWindow pw;
    private Gamepad gpad;

    private int width;
    private int height;
    private CreateFont font;
    private Shader fontShader;
    private CreateString text;

    private long a_buffer_timestamp = System.currentTimeMillis();
    private long a_buffer_max = 200;

    private MenuButton returnButton;
    private MenuButton end; 

    public SolvedWindow(WindowManager wm, int width, int height, SudokuBoard sb, SudokuBoard solvedsb, playSudokuWindow pw) {
        super(wm);
        this.wm = wm;
        sudokuBoard = sb;
        solvedSudokuBoard = solvedsb;
        this.pw = pw;
        this.width = width;
        this.height = height;
        wm.setActiveWindow(this);
        
    }

    public void update(WindowManager wm, int width, int height, SudokuBoard sb, SudokuBoard solvedsb, playSudokuWindow pw){
        this.wm = wm;
        sudokuBoard = sb;
        solvedSudokuBoard = solvedsb;
        this.pw = pw;
        this.width = width;
        this.height = height;
        wm.setActiveWindow(this);
    }


    public void create() {
        // This code runs once
        gpad = new Gamepad();
        font = wm.getFont();
		fontShader = wm.getFontShader();
		text = new CreateString(fontShader, font, width, height);        

        returnButton = new MenuButton(-0.7, 0.75, 0.2, text, fontShader, "Back");
        addElement(returnButton, 0);
        gpad.addElement(returnButton, 0, 0);

        end = new MenuButton(-0.7, -0.75, 0.2, text, fontShader, "End");
        addElement(end, 0);
        gpad.addElement(end, 0, 1);


        sudokuUI = new Sudoku(width, height, 1.6, solvedSudokuBoard, sudokuBoard, font, fontShader, this);
        addElement(sudokuUI, 0);

    }

    

    public void step() {
        // This code runs every frame
        gpad.step();
        holdOver(end);
        holdOver(returnButton);
        long now = System.currentTimeMillis();
        boolean entered = gpad.isEntered() && (now - a_buffer_timestamp >= a_buffer_max);
        if (entered) {
            a_buffer_timestamp = now;
            windowTransition(end);
            windowTransition(returnButton);
        }
    }


     private void holdOver(MenuButton button) {
        double mouseXt = mouseX / (width / 2) - 1;
        double mouseYt = -mouseY / (height / 2) + 1;
        if ((button.getPos()[0] - returnButton.getSize() / 2 < mouseXt &
                button.getPos()[0] + returnButton.getSize() / 2 > mouseXt &

                button.getPos()[1] - button.getSize() / 2 < mouseYt &
                button.getPos()[1] + button.getSize() / 2 > mouseYt && !gpad.isConnected()) || gpad.isSelected(button)) {
            button.heldOver(true);
        } else {
            button.heldOver(false);
        }
    }
    @Override // If you don't need a key callback, just delete this
    public void keyCallback(int key, int scancode, int action, int mods) {
        if (key == GLFW_KEY_SPACE && action == GLFW_PRESS) {
            System.out.println("Space pressed!");
        }
    }
    
    @Override // If you don't need a resize callback, just delete this
    public void resizeCallback(int width, int height) {
        this.width = width;
        this.height = height;
        sudokuUI.resize(width, height);
    }

    @Override // If you don't need a mouse button callback, just delete this
    public void mouseButtonCallback(int button, int action, int mods) {

        if (button == GLFW_MOUSE_BUTTON_LEFT &&
            action == GLFW_PRESS) {
            windowTransition(returnButton);
            windowTransition(end);
            

        }

        if (button == GLFW_MOUSE_BUTTON_RIGHT &&
            action == GLFW_PRESS) {

            System.out.println("Right click!");
        }
    }

    public void windowTransition(MenuButton e) {
        if(e == returnButton && returnButton.isHeldOver()){
            wm.setActiveWindow(pw);
        }else if(e == end && end.isHeldOver()){
            new mainMenuWindow(wm, width, height);
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