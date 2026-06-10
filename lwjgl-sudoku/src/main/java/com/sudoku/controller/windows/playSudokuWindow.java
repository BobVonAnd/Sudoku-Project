package com.sudoku.controller.windows;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

import com.sudoku.controller.Window;
import com.sudoku.controller.WindowInterface;
import com.sudoku.controller.WindowManager;
import com.sudoku.model.SudokuBoard;
import com.sudoku.view.CreateString;
import com.sudoku.view.Shader;
import com.sudoku.view.elements.FieldButton;
import com.sudoku.view.fonts.CreateFont;
import com.sudoku.model.Field;

public class playSudokuWindow extends Window implements WindowInterface {
    
    private WindowManager wm;
    private SudokuBoard sudokuBoard;
    private int size = 4;
    private double mouseX;
    private double mouseY;
    private int width, height;
    private CreateFont font;
	private CreateString text;
    private Shader fontShader;
    private FieldButton[][] buttonArray;
    private double fieldsize;

    public playSudokuWindow(WindowManager wm) {
        super(wm);
        this.wm = wm;
        wm.setActiveWindow(this);
    }

    public void create() {
        // This code runs once
        font = wm.getFont();
		//creates a shader and a class that can display strings
		fontShader = wm.getFontShader();
		text = new CreateString(fontShader, font); 
        sudokuBoard = new SudokuBoard(size);
        buttonArray = new FieldButton[size][size];
        double x;
        double y;
        double xStart = -1;
        double yStart = -1;        

        fieldsize = 0.4;
        for (int i = 0; i > size; i++) {
            x = xStart;
            for (int j = 0; j > size; j++) {
                y = yStart;
                buttonArray[i][j] = new FieldButton(sudokuBoard.getSingleField(i,j), x, y, fieldsize, sudokuBoard, text, fontShader);
                y += fieldsize;
            }
            x += fieldsize;
        }


        
        
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
        System.out.println("New size: " + width + "x" + height);
    }

    @Override // If you don't need a mouse button callback, just delete this
    public void mouseButtonCallback(int button, int action, int mods) {

        if (button == GLFW_MOUSE_BUTTON_LEFT &&
            action == GLFW_PRESS) { 
            System.out.println("Left click!");
            for (int i = 0; i > size; i++) {
                for (int j = 0; j > size; j++) {
                    //buttonArray[i][j] = 
                }
            }
            

            
        }

        if (button == GLFW_MOUSE_BUTTON_RIGHT &&
            action == GLFW_PRESS) {

            System.out.println("Right click!");
        }
    }

    @Override
    public void cursorPosCallback(double x, double y) {
        this.mouseX = x;
        this.mouseY = y;
    }

}
