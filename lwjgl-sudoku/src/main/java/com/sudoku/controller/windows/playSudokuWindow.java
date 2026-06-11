package com.sudoku.controller.windows;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_1;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_2;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_3;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_4;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_5;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_6;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_7;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_8;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_9;
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
    private int size;
    private double mouseX;
    private double mouseY;
    private int width, height;
    private CreateFont font;
	private CreateString text;
    private Shader fontShader;
    private FieldButton[][] buttonArray;
    private double fieldsize;
    private int[] selectedField = new int[2];

    public playSudokuWindow(WindowManager wm, int width, int height) {
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
        size = 36;
        sudokuBoard = new SudokuBoard(size);
        sudokuBoard.populate(1);
        buttonArray = new FieldButton[size][size];
        double y;
        double x = -0.8;
        double yStart = 0.8;        

        fieldsize = 1.6 / size ;
        for (int i = 0; i < size; i++) {
            y = yStart;
            for (int j = 0; j < size; j++) {
                buttonArray[i][j] = new FieldButton(sudokuBoard.getSingleField(i,j), x, y, fieldsize, sudokuBoard, text, fontShader);
                addElement(buttonArray[i][j], 0);
                y -= fieldsize;
            }
            x += fieldsize;
        }

    }

    public void step() {
        // This code runs every frame

    }

    @Override // If you don't need a key callback, just delete this
    public void keyCallback(int key, int scancode, int action, int mods){
        if (key == GLFW_KEY_1 && action == GLFW_PRESS){
                    sudokuBoard.changeField(selectedField[0],selectedField[1],1);
                    System.out.println("1 pressed!");
        }else if (key == GLFW_KEY_2 && action == GLFW_PRESS){
                    sudokuBoard.changeField(selectedField[0],selectedField[1],2);
                    System.out.println("2 pressed!");
        }else if (key == GLFW_KEY_3 && action == GLFW_PRESS){
                    sudokuBoard.changeField(selectedField[0],selectedField[1],3);
                    System.out.println("3 pressed!");
        }else if (key == GLFW_KEY_4 && action == GLFW_PRESS){
                    sudokuBoard.changeField(selectedField[0],selectedField[1],4);
                    System.out.println("4 pressed!");
        }else if (key == GLFW_KEY_5 && action == GLFW_PRESS){
                    sudokuBoard.changeField(selectedField[0],selectedField[1],5);
                    System.out.println("5 pressed!");
        }else if (key == GLFW_KEY_6 && action == GLFW_PRESS){
                    sudokuBoard.changeField(selectedField[0],selectedField[1],6);
                    System.out.println("6 pressed!");
        }else if (key == GLFW_KEY_7 && action == GLFW_PRESS){
                    sudokuBoard.changeField(selectedField[0],selectedField[1],7);
                    System.out.println("7 pressed!");
        }else if (key == GLFW_KEY_8 && action == GLFW_PRESS){
                    sudokuBoard.changeField(selectedField[0],selectedField[1],8);
                    System.out.println("8 pressed!");
        }else if (key == GLFW_KEY_9 && action == GLFW_PRESS){
                    sudokuBoard.changeField(selectedField[0],selectedField[1],9);
                    System.out.println("9 pressed!");
        }
        
        if (key == GLFW_KEY_SPACE && action == GLFW_PRESS) {
            System.out.println("Space pressed!");
        }
    }
    
    @Override // If you don't need a resize callback, just delete this
    public void resizeCallback(int width, int height) {
        this.width = width;
        this.height = height;
        System.out.println("New size: " + width + "x" + height);
    }

    @Override // If you don't need a mouse button callback, just delete this
    public void mouseButtonCallback(int button, int action, int mods) {
        
        if (button == GLFW_MOUSE_BUTTON_LEFT &&
            action == GLFW_PRESS) { 
            double [] pos = new double[2];
            double mouseXt = mouseX/(width/2)-1;
            double mouseYt = -mouseY/(height/2)+1;
            FieldButton fieldButton;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    fieldButton = buttonArray[i][j];
                    pos = fieldButton.getPos();
                    if (pos[0] < mouseXt & pos[0] + fieldsize > mouseXt &
                        pos[1] > mouseYt & pos[1] - fieldsize < mouseYt){
                        fieldButton.selected(true);
                        selectedField[0] = i;
                        selectedField[1] = j;
                    } else {
                        fieldButton.selected(false);
                    }
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
