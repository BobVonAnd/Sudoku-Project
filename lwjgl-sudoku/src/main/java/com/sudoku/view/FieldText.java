package com.sudoku.view;

import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glVertexPointer;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.stb.STBEasyFont;

public class FieldText {

    public FieldText() {

    }

    public void renderText(String text) {
        // ByteBuffer buffer = ByteBuffer.allocateDirect(9999); // buffer til vertex data
        // int quads = STBEasyFont.stb_easy_font_print(200, 450, text, null, buffer);

        // glEnableClientState(GL_VERTEX_ARRAY);//lets LWJGL read buffer
        // glVertexPointer(2, GL_FLOAT, 16, buffer); 
        // glDrawArrays(GL_QUADS, 0, quads * 4);
        // glDisableClientState(GL_VERTEX_ARRAY);

        
    }
}
