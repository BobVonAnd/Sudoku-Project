package com.sudoku.controller.windows;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_0;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_9;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

import com.sudoku.controller.Window;
import com.sudoku.controller.WindowInterface;
import com.sudoku.controller.WindowManager;
import com.sudoku.view.CreateString;
import com.sudoku.view.Shader;
import com.sudoku.view.elements.NumPadButton;
import com.sudoku.view.elements.TextFieldButton;
import com.sudoku.view.fonts.CreateFont;

/// THIS IS PURELY FOR THE DEVELOPERS TO BE ABLE TO MAKE A WINDOW
public class CreateMenuWindow extends Window implements WindowInterface {
    
    private WindowManager wm;
    private CreateFont font;
    private Shader fontShader;
    private CreateString text;
    private CreateString textInfo;
    private TextFieldButton textField;
    private NumPadButton numPad;

    private String output = "Size: ";
    //x, y, scale, width, hight  
    private float[] textFieldPrime = new float[]{-0.6f, 0.7f,0.3f, 0.0f, 0.1f};

    public CreateMenuWindow(WindowManager wm) {
        super(wm);
        this.wm = wm;
        wm.setActiveWindow(this);
    }

    public void create() {
        // This code runs once
        font = wm.getFont();
		//creates a shader and a class that can display strings
		fontShader = wm.getFontShader();
		text = new CreateString(fontShader, font);

        //creates a String and a box, with xyPos, color, width, hight
        textField = new TextFieldButton(text, fontShader, output, textFieldPrime[0], textFieldPrime[1], 
            textFieldPrime[2], new float[]{1.0f, 0.0f, 0.0f},textFieldPrime[3],textFieldPrime[4]);
        textInfo = new CreateString(fontShader, font);

        
        float aspect = 1280f/720f;
        numPad = new NumPadButton(0.65f,0.6f,0.1f,0.1f*aspect,text,fontShader);
    }

    public void step() {
        // This code runs every frame
        double mouseXt = mouseX/(1280/2)-1;
        double mouseYt = -mouseY/(720/2)+1;
        
        textFieldHover(mouseXt, mouseYt);
        numPadHover(mouseXt, mouseYt);
       
        textInfo.makeText("You Can Costemize A 4x4, 9x9, 16x16, 25x25",(textFieldPrime[0]-0.005f), (textFieldPrime[1]-0.06f), 0.2f, new float[]{1.0f, 0.0f, 0.0f});
        textInfo.flush();
        textField.draw();
        numPad.draw();
    
      
    }

    private void textFieldHover(double mouseXt, double mouseYt){
        if(mouseXt < textField.quadPos[0] && mouseXt > textField.quadPos[4]
            && mouseYt > textField.quadPos[1] && mouseYt < textField.quadPos[3]
        ){
            textField.heldOver(true);
        }else{
            textField.heldOver(false);
        }
    }

    private void numPadHover(double mouseXt, double mouseYt){
        float xNP = numPad.getX();
        float yNP = numPad.getY();
        float widthNP = numPad.getWidth();
        float heightNP = numPad.getHeight();

        //first layer
        numPad.setSelected(0,mouseXt > xNP && mouseXt < xNP+widthNP && mouseYt > yNP-heightNP && mouseYt < yNP);
        numPad.setSelected(1,mouseXt > xNP+widthNP && mouseXt < xNP+(widthNP*2) && mouseYt > yNP-heightNP && mouseYt < yNP);
        numPad.setSelected(2,mouseXt > xNP+(widthNP*2) && mouseXt < xNP+(widthNP*3) && mouseYt > yNP-heightNP && mouseYt < yNP);
        //second layer
        numPad.setSelected(3,mouseXt > xNP && mouseXt < xNP+widthNP && mouseYt > yNP-(heightNP*2) && mouseYt < yNP-heightNP);
        numPad.setSelected(4,mouseXt > xNP+widthNP && mouseXt < xNP+(widthNP*2) && mouseYt > yNP-(heightNP*2) && mouseYt < yNP-heightNP);
        numPad.setSelected(5,mouseXt > xNP+(widthNP*2) && mouseXt < xNP+(widthNP*3) && mouseYt > yNP-(heightNP*2) && mouseYt < yNP-heightNP);
        //third layer
        numPad.setSelected(6,mouseXt > xNP && mouseXt < xNP+widthNP && mouseYt > yNP-(heightNP*3) && mouseYt < yNP-(heightNP*2));
        numPad.setSelected(7,mouseXt > xNP+widthNP && mouseXt < xNP+(widthNP*2) && mouseYt > yNP-(heightNP*3) && mouseYt < yNP-(heightNP*2));
        numPad.setSelected(8,mouseXt > xNP+(widthNP*2) && mouseXt < xNP+(widthNP*3) && mouseYt > yNP-(heightNP*3) && mouseYt < yNP-(heightNP*2));





    }

        @Override // If you don't need a key callback, just delete this
        public void keyCallback(int key, int scancode, int action, int mods) {
            if (action == GLFW_PRESS) {
                if (textField.isSelected()) {
                    if (key >= GLFW_KEY_0 && key <= GLFW_KEY_9) {
                    char c = (char) ('0' + (key - GLFW_KEY_0));
                    textField.updateInput(c);
                    }
                    else if(key == GLFW_KEY_BACKSPACE){
                        textField.updateInput();
                    }
                }
            }
        }
    
    @Override // If you don't need a resize callback, just delete this
    public void resizeCallback(int width, int height) {
        System.out.println("New size: " + width + "x" + height);
    }

    @Override // If you don't need a mouse button callback, just delete this
    public void mouseButtonCallback(int button, int action, int mods) {

        if (button == GLFW_MOUSE_BUTTON_LEFT &&
            action == GLFW_PRESS) {
            if(textField.isHeldOver()){
                textField.setSelected(true);
            }else{
                textField.setSelected(false);
            }

            if(numPad.isSelected()[numPad.getIndexSelec()]){
                System.out.println(numPad.getIndexSelec()+1+ " is pressed");
            }
            
        }

     

        // if (button == GLFW_MOUSE_BUTTON_RIGHT &&
        //     action == GLFW_PRESS) {

        //     System.out.println("Right click!");
        // }
    }

    private double mouseX;
    private double mouseY;

    @Override
    public void cursorPosCallback(double x, double y) {
        this.mouseX = x;
        this.mouseY = y;
    }

}
