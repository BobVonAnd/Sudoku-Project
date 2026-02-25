package com.sudoku.model;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

public class Field {
    private int value, x, y;
    private List<Integer> legalEntries = new ArrayList<>();


    public Field(int x, int y, int value, int size, double buttomSize){
        this.x = x;
        this.y = y;
        this.value = value;
        for(int i = 1; i <= size; i++){
            legalEntries.add(i);
        }
    }

    public Field()
    {

    }

    public void setValue(int value){
        this.value = value;
    }

    public void createButtom(){
        int buttonX = 100;
        int buttonY = 100;
        int buttonWidth = 200;
        int buttonHeight = 50;
        glColor3f(1, 0, 0);
        glBegin(GL_QUADS);
        glVertex2f(buttonX, buttonY);
        glVertex2f(buttonX + buttonWidth, buttonY);
        glVertex2f(buttonX + buttonWidth, buttonY + buttonHeight);
        glVertex2f(buttonX, buttonY + buttonHeight);
        glEnd();
    }


}


