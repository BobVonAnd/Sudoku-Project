package com.sudoku.view.fonts;

import org.joml.Vector2f;

//Borrowed form GamesWithGabe and edited
public class CharInfo {
    
    public int x;
    public int y;
    public int width;
    public int height;
    public float yOfSet;


     public Vector2f[] textureCoord = new Vector2f[4];


    public CharInfo(int x, int y, int width, int height, float ofset){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.yOfSet = ofset;
    }

    public void calTextureCoord(int fontWidth, int fontHeight){
        float x0 = (float)x / (float)fontWidth;
        float x1 = ((float)x + width) / (float)fontWidth;
        float y0 = ((float)y - height) / (float)fontHeight;
        float y1 = (float)(y) / (float)fontHeight;

   //  - (100)
   //+30
        textureCoord[0] = new Vector2f(x1, y1);
        textureCoord[1] = new Vector2f(x0, y0);
    }
}
