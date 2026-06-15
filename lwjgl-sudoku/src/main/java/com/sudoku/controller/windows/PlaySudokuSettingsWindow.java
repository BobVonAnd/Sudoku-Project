package com.sudoku.controller.windows;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_0;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_9;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

import com.sudoku.controller.Window;
import com.sudoku.controller.WindowInterface;
import com.sudoku.controller.WindowManager;
import com.sudoku.model.SudokuBoard;
import com.sudoku.view.CreateString;
import com.sudoku.view.Shader;
import com.sudoku.view.elements.MenuButton;
import com.sudoku.view.elements.Slider;
import com.sudoku.view.elements.TextFieldButton;
import com.sudoku.view.fonts.CreateFont;

public class PlaySudokuSettingsWindow extends Window implements WindowInterface {
    
    private WindowManager wm;
    private MenuButton startButton, backButton;
    private Slider difficultySlider;
    private MenuButton[] Buttons = new MenuButton[2];
    private double mouseX;
    private double mouseY;
    private CreateString textInfo;
    private int width, height;
    private CreateFont font;
	private CreateString text;
    private boolean mbLeftHeld;
    private SudokuBoard sb;
    private TextFieldButton textField;
    private float[] textFieldPrime = new float[]{-0.6f, 0.7f,0.3f, 0.0f, 0.1f};
    private String output = "Size: ";
    private boolean startButtonShowing = false;

    public PlaySudokuSettingsWindow(WindowManager wm, int width, int height) {
        super(wm);
        this.wm = wm;
        this.width = width;
        this.height = height;
        this.sb = new SudokuBoard(9);
        wm.setActiveWindow(this);
    }

    public void create() {
        // This code runs once
        font = wm.getFont();
		//creates a shader and a class that can display strings
		Shader fontShader = wm.getFontShader();
		text = new CreateString(fontShader, font, width, height);

        startButton = new MenuButton(.5,-.5,0.4,text,fontShader,"Start");
        Buttons[0] = startButton;

        backButton = new MenuButton(-.5,-.5,0.4,text,fontShader,"Back");
        addElement(backButton,0);
        Buttons[1] = backButton;

        difficultySlider = new Slider(0, 0.3, this.mouseX, this.mouseY, this.width, this.height, 1, 1, text, fontShader, "Difficulty: ", " (easy)");
        addElement(difficultySlider, 0);

        textField = new TextFieldButton(text, fontShader, output, textFieldPrime[0], textFieldPrime[1], 
            textFieldPrime[2], new float[]{1.0f, 0.0f, 0.0f},textFieldPrime[3],textFieldPrime[4]);
        addElement(textField, 0);
        textInfo = new CreateString(fontShader, font, width, height);
        
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

    @Override
    public void cursorPosCallback(double x, double y) {
        this.mouseX = x;
        this.mouseY = y;
    }

    @Override
    public void resizeCallback(int width, int height) {
        this.width = width;
        this.height = height;
        text.setXY(width, height);
    }

    public void step() {
        // This code runs every frame
        difficultySlider.update(mouseX, mouseY, width, height, mbLeftHeld);
        // Check every menu button if we're holding over
        double mouseXt = mouseX/(width/2)-1;
        double mouseYt = -mouseY/(height/2)+1;
        for (int i = 0 ; i < Buttons.length ; i++) {
            if (Buttons[i].getPos()[0] - Buttons[i].getSize()/2 < mouseXt & 
                Buttons[i].getPos()[0] + Buttons[i].getSize()/2 > mouseXt &

                Buttons[i].getPos()[1] - Buttons[i].getSize()/2 < mouseYt & 
                Buttons[i].getPos()[1] + Buttons[i].getSize()/2 > mouseYt) {
                Buttons[i].heldOver(true);
            } else {
                Buttons[i].heldOver(false);
            }
        }
        if (textField.getValidity() && sb.getSize() != Integer.valueOf(textField.getInput())) {
            sb = null;
            sb = new SudokuBoard(Integer.valueOf(textField.getInput()));
        }

        if (!textField.getValidity() && !textField.isSelected()) {
            textField.setInput("Input a valid size here...");
        }  else if (textField.getInput() == "Input a valid size here...") {
            textField.setInput("");
        }

        if (textField.getValidity() && !startButtonShowing) {
            addElement(startButton,0);
            startButtonShowing = true;
        } else if (startButtonShowing && !textField.getValidity()){
            removeElement(startButton);
            startButtonShowing = false;
        }

        sb.setDifficultyScale(1-difficultySlider.getValue());
        difficultySlider.setOverrideValueString(sb.getDifficultyString());
        difficultySlider.updateSuffix(" (Removes "+sb.getFieldsToRemove(1-difficultySlider.getValue())+" Fields)");
        textFieldHover(mouseXt, mouseYt);

        textInfo.makeText("You Can Customize a 4x4, 9x9, 16x16, 25x25",(textFieldPrime[0]-0.005f), (textFieldPrime[1]-0.06f), 0.2f, new float[]{1.0f, 0.0f, 0.0f});
        textInfo.flush();
    }

    @Override // If you don't need a key callback, just delete this
    public void keyCallback(int key, int scancode, int action, int mods) {
        if (key == GLFW_KEY_SPACE && action == GLFW_PRESS) {
            System.out.println("Space pressed!");
        }
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

    @Override // If you don't need a mouse button callback, just delete this
    public void mouseButtonCallback(int button, int action, int mods) {

        if (button == GLFW_MOUSE_BUTTON_LEFT &&
            action == GLFW_PRESS) {
            // Button Action
            for (int i = 0 ; i < Buttons.length ; i++) {
                if (Buttons[i].isHeldOver() && elementExists(Buttons[i])) {
                    if (Buttons[i] == startButton) {
                        sb.populate();
                        new playSudokuWindow(wm, width, height, sb);
                    } else if (Buttons[i] == backButton) {
                        new mainMenuWindow(wm, width, height);
                    }
                }
            }
        }
        if (button == GLFW_MOUSE_BUTTON_LEFT) {
            if (action == GLFW_PRESS) {
                mbLeftHeld = true;
            } else if (action == GLFW_RELEASE) {
                mbLeftHeld = false;
            }
            
        }

        if (button == GLFW_MOUSE_BUTTON_LEFT &&
            action == GLFW_PRESS) {
            if(textField.isHeldOver()){
                textField.setSelected(true);
            }else{
                textField.setSelected(false);
            }

        }
        
    }

}
