package com.sudoku.view.elements;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glVertex2f;

import com.sudoku.controller.Window;
import com.sudoku.model.SudokuBoard;
import com.sudoku.view.CreateString;
import com.sudoku.view.Shader;
import com.sudoku.view.fonts.CreateFont;

public class Sudoku implements Element {

    private SudokuBoard sudokuBoard;
    private int size, width, height;
	private CreateString text;
    private Shader fontShader;
    private FieldButton[][] buttonArray;
    private double fieldsizeX, fieldsizeY, aspect, xAspect, yAspect;
    private double yStart = 0.8;        
    private double xStart = -0.8;



    
    public Sudoku(int width, int height, SudokuBoard sb, CreateFont font, Shader fontShader, Window window){
        this.width = width;
        this.height = height;
        this.sudokuBoard = sb;
        this.fontShader = fontShader;
        
		//creates a shader and a class that can display strings
		text = new CreateString(fontShader, font);

        size = sudokuBoard.getSize();
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
                window.addElement(buttonArray[i][j], 0);
                y -= fieldsizeY;
            }
            x += fieldsizeX;
        }
    }

    //this is used for solved sudoku and it changes the color
    //where there was an input in the finel solved sudoku
    public Sudoku(int width, int height, SudokuBoard sb, SudokuBoard unsolvedSB, CreateFont font, Shader fontShader, Window window){
        this.width = width;
        this.height = height;
        this.sudokuBoard = sb;
        this.fontShader = fontShader;
        
		//creates a shader and a class that can display strings
		text = new CreateString(fontShader, font);

        size = sudokuBoard.getSize();
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
                boolean gess = unsolvedSB.getSingleField(i, j).getValue() != 0;
                buttonArray[i][j] = new FieldButton(sudokuBoard.getSingleField(i,j), x, y, fieldsizeX, fieldsizeY, sudokuBoard, text, fontShader, gess, true);
                window.addElement(buttonArray[i][j], 0);
                y -= fieldsizeY;
            }
            x += fieldsizeX;
        }
    }

    
    
    public void draw(){
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

    public void setNotValidInput(boolean isValidInput, int[] selectedField){
        buttonArray[selectedField[0]][selectedField[1]].setNotValid(isValidInput);
    }
    public void setNotValidInputToFalse(int[] selectedField){
        buttonArray[selectedField[0]][selectedField[1]].setNotValid(false);
    }

    public int[] leftClick(double x,double y){
        double [] pos = new double[2];
        double mouseXt = x/(width/2)-1;
        double mouseYt = -y/(height/2)+1;
        FieldButton fieldButton;
        int[] selectedField = new int[2];
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
        return selectedField;
    }

    public void resize(int width, int height){
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




} 








