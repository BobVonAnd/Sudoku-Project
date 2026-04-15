package com.sudoku.view;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import com.sudoku.view.fonts.CharInfo;
import com.sudoku.view.fonts.CreateFont;

//borrowed from GamesWithGabe
public class CreateString {
    
    private CreateFont font;
    private Shader shader;
    private int vao;
    private int vbo;

    private int[] indices = {
        0, 1, 3,
        1, 2, 3
    };
    
    private int batchSize = 100;
    private int size = 0;
    private int vertexSize = 7;
    private double[] verticies = new double[batchSize*vertexSize];

    public void makeText(String text, int x, int y, double scale, int rgb){

        for(char i : text.toCharArray()){
            CharInfo charInfo = font.getChar(i);
            double xPos = x;
            double yPos = y;
            processChar(charInfo, scale, xPos, yPos, rgb);
        }
    }

    private void processChar(CharInfo charInfo, double scale, double x, double y, int rgb){
        //pos
        double x0 = x;
        double y0 = y;
        double x1 = x + scale * charInfo.width;
        double y1 = y + scale * charInfo.height;

        double ux0 = charInfo.textureCoord[]
    }

    private void createBatch(){
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, Float.BYTES*vertexSize*batchSize, GL_STATIC_DRAW);

        generateEBO();

        int stride = 7 * Float.BYTES;
        glVertexAttribPointer(0, 2, GL_FLOAT, false, stride, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, stride, 2 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, 2, GL_FLOAT, false, stride, 5 * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    private void generateEBO(){
        //batchSize * points of the triangle
        int EboSize = batchSize * 3;
        int[] eBuffer = new int[EboSize];

        for(int i = 0; i < eBuffer.length; i++){
            eBuffer[i] = indices[(i%6)] + ((i/6)*4);
        }


        int ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
    }

   

}
