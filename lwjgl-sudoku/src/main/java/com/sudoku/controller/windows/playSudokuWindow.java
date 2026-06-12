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
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
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
    private int size, width, height;
    private double mouseX, mouseY;
    private CreateFont font;
	private CreateString text;
    private Shader fontShader;
    private FieldButton[][] buttonArray;
    private double fieldsizeX, fieldsizeY, aspect, xAspect, yAspect, difficulty;
    private double yStart = 0.8;        
    private double xStart = -0.8;
    private int[] selectedField = new int[2];

    public playSudokuWindow(WindowManager wm, int width, int height, int size, double difficulty) {
        super(wm);
        this.wm = wm;
        this.width = width;
        this.height = height;
        this.size = size;
        this.difficulty = difficulty;
        wm.setActiveWindow(this);
    }

    public void create() {
        // This code runs once
        font = wm.getFont();
		//creates a shader and a class that can display strings
		fontShader = wm.getFontShader();
		text = new CreateString(fontShader, font); 
        
        sudokuBoard = new SudokuBoard(size);
        sudokuBoard.populate(difficulty);
        buttonArray = new FieldButton[size][size];

        aspect = (double) height/(double)width;
        xAspect = xStart * aspect;

        fieldsizeY = 1.6 / size ;
        fieldsizeX = fieldsizeY*aspect;

        double y;
        double x;

        x = xAspect; 
        for (int i = 0; i < size; i++) {
            y = yStart;
            for (int j = 0; j < size; j++) {
                buttonArray[i][j] = new FieldButton(sudokuBoard.getSingleField(i,j), x, y, fieldsizeX, fieldsizeY, sudokuBoard, text, fontShader);
                addElement(buttonArray[i][j], 0);
                y -= fieldsizeY;
            }
            x += fieldsizeX;
        }
    }

    public void step() {
        // This code runs every frame
        fontShader.detach();
        glBegin(GL_LINES);

        float boardlenth = (float)(size * fieldsizeY);
        int bigfield = (int)Math.sqrt(size);

        regularline();
        float x = (float)xAspect;
        for (int i = 0; i <= size; i++){
            if (i % bigfield != 0){
                glVertex2f(x, (float)yStart);
                glVertex2f(x, (float)yStart-boardlenth);
            }
            x += (float)fieldsizeX;
        }
        boardlenth = (float)(size * fieldsizeX);

        float y = (float)yStart;
        for (int i = 0; i <= size; i++){
            if (i % bigfield != 0){
                glVertex2f((float)xAspect, y);
                glVertex2f((float)xAspect+boardlenth, y);
            }
            y -= (float)fieldsizeY;
        }

        boardlenth = (float)(size * fieldsizeY);
        bigfieldline();
        x = (float)xAspect;
        for (int i = 0; i <= size; i++){
            if (i % bigfield == 0){
                glVertex2f(x, (float)yStart);
                glVertex2f(x, (float)yStart-boardlenth);
            }
            x += (float)fieldsizeX;
        }

        boardlenth = (float)(size * fieldsizeX);
        y = (float)yStart;
        for (int i = 0; i <= size; i++){
            if (i % bigfield == 0){
                glVertex2f((float)xAspect, y);
                glVertex2f((float)xAspect+boardlenth, y);
            }
            y -= (float)fieldsizeY;
        }

        glEnd();
    }

    private void bigfieldline(){
        glLineWidth(2.5f);
        glColor3d(0.20392157f,0.27842137f,0.38039216f);
    }

    private void regularline(){
        glLineWidth(1.5f);
        glColor3d(0.84705882f,0.88235294f,0.91764706f);
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
        
        aspect = (double)height/(double)width;
        xAspect = xStart * aspect;
        
        fieldsizeY = 1.6 / size ;
        fieldsizeX = fieldsizeY*aspect;

        double y;
        double x;

        x = xAspect; 
        for (int i = 0; i < size; i++) {
            y = yStart;
            for (int j = 0; j < size; j++) {
                double[] xy = {x,y}; 
                buttonArray[i][j].setXY(xy);
                xy[0] = fieldsizeX;
                xy[1] = fieldsizeY;
                buttonArray[i][j].setFieldSize(xy);
                y -= fieldsizeY;
            }
            x += fieldsizeX;
        }
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
                    if (pos[0] < mouseXt & pos[0] + fieldsizeX > mouseXt &
                        pos[1] > mouseYt & pos[1] - fieldsizeY < mouseYt){
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
