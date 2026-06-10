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

public class TextFieldButton implements Element{
    
    private CreateString text;
    private Shader fontShader;
    private String startString = "";
    private String input = "";
    private float xPos;
    private float yPos;
    private float scale;
    private float[] rgb;

    private boolean heldOver;
    private boolean selected = false;
    private boolean isValid;

    //even = x, odd = y
    public float[] quadPos = new float[8];
    
    public TextFieldButton(CreateString text, Shader fontShader, String input, float xPos, float yPos, float scale, float[] rgb, float width, float hight){
        this.text = text;
        this.fontShader = fontShader;
        this.startString = input;
        this.xPos = xPos;
        this.yPos = yPos;
        this.scale = scale;
        this.rgb = rgb;


        //perameter for textField outline
        quadPos[0] = -(xPos-0.01f);
        quadPos[1] = yPos;

        quadPos[2] = -(xPos-0.01f);
        quadPos[3] = yPos+hight;

        quadPos[4] = (xPos-0.01f);
        quadPos[5] = yPos+hight;

        quadPos[6] = (xPos-0.01f);
        quadPos[7] = yPos;
    }

    public boolean isHeldOver() {
        return heldOver;
    }

    public void heldOver(boolean isHeltOver){
        heldOver = isHeltOver;
    }
    
    public void setSelected(boolean isSelected){
        selected = isSelected;
    }
    public boolean isSelected(){
        return selected;
    }

    public String getInput() {
        return input;
    }

    public boolean getValidity() {
        return isValid;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public void updateInput(char input){
        this.input += input;
    }
    public void updateInput(){
        if (!input.isEmpty()) {
            input = input.substring(0, input.length() - 1);
        }
        System.out.println(input);
    }

    public void draw(){
        fontShader.detach();

        glBegin(GL_QUADS);
        //light grey or dark grey
        glColor3d( selected ? 0.5f: 0.82745f, selected ? 0.5f : 0.82745f, selected ? 0.5f : 0.82745f);
        glVertex2d(quadPos[0], quadPos[1]);
        glVertex2d(quadPos[2], quadPos[3]);
        glVertex2d(quadPos[4], quadPos[5]);
        glVertex2d(quadPos[6], quadPos[7]);
        
        
        glEnd();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        if(input.equals("4") || input.equals("9") ||input.equals("16")|| input.equals("25")){
            text.makeText(startString+input, xPos, yPos, scale, new float[]{0.0f,1.0f,0.0f});
            isValid = true;
        }else{
            text.makeText(startString+input, xPos, yPos, scale, rgb);
            isValid = false;
        }
    
        text.flush();
    }
}
