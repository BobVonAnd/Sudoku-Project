package com.sudoku.view;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RED;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTruetype;

public class TestTextShader {

    private int vaoID, vboID, eboID;
    private int shaderProgram, vertexID, fragmentID;
    private int fontTexID;

    private final float[] quadVertices = {
            0f, 0f, 0f, 0f, 0f, // bottom-left
            1f, 0f, 0f, 1f, 0f, // bottom-right
            1f, 1f, 0f, 1f, 1f, // top-right
            0f, 1f, 0f, 0f, 1f  // top-left
    };
    private final int[] quadElements = {0, 1, 2, 2, 3, 0};

    private STBTTBakedChar.Buffer charData; // info om hver karakter i atlas

    private String vertexShaderSrc = "#version 330 core\r\n" + //
                "layout(location = 0) in vec3 aPos;\r\n" + //
                "layout(location = 1) in vec2 aTexCoord;\r\n" + //
                "out vec2 TexCoord;\r\n" + //
                "uniform vec2 offset;\r\n" + //
                "uniform vec2 scale;\r\n" + //
                "uniform vec2 screenSize; \r\n" + //
                "\r\n" + //
                "void main() {\r\n" + //
                "   vec3 pos = aPos;\r\n" + //
                "   vec2 ndc = offset / screenSize * 2.0 - 1.0;\r\n" + //
                "   pos.xy = pos.xy * scale / screenSize * 2.0 + ndc;\r\n" + //
                "   gl_Position = vec4(pos,1.0);\r\n" + //
                "   TexCoord = aTexCoord;\r\n" + //
                "}";

    private String fragmentShaderSrc = "#version 330 core\n" +
            "in vec2 TexCoord;\n" +
            "out vec4 FragColor;\n" +
            "uniform sampler2D fontTexture;\n" +
            "uniform vec4 textColor;\n" +
            "void main() {\n" +
            "   float alpha = texture(fontTexture, TexCoord).r;\n" +
            "   FragColor = vec4(textColor.rgb, textColor.a * alpha);\n" +
            "}";

    public void init(String ttfPath, int fontPixelHeight) throws IOException {
        // --- Load TTF file ---
        byte[] bytes = Files.readAllBytes(Paths.get(ttfPath));
        ByteBuffer fontBuffer = BufferUtils.createByteBuffer(bytes.length);
        fontBuffer.put(bytes);
        fontBuffer.flip();
        // --- Create shader ---
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, vertexShaderSrc);
        glCompileShader(vertexID);
        checkShader(vertexID, "VERTEX");

        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentShaderSrc);
        glCompileShader(fragmentID);
        checkShader(fragmentID, "FRAGMENT");

        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);
        checkProgram(shaderProgram);

        // --- VAO/VBO/EBO ---
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(quadVertices.length);
        vertexBuffer.put(quadVertices).flip();

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_DYNAMIC_DRAW); // dynamic because UV changes per char

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(quadElements.length);
        elementBuffer.put(quadElements).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        glVertexAttribPointer(0,3,GL_FLOAT,false,5*4,0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1,2,GL_FLOAT,false,5*4,3*4);
        glEnableVertexAttribArray(1);

        // --- Create font texture ---
        fontTexID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, fontTexID);
        int bitmapWidth = 512;
        int bitmapHeight = 512;
        ByteBuffer bitmap = BufferUtils.createByteBuffer(bitmapWidth * bitmapHeight);

        charData = STBTTBakedChar.malloc(96); // 96 printable ASCII
        STBTruetype.stbtt_BakeFontBitmap(fontBuffer, fontPixelHeight, bitmap, bitmapWidth, bitmapHeight, 32, charData);

        glTexImage2D(GL_TEXTURE_2D,0,GL_RED,bitmapWidth,bitmapHeight,0,GL_RED,GL_UNSIGNED_BYTE,bitmap);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    }

    private void checkShader(int shader, String type) {
        if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE)
            System.err.println(type + " shader compile error: " + glGetShaderInfoLog(shader));
    }

    private void checkProgram(int program) {
        if (glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE)
            System.err.println("Program link error: " + glGetProgramInfoLog(program));
    }

    // Tegn tekst
    public void drawText(String text, float x, float y, float scale, float[] color) {
        
        float screenWidth = 1280f;
        float screenHeight = 720f;

        // konverter pixel coords til -1..1
        float xNorm = (x / screenWidth) * 2f - 1f;
        float yNorm = 1f - (y / screenHeight) * 2f;

        glUseProgram(shaderProgram);
        glBindVertexArray(vaoID);
        glBindTexture(GL_TEXTURE_2D, fontTexID);

        int offsetLoc = glGetUniformLocation(shaderProgram,"offset");
        int scaleLoc = glGetUniformLocation(shaderProgram,"scale");
        int colorLoc = glGetUniformLocation(shaderProgram,"textColor");

        float startX = x;
        for (char c : text.toCharArray()) {
            if (c < 32 || c > 126) continue; // skip non-printable
            STBTTBakedChar g = charData.get(c - 32);
            float[] uv = {g.x0()/512f, g.y0()/512f, g.x1()/512f, g.y1()/512f};

            // Tegn quad for dette tegn
            glUniform2f(offsetLoc, x + g.xoff()*scale, y - g.yoff()*scale);
            glUniform2f(scaleLoc, g.x1() - g.x0(), g.y1() - g.y0());
            glUniform4f(colorLoc,color[0],color[1],color[2],color[3]);

            // Update UV
            FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(quadVertices.length);
            float[] temp = quadVertices.clone();
            temp[3] = uv[0]; temp[4] = uv[1];      // bottom-left
            temp[8] = uv[2]; temp[9] = uv[1];      // bottom-right
            temp[13] = uv[2]; temp[14] = uv[3];    // top-right
            temp[18] = uv[0]; temp[19] = uv[3];    // top-left
            vertexBuffer.put(temp).flip();
            glBindBuffer(GL_ARRAY_BUFFER,vboID);
            glBufferSubData(GL_ARRAY_BUFFER,0,vertexBuffer);

            glDrawElements(GL_TRIANGLES,quadElements.length,GL_UNSIGNED_INT,0);

            x += g.xadvance()*scale; // move to next character
        }
    }
}
