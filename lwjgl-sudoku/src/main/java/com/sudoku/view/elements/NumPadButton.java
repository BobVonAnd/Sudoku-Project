package com.sudoku.view.elements;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glVertex2d;
import static org.lwjgl.opengl.GL11.glVertex2f;

import com.sudoku.view.CreateString;
import com.sudoku.view.Shader;

public class NumPadButton implements Element{
    
    private CreateString text;
    private Shader fontShader;
    private String startString = "";
    private String input = "";
    private float x;
    private float y;
    private float scale = 0.4f;
    private float[] rgb = new float[]{1.0f,1.0f,1.0f};

    private boolean numPadAnimate = false;
    private double startTime = System.currentTimeMillis();

    private float width;
    private float height;

    private float textOfSetY;
    private float textOfSetX;

    private boolean[] selected = new boolean[]{
        false,true,false,
        true,false,true,
        false,true,false,
        false,false,false
    };
    private int indexSelec;

    public NumPadButton(float x, float y, float width, float height, CreateString text, Shader fontShader){
        this.text = text;
        this.fontShader = fontShader;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        textOfSetY = -0.03777778f + height;
        textOfSetX = -0.066f + width;

    }

    public float getWidth(){
        return width;
    }
    public float getHeight(){
        return height;
    }
    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }
    public boolean[] isSelected(){
        return selected;
    }
    public int getIndexSelec(){
        return indexSelec;
    }


    public void draw(){
        fontShader.detach();
        makeQuads();
        makeLines();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        makeTexts();
        
    }
    
    public void setSelected(int index, boolean isSelected){
        selected[index] = isSelected;
        if(isSelected){
            indexSelec = index;
        }
    }

    private void makeLines() {
        fontShader.detach();

        glLineWidth(1.0f);
        glBegin(GL_LINES);

        glColor3d(0f, 0f, 0.4f);

        // vertical lines
        glVertex2f(x, y);
        glVertex2f(x, y - height * 4);

        glVertex2f(x + width, y);
        glVertex2f(x + width, y - height * 4);

        glVertex2f(x + width * 2, y);
        glVertex2f(x + width * 2, y - height * 4);

        glVertex2f(x + width * 3, y);
        glVertex2f(x + width * 3, y - height * 4);

        // horizontal lines
        glVertex2f(x - 0.001f, y);
        glVertex2f(x + width * 3, y);

        glVertex2f(x, y - height);
        glVertex2f(x + width * 3, y - height);

        glVertex2f(x, y - height * 2);
        glVertex2f(x + width * 3, y - height * 2);

        glVertex2f(x, y - height * 3);
        glVertex2f(x + width * 3, y - height * 3);

        glVertex2f(x, y - height * 4);
        glVertex2f(x + width * 3, y - height * 4);

        glEnd();
    }

    private void makeTexts() {

        text.makeText("1", x + textOfSetX, y - textOfSetY, scale, rgb);
        text.flush();

        text.makeText("2", x + textOfSetX + width, y - textOfSetY, scale, rgb);
        text.flush();

        text.makeText("3", x + textOfSetX + width * 2, y - textOfSetY, scale, rgb);
        text.flush();

        text.makeText("4", x + textOfSetX, y - textOfSetY - height, scale, rgb);
        text.flush();

        text.makeText("5", x + textOfSetX + width, y - textOfSetY - height, scale, rgb);
        text.flush();

        text.makeText("6", x + textOfSetX + width * 2, y - textOfSetY - height, scale, rgb);
        text.flush();

        text.makeText("7", x + textOfSetX, y - textOfSetY - height * 2, scale, rgb);
        text.flush();

        text.makeText("8", x + textOfSetX + width, y - textOfSetY - height * 2, scale, rgb);
        text.flush();

        text.makeText("9", x + textOfSetX + width * 2, y - textOfSetY - height * 2, scale, rgb);
        text.flush();

        text.makeText("0", x + textOfSetX, y - textOfSetY - height * 3, scale, rgb);
        text.flush();

        text.makeText("<<", x + textOfSetX + width, y - textOfSetY - height * 3, scale, rgb);
        text.flush();

        text.makeText("OK", x + textOfSetX + width * 2, y - textOfSetY - height * 3, scale, rgb);
        text.flush();
    }

    private void makeQuads() {

        double currentTime = System.currentTimeMillis() - startTime;
        double spd = 0.01;

        float xOffset = numPadAnimate ? (float)(Math.sin(currentTime * spd) / 500.0) : 0f;
        float yOffset = numPadAnimate ? (float)((Math.sin(currentTime * spd) + Math.cos(currentTime * spd)) / 500.0) : 0f;

        float totalWidth = width * 3;
        float totalHeight = height * 4;
        float height_offset = -totalHeight * 0.05f;
        float width_offset = -totalWidth * 0.05f;
        glBegin(GL_QUADS);

        // Wiggle box behind numpad
        glColor3d(0.0f, 0.0f, numPadAnimate ? 0.8f : 0.4f);

        glVertex2f(x - 0.01f + xOffset + width_offset, y + 0.01f - yOffset + height_offset);
        glVertex2f(x - 0.01f - xOffset + width_offset, y - totalHeight + yOffset + height_offset);
        glVertex2f(x + totalWidth + 0.01f + xOffset + width_offset, y - totalHeight - yOffset + height_offset);
        glVertex2f(x + totalWidth + 0.01f - xOffset + width_offset, y + 0.01f + yOffset + height_offset);

        numPadAnimate = false;
        
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 3; col++) {

                int index = row * 3 + col;
                glColor3d(selected[index] ? 0f : 0.2, selected[index] ? 0f : 0.3, selected[index] ? 0.4f : 0.7);
                if (selected[index]) {
                    numPadAnimate = selected[index];
                }
                float left = x + (width * col);
                float right = left + width;

                float top = y - (height * row);
                float bottom = top - height;

                glVertex2d(left, top);
                glVertex2d(left, bottom);
                glVertex2d(right, bottom);
                glVertex2d(right, top);
            }
        }

        glEnd();
    }
}
