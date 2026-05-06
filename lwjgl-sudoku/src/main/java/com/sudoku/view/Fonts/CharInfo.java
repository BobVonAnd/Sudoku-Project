// package com.sudoku.view.fonts;

// import org.joml.Vector2f;

//Borrowed form GamesWithGabe and edited
public class CharInfo {
    
    public int x;
    public int y;
    public int width;
    public int height;
    public float yOfSet = 94.8125f - 67.875f+1;

//      public Vector2f[] textureCoord = new Vector2f[4];


    public CharInfo(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        // this.yOfSet = 
    }

    public void calTextureCoord(int fontWidth, int fontHeight){
        float x0 = (float)x / (float)fontWidth;
        float x1 = ((float)x + width) / (float)fontWidth;
        float y0 = ((float)y - (height-67.875f)) / (float)fontHeight;
        float y1 = (float)(y+yOfSet) / (float)fontHeight;

     
        textureCoord[0] = new Vector2f(x1, y1);
        textureCoord[1] = new Vector2f(x0, y0);
    }
}
