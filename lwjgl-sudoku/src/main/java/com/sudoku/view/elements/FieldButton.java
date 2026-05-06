package com.sudoku.view.elements;

import com.sudoku.model.Field;

import com.sudoku.model.SudokuBoard;
import static org.lwjgl.opengl.GL11.*;

public class FieldButton implements Element {
    private Field field;
    private int size;
    private SudokuBoard sudokuBoard;
    private double x,y;

    public FieldButton(Field f, int size, SudokuBoard sudokuBoard) {
        this.field = f;    
        this.size = size;
        this.sudokuBoard = sudokuBoard;
    }
    

    public void draw() {
        double size = 0.9 / sudokuBoard.getSize();
		double gap = size * 0.1; // spacing

		double half = size - gap;

        double row = this.field.getCoordinates()[0];
        double col = this.field.getCoordinates()[1];

        this.x = -0.9 + col * size * 2 + size;
        this.y = -0.9 + row * size * 2 + size;
        glBegin(GL_QUADS);

        glColor3d(this.field.getRed(), this.field.getGreen(), this.field.getBlue());
        glVertex2d(x - half, y - half-size/100); //Bottom left
        glVertex2d(x + half, y - half-size/100); //Bottom right
        glVertex2d(x + half, y + half); //Top Right
        glVertex2d(x - half, y + half); //Top Left

        glEnd();
    }


    // What we wanna do
    // Be able to paste a image on it with clipping mask
    // Be able to paste text on it
    // Store coords for both screen coords and sudokuboard coords
    // It needs to hold size, force aspect ratio
    // It needs to be able to be attached to a Window

}