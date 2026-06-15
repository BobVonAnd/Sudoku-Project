package com.sudoku.controller.windows;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

import com.sudoku.controller.Window;
import com.sudoku.controller.WindowInterface;
import com.sudoku.controller.WindowManager;
import com.sudoku.model.SudokuBoard;
import com.sudoku.view.CreateString;
import com.sudoku.view.Shader;
import com.sudoku.view.elements.Sudoku;
import com.sudoku.view.fonts.CreateFont;

public class SolvedWindow extends Window implements WindowInterface {
    
    private WindowManager wm;
    private Sudoku sudokuUI;
    private SudokuBoard sudokuBoard;
    private SudokuBoard solvedSudokuBoard;
    private int width;
    private int height;
    private CreateFont font;
    private Shader fontShader;
    private CreateString text;

    public SolvedWindow(WindowManager wm, int width, int height, SudokuBoard sb, SudokuBoard solvedsb) {
        super(wm);
        this.wm = wm;
        sudokuBoard = sb;
        solvedSudokuBoard = solvedsb;
        this.width = width;
        this.height = height;
        wm.setActiveWindow(this);
        
    }


    public void create() {
        // This code runs once
        font = wm.getFont();
		fontShader = wm.getFontShader();
		text = new CreateString(fontShader, font, width, height);        

        sudokuUI = new Sudoku(width, height, solvedSudokuBoard, sudokuBoard, font, fontShader, this);
        addElement(sudokuUI, 0);

    }

    

    public void step() {
        // This code runs every frame
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
