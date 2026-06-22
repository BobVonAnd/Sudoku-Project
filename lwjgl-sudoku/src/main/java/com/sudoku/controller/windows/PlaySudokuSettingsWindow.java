package com.sudoku.controller.windows;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_0;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_9;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import com.sudoku.controller.Window;
import com.sudoku.controller.WindowInterface;
import com.sudoku.controller.WindowManager;
import com.sudoku.model.Gamepad;
import com.sudoku.model.SudokuBoard;
import com.sudoku.view.CreateString;
import com.sudoku.view.Shader;
import com.sudoku.view.elements.MenuButton;
import com.sudoku.view.elements.NumPadButton;
import com.sudoku.view.elements.Slider;
import com.sudoku.view.elements.TextFieldButton;
import com.sudoku.view.font.CreateFont;

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
    private Gamepad gpad;
    private NumPadButton numPad;
    private int gpadNumpadIndex = 0;
    private long numpad_buffer = 167;
    private long numpad_buffer_timestamp = System.currentTimeMillis();
    private long numpad_type_buffer = 167;
    private long numpad_type_buffer_timestamp = System.currentTimeMillis();
    private boolean numpad_type_selected_before = false;
    private boolean gpad_selected_before = true;

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
        gpad = new Gamepad();
        font = wm.getFont();
		//creates a shader and a class that can display strings
		Shader fontShader = wm.getFontShader();
		text = new CreateString(fontShader, font, width, height);

        startButton = new MenuButton(.5,-.5,0.4,text,fontShader,"Start");
        Buttons[0] = startButton;

        backButton = new MenuButton(-.5,-.5,0.4,text,fontShader,"Back");
        addElement(backButton,0);
        Buttons[1] = backButton;

        difficultySlider = new Slider(0, 0.3, this.mouseX, this.mouseY, this.width, this.height, 1, 1, text, fontShader, gpad, "Difficulty: ", " (easy)");

        textField = new TextFieldButton(text, fontShader, output, textFieldPrime[0], textFieldPrime[1], 
            textFieldPrime[2], new float[]{1.0f, 0.0f, 0.0f},textFieldPrime[3],textFieldPrime[4]);
        addElement(textField, 0);
        textInfo = new CreateString(fontShader, font, width, height);

        float aspect = 1280f / 720f;
        numPad = new NumPadButton(-.6f, .62f, 0.1f, 0.1f * aspect, text, fontShader);
        

        gpad.addElement(textField, 0, 0);
        gpad.addElement(backButton, -1, 2);
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

    public void typeSize(int idx) {
        if (numpad_type_selected_before && gpad.isSelected(numPad)) {
            numpad_type_buffer_timestamp = System.currentTimeMillis();
            numpad_type_selected_before = false;
        }
    
        long now = System.currentTimeMillis();
        boolean canMove = (numpad_type_buffer_timestamp == -1 ||
                now - numpad_type_buffer_timestamp >= numpad_type_buffer);
    
        if (canMove) {
            if (idx == 11) { // enter — close numpad, return to textField
                removeElement(numPad);
                gpad.removeElement(numPad);
                gpad.setPosition(0, 0);
                gpad.setMoveLocked(false);
                numpad_type_selected_before = true;
            } else if (idx == 10) { // backspace
                textField.updateInput();
            } else if (idx == 9) { // 0
                textField.updateInput('0');
            } else { // 1-9
                textField.updateInput((char) ('1' + idx));
            }
            numpad_type_buffer_timestamp = now;
        }
    }

    private void numPadHover(double mouseXt, double mouseYt) {
        float xNP = numPad.getX();
        float yNP = numPad.getY();
        float widthNP = numPad.getWidth();
        float heightNP = numPad.getHeight();

        // Mouse hover selections
        numPad.setSelected(0,  mouseXt > xNP && mouseXt < xNP + widthNP              && mouseYt > yNP - heightNP       && mouseYt < yNP               && !gpad.isConnected());
        numPad.setSelected(1,  mouseXt > xNP + widthNP && mouseXt < xNP + widthNP*2  && mouseYt > yNP - heightNP       && mouseYt < yNP               && !gpad.isConnected());
        numPad.setSelected(2,  mouseXt > xNP + widthNP*2 && mouseXt < xNP + widthNP*3 && mouseYt > yNP - heightNP      && mouseYt < yNP               && !gpad.isConnected());
        numPad.setSelected(3,  mouseXt > xNP && mouseXt < xNP + widthNP              && mouseYt > yNP - heightNP*2     && mouseYt < yNP - heightNP    && !gpad.isConnected());
        numPad.setSelected(4,  mouseXt > xNP + widthNP && mouseXt < xNP + widthNP*2  && mouseYt > yNP - heightNP*2     && mouseYt < yNP - heightNP    && !gpad.isConnected());
        numPad.setSelected(5,  mouseXt > xNP + widthNP*2 && mouseXt < xNP + widthNP*3 && mouseYt > yNP - heightNP*2   && mouseYt < yNP - heightNP    && !gpad.isConnected());
        numPad.setSelected(6,  mouseXt > xNP && mouseXt < xNP + widthNP              && mouseYt > yNP - heightNP*3     && mouseYt < yNP - heightNP*2  && !gpad.isConnected());
        numPad.setSelected(7,  mouseXt > xNP + widthNP && mouseXt < xNP + widthNP*2  && mouseYt > yNP - heightNP*3     && mouseYt < yNP - heightNP*2  && !gpad.isConnected());
        numPad.setSelected(8,  mouseXt > xNP + widthNP*2 && mouseXt < xNP + widthNP*3 && mouseYt > yNP - heightNP*3   && mouseYt < yNP - heightNP*2  && !gpad.isConnected());
        numPad.setSelected(9,  mouseXt > xNP && mouseXt < xNP + widthNP              && mouseYt > yNP - heightNP*4     && mouseYt < yNP - heightNP*3  && !gpad.isConnected());
        numPad.setSelected(10, mouseXt > xNP + widthNP && mouseXt < xNP + widthNP*2  && mouseYt > yNP - heightNP*4     && mouseYt < yNP - heightNP*3  && !gpad.isConnected());
        numPad.setSelected(11, mouseXt > xNP + widthNP*2 && mouseXt < xNP + widthNP*3 && mouseYt > yNP - heightNP*4   && mouseYt < yNP - heightNP*3  && !gpad.isConnected());

        // Gamepad navigation of numpad
        if (gpad_selected_before && gpad.isSelected(numPad)) {
            numpad_buffer_timestamp = System.currentTimeMillis();
            gpad_selected_before = false;
        }
        long now = System.currentTimeMillis();
        boolean canMove = (numpad_buffer_timestamp == -1 ||
                now - numpad_buffer_timestamp >= numpad_buffer);

        if (gpad.isSelected(numPad)) {
            gpad.setMoveLocked(true);
            numPad.setSelected(gpadNumpadIndex, true);
            if (canMove) {
                int x_move = gpad.getXDir();
                int y_move = gpad.getYDir();
                gpadNumpadIndex += 3 * y_move + x_move;
                gpadNumpadIndex = gpadNumpadIndex > 11 ? 11 : gpadNumpadIndex < 0 ? 0 : gpadNumpadIndex;
                numpad_buffer_timestamp = now;
            }
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
        gpad.step();
        difficultySlider.update(mouseX, mouseY, width, height, mbLeftHeld);
    
        double mouseXt = mouseX / (width / 2) - 1;
        double mouseYt = -mouseY / (height / 2) + 1;
    
        for (int i = 0; i < Buttons.length; i++) {
            if ((Buttons[i].getPos()[0] - Buttons[i].getSize() / 2 < mouseXt &&
                    Buttons[i].getPos()[0] + Buttons[i].getSize() / 2 > mouseXt &&
                    !gpad.isConnected() &&
                    Buttons[i].getPos()[1] - Buttons[i].getSize() / 2 < mouseYt &&
                    Buttons[i].getPos()[1] + Buttons[i].getSize() / 2 > mouseYt) || gpad.isSelected(Buttons[i])) {
                Buttons[i].heldOver(true);
                windowTransition(Buttons[i], false);
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
        } else if (textField.getInput() == "Input a valid size here...") {
            textField.setInput("");
        }

        if (gpad.isConnected()) {
            if (gpad.isSelected(textField)) {
                textField.setSelected(true);
                // a
                if (gpad.isEntered()) {
                    addElement(numPad, 1);
                    gpad.addElement(numPad, 37, 0);
                    gpad.setPosition(37, 0);
                }
            } else if (gpad.isSelected(numPad)) {
                textField.setSelected(true); // keep textField visually selected while typing
                // a
                if (gpad.isEntered()) {
                    typeSize(gpadNumpadIndex);
                }
            } else {
                textField.setSelected(false);
            }
        }
        
        numPadHover(mouseXt, mouseYt);
    
        if (!elementExists(numPad)) {
            if (textField.getValidity() && !startButtonShowing) {
                addElement(startButton, 0);
                gpad.addElement(startButton, 0, 2);
                addElement(difficultySlider, 0);
                gpad.addElement(difficultySlider, 0, 1);
                startButtonShowing = true;
            } else if (startButtonShowing && !textField.getValidity()) {
                removeElement(startButton);
                gpad.removeElement(startButton);
                removeElement(difficultySlider);
                gpad.removeElement(difficultySlider);
                startButtonShowing = false;
            }
        } else {
            removeElement(startButton);
            gpad.removeElement(startButton);
            removeElement(difficultySlider);
            gpad.removeElement(difficultySlider);
            startButtonShowing = false;
        }
    
        sb.setDifficultyScale(1 - difficultySlider.getValue());
        difficultySlider.setOverrideValueString(sb.getDifficultyString());
        difficultySlider.updateSuffix(" (Removes " + sb.getFieldsToRemove(1 - difficultySlider.getValue()) + " Fields)");
        textFieldHover(mouseXt, mouseYt);
    
        textInfo.makeText("You Can Customize a 4x4, 9x9, 16x16, 25x25, 36x36", (textFieldPrime[0] - 0.005f), (textFieldPrime[1] - 0.06f), 0.2f, new float[]{1.0f, 0.0f, 0.0f});
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
        if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
            mbLeftHeld = true;
            // Button Action
            for (int i = 0 ; i < Buttons.length ; i++) {
                if (Buttons[i].isHeldOver() && elementExists(Buttons[i])) {
                    windowTransition(Buttons[i], true);
                }
            }

            if(textField.isHeldOver()){
                textField.setSelected(true);
            }else{
                textField.setSelected(false);
            }

        } else if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_RELEASE){
                        mbLeftHeld = false;
        }
    }

    public void windowTransition(MenuButton b, boolean mouseClick) {
        if (mouseClick || gpad.isEntered()) {
            if (b.isHeldOver() && elementExists(b)) {
                if (b == startButton) {
                    new playSudokuWindow(wm, width, height, sb, false);
                } else if (b == backButton) {
                    new mainMenuWindow(wm, width, height);
                }
            }
        }
    }
}
