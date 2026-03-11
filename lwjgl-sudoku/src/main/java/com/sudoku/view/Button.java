package com.sudoku.view;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

public class Button {

    private int x,y;
    private final int hight = 30;
    private final int width = 30;
    private final int space = 40;
    private int startPosX = 480; // 1280/2 - (space+width) * 4 (4 being 4 button down from the middle)
    private int startPosY = 210;  //same logic
    private float[] color = new float[3];

    public Button(int x, int y){

        switch (x/3) {
            case 0:
                color[0] += 0f;
                break;
            case 1:
                color[1] += 0.5f;
                break;
            case 2:
                color[2] += 1f;
            default:
                break;
        }
        switch (y/3) {
            case 0:
                color[0] += 0f;
                break;
            case 1:
                color[1] += 0.5f;
                break;
            case 2:
                color[2] += 0.5f;
            default:
                break;
        }


        this.x = startPosX + x * space;
        this.y = startPosY + y * space;

    }

    public void rendering(){

        glColor3f(color[0], color[1], color[2]);
        glBegin(GL_QUADS); 
        glVertex2f(x, y); 
        glVertex2f(x+width, y);
        glVertex2f(x+width, y+hight);
        glVertex2f(x, y+hight);
        glEnd();

    }
    
}
