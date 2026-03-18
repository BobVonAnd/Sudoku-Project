package com.sudoku.view;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL45.glCreateVertexArrays;

public class TestShader {
    
    private String vertexShaderSrc = "#version 330 core\r\n" + //
                "\r\n" + //
                "layout (location = 0) in vec3 aPos;\r\n" + //
                "layout (location = 1) in vec4 aColor;\r\n" + //
                "\r\n" + //
                "out vec4 fColor;\r\n" + //
                "\r\n" + //
                "void main(){\r\n" + //
                "    fColor = aColor;\r\n" + //
                "    gl_Position = vec4(aPos, 1.0);\r\n" + //
                "}";

    private String fregmentShaderSrc = "#version 330 core \r\n" + //
                "\r\n" + //
                "in vec4 fColor;\r\n" + //
                "\r\n" + //
                "out vec4 color;\r\n" + //
                "\r\n" + //
                "void main(){\r\n" + //
                "    color = fColor; \r\n" + //
                "}";

    
    private  int vertexID, fregmentID, shaderProgram;

    //vertex array object VAO
    //vertex buffer object VBO
    //element buffer object EBO
    private  int vaoID, vboID, eboID;

    private float[] vertexArray = {
        //pos                   //color
         0.5f, -0.5f,  0.0f,      1.0f, 0.0f, 0.0f, 1.0f,     //bottom right
        -0.5f,  0.5f,  0.0f,      0.0f, 1.0f, 0.0f, 1.0f,     //top left 
         0.5f,  0.5f,  0.0f,      0.0f, 0.0f, 1.0f, 1.0f,     //top right
        -0.5f, -0.5f,  0.0f,      1.0f, 0.0f, 1.0f, 1.0f      //bottom left
    };

    //must be counter clock 
    private int[] elementArray = {

        2, 1, 0, //top right triangle
        0, 1, 3  //Bottom left triangle
    
    };


    public void init(){

        //load an compile vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        
        //pass to GPU
        glShaderSource(vertexID, vertexShaderSrc);
        glCompileShader(vertexID);

        //checking for error
        int succes = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if(succes == GL_FALSE){
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.err.println("Error: shader compile fail");
            System.err.println(glGetShaderInfoLog(vertexID, len));
        }

        //____________________________________________________________________

        //load an compile fregment shader
        fregmentID = glCreateShader(GL_FRAGMENT_SHADER);
        
        //pass to GPU
        glShaderSource(fregmentID, fregmentShaderSrc);
        glCompileShader(fregmentID);

        //checking for error
        succes = glGetShaderi(fregmentID, GL_COMPILE_STATUS);
        if(succes == GL_FALSE){
            int len = glGetShaderi(fregmentID, GL_INFO_LOG_LENGTH);
            System.err.println("Error: fregment compile fail");
            System.err.println(glGetShaderInfoLog(fregmentID, len));
        }


        //link shader
        shaderProgram = glCreateProgram(); 
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fregmentID);
        glLinkProgram(shaderProgram);

        //check for linking error
        succes = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if(succes == GL_FALSE){
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.err.println("error link fail");
            System.err.println(glGetProgramInfoLog(fregmentID, len));
        }


        //VAO VBO and EBO
        vaoID = glCreateVertexArrays();
        glBindVertexArray(vaoID);

        //create a float buffer of verticies
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        //create VBO upload the vertex buffer 
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER ,vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);


        //create the indicies and upload

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        //add the vertex attributes pointers
        int positionsSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeInBytes = (positionsSize + colorSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeInBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeInBytes, positionsSize * floatSizeBytes);
        glEnableVertexAttribArray(1);

    }

    public void update()
    {
        //bind shaderProgram
        glUseProgram(shaderProgram);
        //bind VAO
        glBindVertexArray(vaoID);
        
        //enable vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        //unbind all

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        glUseProgram(0);

    }

}
