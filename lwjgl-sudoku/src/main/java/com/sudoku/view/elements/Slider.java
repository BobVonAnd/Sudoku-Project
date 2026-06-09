package com.sudoku.view.elements;
import static org.lwjgl.opengl.GL11.*;

import com.sudoku.controller.windows.playSudokuWindow;
import com.sudoku.view.CreateString;
import com.sudoku.view.Shader;
import com.sudoku.view.fonts.CreateFont;
import org.lwjgl.glfw.GLFW;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;

public class Slider implements Element {
    private double size;
    private double x,y,half,ten;
    private double startTime = System.currentTimeMillis();
    private String prefixTextString, suffixTextString;
    private CreateString text;
    private Shader fontShader;
    private boolean heldOver, pickerHeldOver, mbLeftHeld, pickerDragging;
    private double mouseX, mouseY, screenWidth, screenHeight, width, height, ySquareVelocity, ySquareRot, sliderX;
    private double ySquareSpdMax = 0.6*Math.PI;
    private double ySquareSpd = ySquareSpdMax;
    private double diamondScale = 2;
    private long lastFrameTime = System.nanoTime();
    private double deltaTime = 0.0;
    private double pickerBarWidth;

    public Slider(double x, double y, double mouseX, double mouseY, double screenWidth, double screenHeight, double width, double size, CreateString text, Shader fontShader, String prefixTextString, String suffixTextString) {   
        ///                          MOUSE POS SHOULD BE SCREEN POS
        this.size = size;
        this.half = width/2;
        this.x = x;
        this.y = y;
        this.ten = size*.04;
        this.text = text;
        this.prefixTextString = prefixTextString;
        this.fontShader = fontShader;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.width = width;
        this.pickerBarWidth = width*0.95;
        this.mbLeftHeld = false;
        this.suffixTextString = suffixTextString;
    }

    public void update(double mouseX, double mouseY, double screenWidth, double screenHeight, boolean mbLeftHeld) {
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.mbLeftHeld = mbLeftHeld;
    }

    public double[] getPos() {
        return new double[] {this.x,this.y};
    }

    public double getSize() {
        return this.size;
    }
    
    private double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }

    public double getValue() {
        double min = x + (width - pickerBarWidth) / 2;
        double max = x + pickerBarWidth + (width - pickerBarWidth) / 2;
        double value = ((sliderX - min) / (max - min));
        value = Math.round(value * 100.0) / 100.0;
        if (value >= 1) {
            value = 1;
        } else if (value <= 0) {
            value = 0;
        }
        return value;
    }

    public void draw() {
        double beforeMove = getValue();
        long now = System.nanoTime();
        deltaTime = (now - lastFrameTime) / 1_000_000_000.0; // seconds
        lastFrameTime = now;

        double mouseXt = mouseX/(screenWidth/2)-1;
        double mouseYt = -mouseY/(screenHeight/2)+1;
        double aspect = screenWidth / screenHeight;
        double pickerX = x - pickerBarWidth/2;
        double pickerRadiusX = (ten * diamondScale) / aspect;
        double pickerRadiusY = ten * diamondScale;

        if (pickerDragging) {
            sliderX = mouseXt-x + pickerBarWidth/2;
        }
        if (sliderX > x+pickerBarWidth + (width-pickerBarWidth)/2 ) {
            sliderX = x+pickerBarWidth + (width-pickerBarWidth)/2;
        } else if (sliderX < x+ (width-pickerBarWidth)/2) {
            sliderX = x + (width-pickerBarWidth)/2;
        }

        pickerHeldOver = 
            mouseXt >= pickerX - pickerRadiusX + sliderX && 
            mouseXt <= pickerX + pickerRadiusX + sliderX && 
            mouseYt >= y - pickerRadiusY &&
            mouseYt <= y + pickerRadiusY;
        
        double left = x - half;
        double right = x + half + ten;
        double bottom = y - ten;
        double top = y + ten;

        heldOver =
            mouseXt >= left &&
            mouseXt <= right &&
            mouseYt >= bottom &&
            mouseYt <= top;

        double currentTime = System.currentTimeMillis() - startTime;
        double spd = 0.01;
        double overSpd = 0.15;
        double xOffset = (heldOver || pickerHeldOver ? (Math.sin(currentTime * spd)) : 0) / 500 ;
        double yOffset = (heldOver || pickerHeldOver ? (Math.sin(currentTime * spd) + Math.cos(currentTime * spd)) : 0) / 500;
        if (mbLeftHeld && pickerHeldOver) {
            pickerDragging = true;
        } else if (mbLeftHeld == false) {
            pickerDragging = false;
        }
        fontShader.detach();
        glBegin(GL_QUADS);
        // behind
        glColor3d(0.0, 0.0, 0.3);
        glVertex2d(x - half - ten + xOffset, y - ten - ten/1.5 - yOffset);
        glVertex2d(x + half - ten - xOffset, y - ten - ten/1.5 + yOffset); 
        glVertex2d(x + half - ten + xOffset+ten, y + ten - ten/1.5 - yOffset);
        glVertex2d(x - half - ten - xOffset+ten, y + ten - ten/1.5 + yOffset);
        // front
        glColor3d(0.2, 0.3, 0.7);
        glVertex2d(x - half + xOffset *overSpd, y - ten - yOffset *overSpd);
        glVertex2d(x + half - xOffset *overSpd, y - ten + yOffset *overSpd); 
        glVertex2d(x + half + xOffset *overSpd+ten, y + ten - yOffset *overSpd);
        glVertex2d(x - half - xOffset *overSpd+ten, y + ten + yOffset *overSpd);
        // picker
        glColor4d(0, 0, 0, pickerHeldOver ? .7 : .5);
        glVertex2d(x - pickerBarWidth/2 + xOffset*.5 + sliderX, y + (ten*diamondScale) - yOffset*.5);
        glVertex2d(x - pickerBarWidth/2 + (ten*diamondScale) / aspect - xOffset*.5 + sliderX, y + yOffset*.5);
        glVertex2d(x - pickerBarWidth/2 + xOffset*.5 + sliderX, y - (ten*diamondScale) + yOffset*.5);
        glVertex2d(x - pickerBarWidth/2 - (ten*diamondScale) / aspect - xOffset*.5 + sliderX, y - yOffset*.5);
        // picker click yellow square
        glColor4d(1, 0.9, 0.47, pickerHeldOver || pickerDragging ? .7 : 0);
        double halfSize = ten * diamondScale*.75;
        ySquareVelocity = lerp(ySquareVelocity,  pickerHeldOver || pickerDragging ? ySquareSpd * deltaTime : 0, 5*deltaTime);
        ySquareSpd += (getValue() - beforeMove) * 30;
        ySquareSpd = lerp(ySquareSpd, ySquareSpdMax,  deltaTime);
        ySquareRot += ySquareVelocity;

        double cos = Math.cos(ySquareRot);
        double sin = Math.sin(ySquareRot);

        double cx = x - pickerBarWidth/2 + sliderX;
        double cy = y;

        double vx = -halfSize + xOffset * 0.5 * aspect;
        double vy = -halfSize - yOffset * 0.5;
        glVertex2d(
            cx + (vx * cos - vy * sin) / aspect,
            cy + (vx * sin + vy * cos)
        );

        vx = halfSize + xOffset * 0.5 * aspect;
        vy = -halfSize - yOffset * 0.5;
        glVertex2d(
            cx + (vx * cos - vy * sin) / aspect,
            cy + (vx * sin + vy * cos)
        );

        vx = halfSize - xOffset * 0.5 * aspect;
        vy = halfSize + yOffset * 0.5;
        glVertex2d(
            cx + (vx * cos - vy * sin) / aspect,
            cy + (vx * sin + vy * cos)
        );

        vx = -halfSize - xOffset * 0.5 * aspect;
        vy = halfSize + yOffset * 0.5;
        glVertex2d(
            cx + (vx * cos - vy * sin) / aspect,
            cy + (vx * sin + vy * cos)
        );

        glEnd();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        text.makeText(prefixTextString + String.valueOf(getValue()) + suffixTextString, (float)(x-pickerBarWidth/2),(float)(y+.05), (float)(size*.5), new float[]{0.0f,0.0f,0.0f} );
		text.flush();
    }
}
