package com.sudoku.view.fonts;

import org.joml.Vector2f;

public class CharInfo {
    
    public int x;
    public int y;
    public int width;
    public int height;

     public Vector2f[] textureCoord = new Vector2f[4];


    public CharInfo(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void calTextureCoord(int fontWidth, int fontHeight){
        float x0 = (float)x / (float)fontWidth;
        float x1 = ((float)x + width) / (float)fontWidth;
        float y0 = (float)y / (float)fontHeight;
        float y1 = ((float)y - height) / (float)fontHeight;

        textureCoord[0] = new Vector2f(x1, y0);
        textureCoord[1] = new Vector2f(x1, y1);
        textureCoord[2] = new Vector2f(x0, y1);
        textureCoord[3] = new Vector2f(x0, y0);
    }
}
