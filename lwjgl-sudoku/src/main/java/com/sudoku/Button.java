package com.sudoku;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

public class Button {
    private float x, y, width, height;
    private String text;
    private Runnable onClick;

    public Button(float x, float y, float width, float height, String text, Runnable onClick) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.onClick = onClick;
    }

    public void render() {
        glColor3f(0f, 0.5f, 1f);

        glBegin(GL_QUADS);
        glVertex2f(x, y);                // top-left
        glVertex2f(x + width, y);        // top-right
        glVertex2f(x + width, y - height); // bottom-right
        glVertex2f(x, y - height);       // bottom-left
        glEnd();
    }

    public void checkClick(float mouseX, float mouseY) {
        if (mouseX >= x && mouseX <= x + width &&
            mouseY <= y && mouseY >= y - height) {
            if (onClick != null)
                onClick.run();
        }
    }
}