package com.sudoku.view;

import org.joml.Matrix4f;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL31.GL_TEXTURE_BUFFER;

import com.sudoku.view.font.CharInfo;
import com.sudoku.view.font.CreateFont;

//borrowed from GamesWithGabe and edited a tiny bit
public class CreateString {
    
    private float WINDOWX = 1280f;
    private float WINDOWY = 720f;
    private CreateFont font;
    private Shader shader;
    private int vao;
    private int vbo;
    private float startaspect;
    private float normalizedAspect = 1;

    private int[] indices = {
        0, 1, 3,
        1, 2, 3
    };
    //size of number of chars*4 = 25
    public static int BATCH_SIZE = 100;
    public static int VERTEX_SIZE = 7;
    private int size = 0;
    private float [] vertices = new float[BATCH_SIZE*VERTEX_SIZE];
    private Matrix4f projection = new Matrix4f();


    public CreateString(Shader shader, CreateFont font, int width, int height){
        this.shader = shader;
        this.font = font;
        startaspect = WINDOWX/WINDOWY;
        setXY(width, height);
        createBatch();
    }

    //makes the string into chars and run for each
    public void makeText(String text, float x, float y, float  scale, float [] rgb){
        for(char i : text.toCharArray()){
            CharInfo charInfo = font.getChar(i);
            float xPos = x;
            float yPos = y;
           
            processChar(charInfo, scale, xPos, yPos, rgb);
            x += (charInfo.width/(WINDOWX*normalizedAspect)) * scale;
        }
    }

    //uploads and run shader and draws
    public void flush(){

        //uploads VBO
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, Float.BYTES * VERTEX_SIZE * BATCH_SIZE, GL_DYNAMIC_DRAW);
        glBufferSubData(GL_ARRAY_BUFFER, 0 , vertices);

        //compiles shader and gives inputs in shader class
        shader.use();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_BUFFER, font.textureId);
        shader.uploadTexture("uFontTexture", 0);
        shader.uploadMat4f("uProjection", projection);

		glBindVertexArray(vao);
        //draws as triangles
		glDrawElements(GL_TRIANGLES, (size/4) * 6, GL_UNSIGNED_INT, 0);

        size = 0;
    }

    private void processChar(CharInfo charInfo, float scale, float x, float y, float [] rgb){

        //if batch is full upload
        if(size >= BATCH_SIZE - 4){
            flush();
        }

        //pos
        float x0 = x;
        float y0 = y;
        float x1 = x + scale * (charInfo.width/(WINDOWX*normalizedAspect));
        float y1 = y + scale * (charInfo.height/WINDOWY);

        //color
        float r = rgb[0];
        float g = rgb[1];
        float b = rgb[2];
        
        //texturecoord
        float ux0 = charInfo.textureCoord[1].x;
        float ux1 = charInfo.textureCoord[0].x;
        float uy0 = charInfo.textureCoord[0].y;
        float uy1 = charInfo.textureCoord[1].y;

        //first point in square
        int index = size * 7; 
        vertices[index] = x1; vertices[index+1] = y0;
        vertices[index+2] = r; vertices[index+3] = g; vertices[index+4] = b; 
        vertices[index+5] = ux1; vertices[index+6] = uy0;

        //second point in square
        index += 7;
        vertices[index] = x1; vertices[index+1] = y1;
        vertices[index+2] = r; vertices[index+3] = g; vertices[index+4] = b; 
        vertices[index+5] = ux1; vertices[index+6] = uy1;

        //third point in square
        index += 7;
        vertices[index] = x0; vertices[index+1] = y1;
        vertices[index+2] = r; vertices[index+3] = g; vertices[index+4] = b; 
        vertices[index+5] = ux0; vertices[index+6] = uy1;

        //forth point in square
        index += 7;
        vertices[index] = x0; vertices[index+1] = y0;
        vertices[index+2] = r; vertices[index+3] = g; vertices[index+4] = b; 
        vertices[index+5] = ux0; vertices[index+6] = uy0;

        //next char
        size += 4; 
    }

    private void createBatch(){
        //make the world into -1 to 1 width and -1 to 1 height and 1 to 100 depth
        projection.identity();
        projection.ortho(-1, 1, -1, 1, 1f, 100f);

        //make vao ID and use it
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        //set up VBO and use it
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, Float.BYTES*VERTEX_SIZE*BATCH_SIZE, GL_DYNAMIC_DRAW);

        //Creates the EBO
        generateEBO();

        //setup the values the shader gets
        int stride = 7 * Float.BYTES;
        glVertexAttribPointer(0, 2, GL_FLOAT, false, stride, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, stride, 2 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, 2, GL_FLOAT, false, stride, 5 * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    //sets up the correct indexes for the vertcies 
    private void generateEBO(){
        //batchSize * points of the triangle
        int eboSize = BATCH_SIZE * 3;
        int[] eBuffer = new int[eboSize];

        for(int i = 0; i < eboSize; i++){
            eBuffer[i] = indices[(i%6)] + ((i/6)*4);
        }

        //use the EBO
        int ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, eBuffer, GL_STATIC_DRAW);
    }

    //resize the aspect for the screen size
    public void setXY(int width, int height){
        float aspect = (float)width/(float)height;
        normalizedAspect = aspect/startaspect;
    }
}
