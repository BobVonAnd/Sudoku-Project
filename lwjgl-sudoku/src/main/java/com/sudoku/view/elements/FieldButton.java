package com.sudoku.view.elements;

import com.sudoku.model.Field;

import com.sudoku.model.SudokuBoard;
import com.sudoku.view.CreateString;
import com.sudoku.view.Shader;

import static org.lwjgl.opengl.GL11.*;

public class FieldButton implements Element {
    private Field field;
    private int value;
    private double x,y,size;
    private boolean selected;
    private double[] colour;
    private CreateString text;
    private Shader fontShader;

    public FieldButton(Field f, double x, double y, double size, SudokuBoard sudokuBoard, CreateString text, Shader fontShader) {
        this.field = f;    
        this.size = size;
        this.x = x;
        this.y = y;        
        colour = new double[] {1.0f,1.0f,1.0f};
        this.text = text;
        this.fontShader = fontShader;
        selected = true;
    }

    public void draw() {
        System.out.println("TEST");
        
        fontShader.detach();
        glBegin(GL_QUADS);
        setColour();
        glColor3d(colour[0], colour[1], colour[2]);
        glVertex2d(x, y - size); //Bottom left
        glVertex2d(x - size, y - size); //Bottom right
        glVertex2d(x - size, y ); //Top Right
        glVertex2d(x, y); //Top Left
        glEnd();
        value = field.getValue();
        if (value!= 0){
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            text.makeText(""+value, (float)(x+size/2),(float)(y+size/2), (float)(1.5*1.75*size), new float[]{0.203921569f,0.278431373f,0.380392157f});
		    text.flush();
        }
    }

    public double[] getPos() {
        return new double[] {this.x,this.y};
    }

    public double getSize() {
        return this.size;
    }

    public void selected(boolean isIt) {
        this.selected = isIt;
    }

    private void setColour(){
        if (selected) {
            colour[0] = 0.733333333;
            colour[1] = 0.870588235; 
            colour[2] = 0.984313725;
        } else {
            colour[0] = 1;
            colour[1] = 1; 
            colour[2] = 1;
        }
    }

    private void fieldIsTouching(){
        colour[0] = 0.882352941;
        colour[1] = 0.921568627; 
        colour[2] = 0.952941176;
    }

    private void samenumber(){
        colour[0] = 0.764705882;
        colour[1] = 0.843137255; 
        colour[2] = 0.917647059;
    }

    public boolean isSelected(){  
        return selected;
    }
    // What we wanna do
    // Be able to paste a image on it with clipping mask
    // Be able to paste text on it
    // Store coords for both screen coords and sudokuboard coords
    // It needs to hold size, force aspect ratio
    // It needs to be able to be attached to a Window

}