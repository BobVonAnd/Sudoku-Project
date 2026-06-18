package com.sudoku.view.elements;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

import com.sudoku.view.CreateString;
import com.sudoku.view.Shader;

public class MenuButton implements Element {
    private double size;
    private double x,y,half,ten;
    private boolean heldOver;
    private double startTime = System.currentTimeMillis();
    private String textString;
    private CreateString text;
    private Shader fontShader;
    private boolean toggle;

    public MenuButton(double x, double y, double size, CreateString text, Shader fontShader, String textString) {   
        this.size = size;

        this.half = -size/2;
        this.x = x;
        this.y = y;
        this.ten = -size*.08;
        this.text = text;
        this.textString = textString;
        this.fontShader = fontShader;
    }

    public void setToggle(boolean isOn){
        toggle = isOn;
    }

    public boolean getToggle(){
        return toggle;
    }

    public double[] getPos() {
        return new double[] {this.x,this.y};
    }

    public double getSize() {
        return this.size;
    }

    public void heldOver(boolean isIt) {
        this.heldOver = isIt;
    }

    public boolean isHeldOver() {
        return heldOver;
    }

    public void draw() {
        double currentTime = System.currentTimeMillis() - startTime;
        double spd = 0.01;
        double overSpd = 0.05;
        double xOffset;
        double yOffset;

        fontShader.detach();
        glBegin(GL_QUADS);
        if(heldOver){
            glColor3d(0.0, 0.0, 0.8);
            xOffset = (Math.sin(currentTime * spd)) / 250 ;
            yOffset = ((Math.sin(currentTime * spd) + Math.cos(currentTime * spd))) / 250;
        }else if(toggle){
            glColor3d(1.0, 0.9216, 0.1294);
            xOffset = (Math.sin(currentTime * spd)) / 250 ;
            yOffset = ((Math.sin(currentTime * spd) + Math.cos(currentTime * spd))) / 250;
        }else{
            glColor3d(0.0, 0.0, 0.8);
            xOffset = 0;
            yOffset = 0;
        }
        
        glVertex2d(x - half - ten*2 + ten + xOffset, y - half + ten - yOffset);
        glVertex2d(x + half + ten - xOffset, y - half + ten + yOffset);
        glVertex2d(x + half + ten*2 + ten + xOffset, y + half + ten - yOffset);
        glVertex2d(x - half + ten - xOffset, y + half + ten + yOffset);

        glColor3d(0.2, 0.3, 0.7);
        glVertex2d(x - half - ten*2 + xOffset *overSpd, y - half - yOffset *overSpd);
        glVertex2d(x + half - xOffset *overSpd, y - half + yOffset *overSpd); 
        glVertex2d(x + half + ten*2 + xOffset *overSpd, y + half - yOffset *overSpd);
        glVertex2d(x - half - xOffset *overSpd, y + half + yOffset *overSpd);

        glEnd();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        text.makeText(textString, (float)(x+half+size/4.3),(float)(y+half+size/8), (float)(1.5*1.75*size), heldOver ? new float[]{1.0f,1.0f,0.0f} : new float[]{1.0f,1.0f,1.0f} );
		text.flush();
    }
}
