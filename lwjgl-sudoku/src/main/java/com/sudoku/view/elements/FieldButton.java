package com.sudoku.view.elements;

import com.sudoku.model.Field;

import com.sudoku.model.SudokuBoard;
import com.sudoku.view.CreateString;
import com.sudoku.view.Shader;

import static org.lwjgl.opengl.GL11.*;

public class FieldButton implements Element {
    private Field field;
    private int value;
    private double x,y,sizeX,sizeY;
    private boolean selected;
    private double[] colour;
    private CreateString text;
    private Shader fontShader;
    private boolean touching;
    private boolean snumber;

    public FieldButton(Field f, double x, double y, double sizeX, double sizeY, SudokuBoard sudokuBoard, CreateString text, Shader fontShader) {
        this.field = f;    
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.x = x;
        this.y = y;        
        colour = new double[] {1.0f,0f,0f};
        this.text = text;
        this.fontShader = fontShader;
    }

    public void draw() {
        fontShader.detach();
        glBegin(GL_QUADS);
        setColour();
        glColor3d(colour[0], colour[1], colour[2]);
        glVertex2d(x, y - sizeY); //Bottom left
        glVertex2d(x + sizeX, y - sizeY); //Bottom right
        glVertex2d(x + sizeX, y ); //Top Right
        glVertex2d(x, y); //Top Left
        glEnd();
        value = field.getValue();
        if (value!= 0){
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            if (value < 10){
                text.makeText(""+value, (float)(x+sizeX/2.72),(float)(y-sizeY/0.895), (float)(4.5*sizeY), new float[]{0.203921569f,0.278431373f,0.380392157f});
            } else {
                text.makeText(""+value, (float)(x+sizeX/5),(float)(y-sizeY/0.895), (float)(4.5*sizeY), new float[]{0.203921569f,0.278431373f,0.380392157f});
            }
		    text.flush();
        }
    }

    public double[] getPos() {
        return new double[] {this.x,this.y};
    }

    public double[] getSize() {
        double[] size = {sizeX, sizeY};
        return size;
    }

    public void selected(boolean isIt) {
        this.selected = isIt;
    }

    private void setColour(){
        if (selected) {
            selectedcolour();
        } else if (snumber){ 
            samenumber();
        } else if (touching){ 
            fieldIsTouching();
        } else {
            whiteColour();
        }
    }
    
    private void selectedcolour(){
        colour[0] = 0.733333333f;
        colour[1] = 0.870588235f; 
        colour[2] = 0.984313725f;
    }
    private void samenumber(){
        colour[0] = 0.764705882f;
        colour[1] = 0.843137255f; 
        colour[2] = 0.917647059f;
    }
    private void fieldIsTouching(){
        colour[0] = 0.882352941f;
        colour[1] = 0.921568627f; 
        colour[2] = 0.952941176f;
    }
    private void whiteColour(){
        colour[0] = 1.0f;
        colour[1] = 1.0f; 
        colour[2] = 1.0f;
    }
    public boolean isSelected(){  
        return selected;
    }

    public Field getField(){
        return field;
    }

    public void setXY(double[] xy){
        this.x = xy[0];
        this.y = xy[1];
    }

    // What we wanna do
    // Be able to paste a image on it with clipping mask
    // Be able to paste text on it
    // Store coords for both screen coords and sudokuboard coords
    // It needs to hold size, force aspect ratio
    // It needs to be able to be attached to a Window

}