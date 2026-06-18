package com.sudoku.controller.windows;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_0;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_1;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_2;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_3;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_4;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_5;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_6;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_7;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_8;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_9;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glVertex2f;

import com.sudoku.controller.Window;
import com.sudoku.controller.WindowInterface;
import com.sudoku.controller.WindowManager;
import com.sudoku.model.Gamepad;
import com.sudoku.model.SudokuBoard;
import com.sudoku.view.CreateString;
import com.sudoku.view.Shader;
import com.sudoku.view.elements.FieldButton;
import com.sudoku.view.elements.MenuButton;
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
    private boolean unique, solveable;
    
    private Gamepad gpad;
    private int gpadNumpadIndex = 0;
    private long numpad_buffer = 167; // in ms
    private long numpad_buffer_timestamp = System.currentTimeMillis();
    private long numpad_type_buffer = 167; // in ms
    private long numpad_type_buffer_timestamp = System.currentTimeMillis();
    private boolean numpad_type_selected_before = false;
    private boolean gpad_selected_before = true;
    private boolean numpad_for_board = false;

    private String output = "Size: ";
    // x, y, scale, width, hight
    private float[] textFieldPrime = new float[] { -0.6f, 0.7f, 0.3f, 0.0f, 0.1f };

    private SudokuBoard sudokuBoard = new SudokuBoard(0);
    private int standardSize = 9;
    private int size = standardSize;
    private int width;
    private int height;
    private FieldButton[][] buttonArray;
    private double fieldsize;
    private double yStart = 0.6;
    private double xStart = -0.8;
    private int[] selectedField = new int[2];

    private boolean sudokuCreated = false;

    private MenuButton returnButton;

    private boolean errorDetected = false;

    public CreateMenuWindow(WindowManager wm, int width, int height) {
        super(wm);
        this.wm = wm;
        this.width = width;
        this.height = height;
        wm.setActiveWindow(this);
    }

    public void create() {
        // This code runs once
        gpad = new Gamepad();
        font = wm.getFont();
        // creates a shader and a class that can display strings
        fontShader = wm.getFontShader();
        text = new CreateString(fontShader, font, width, height);

        // creates a String and a box, with xyPos, color, width, hight
        textField = new TextFieldButton(text, fontShader, output, textFieldPrime[0], textFieldPrime[1],
                textFieldPrime[2], new float[] { 1.0f, 0.0f, 0.0f }, textFieldPrime[3], textFieldPrime[4]);
        textInfo = new CreateString(fontShader, font, width, height);

        float aspect = 1280f / 720f;
        numPad = new NumPadButton(0.65f, 0.6f, 0.1f, 0.1f * aspect, text, fontShader);
        addElement(numPad, 0);
        addElement(textField, 0);

        // return to menu button
        returnButton = new MenuButton(-0.8, 0.8, 0.2, text, fontShader, "Menu");
        addElement(returnButton, 0);

        // Add base elements to gamepad
        gpad.addElement(returnButton,1,0);
        gpad.addElement(textField, 2, 0);
    }

    public void step() {
        // This code runs every frame
        gpad.step();
        double mouseXt = mouseX / (1280 / 2) - 1;
        double mouseYt = -mouseY / (720 / 2) + 1;

        // button juggle
        if ((returnButton.getPos()[0] - returnButton.getSize() / 2 < mouseXt &
                returnButton.getPos()[0] + returnButton.getSize() / 2 > mouseXt &
                !gpad.isConnected()&

                returnButton.getPos()[1] - returnButton.getSize() / 2 < mouseYt &
                returnButton.getPos()[1] + returnButton.getSize() / 2 > mouseYt) || gpad.isSelected(returnButton)) {
            returnButton.heldOver(true);
            windowTransition(returnButton,false);
        } else {
            returnButton.heldOver(false);
        }

        textFieldHover(mouseXt, mouseYt);
        numPadHover(mouseXt, mouseYt);

        textInfo.makeText("You Can Customize A 4x4, 9x9, 16x16, 25x25, 36x36", (textFieldPrime[0] - 0.005f),
                (textFieldPrime[1] - 0.06f), 0.2f, new float[] { 1.0f, 0.0f, 0.0f });
        textInfo.makeText("Solveable: " + String.valueOf(solveable) + "   Unique: " + String.valueOf(unique), -.9f, -.9f, .3f, new float[] { 1.0f, 0.0f, 0.0f });
        textInfo.flush();

        // Textfield Controller Support
        if (gpad.isConnected() && (gpad.isSelected(textField) || gpad.isSelected(numPad))) {
            boolean entered = gpad.isEntered();
            // select when the gpad is over it

            if (gpad.isSelected(textField)) {
                textField.setSelected(true);
            // press a to enter
                if (entered) {
                    gpad.addElement(numPad, 37, 0);
                    gpad.setPosition(37, 0);
                    numpad_for_board = false;
                }
            } else {
                textField.setSelected(false);
                // reset the numpad if enter
                if (entered && gpad.isSelected(numPad)) {
                    if (numpad_for_board) {
                        typeBoard(gpadNumpadIndex);
                    } else {
                        typeSize(gpadNumpadIndex);
                    }
                }
            }

            
        }

        // Sudoku board gamepad
        if (gpad.isConnected()) {
            if (textField.getValidity() && sudokuCreated) {
                boolean entered = gpad.isEntered();
                FieldButton fieldButton;
                if (!gpad.isSelected(returnButton) && !gpad.isSelected(textField) && !gpad.isSelected(numPad)) {
                    for (int i = 0; i < size; i++) {
                        for (int j = 0; j < size; j++) {
                            fieldButton = buttonArray[i][j];
                            if (gpad.isSelected(gpad.getElementAt(2+i,2+j))) {
                                fieldButton.selected(true);
                                selectedField[0] = i;
                                selectedField[1] = j;
                            } else {
                                fieldButton.selected(false);
                            }
                        }
                    }

                    if (entered) {
                        numpad_for_board = true;
                        gpad.addElement(numPad, 37, 0);
                        gpad.setPosition(37, 0);
                    }
                }
                
                
            }
        } 
        

        // sudokuBoard
        if (textField.getValidity()) {
            fontShader.detach();
            glBegin(GL_LINES);

            float x = (float) xStart;
            float boardlenth = (float) (size * fieldsize);
            int bigfield = (int) Math.sqrt(size);
            for (int i = 0; i <= size; i++) {
                if (i % bigfield == 0) {
                    bigfieldline();
                } else {
                    regularline();
                }
                glVertex2f(x, (float) yStart);
                glVertex2f(x, (float) yStart - boardlenth);

                x += (float) fieldsize;
            }

            float y = (float) yStart;
            for (int i = 0; i <= size; i++) {
                if (i % bigfield == 0) {
                    bigfieldline();
                } else {
                    regularline();
                }
                glVertex2f((float) xStart, y);
                glVertex2f((float) xStart + boardlenth, y);
                y -= (float) fieldsize;
            }

            glEnd();


            if(errorDetected){
                text.makeText("Invalid input", -0.1f, 0.85f, 0.45f, new float[] {1f,0f,0f});
                text.flush();
            }
        }



    }

    private void bigfieldline() {
        glLineWidth(2.5f);
        glColor3d(0.20392157f, 0.27842137f, 0.38039216f);
    }

    private void regularline() {
        glLineWidth(1.5f);
        glColor3d(0.84705882f, 0.88235294f, 0.91764706f);
    }

    private void textFieldHover(double mouseXt, double mouseYt) {
        if (mouseXt < textField.quadPos[0] && mouseXt > textField.quadPos[4]
                && mouseYt > textField.quadPos[1] && mouseYt < textField.quadPos[3]) {
            textField.heldOver(true);
        } else {
            textField.heldOver(false);
        }
    }

    private void updateBoardState(int value) {

        sudokuBoard.changeField(
            selectedField[0],
            selectedField[1],
            0
        );
    
        if (sudokuBoard.isValid(
                selectedField[0],
                selectedField[1],
                value)) {
    
            boolean algoUnique =
                sudokuBoard.getAlgoX().algoXIsUnique(sudokuBoard);
    
            unique = algoUnique;
            solveable = !algoUnique || algoUnique;
    
        } else {
            unique = false;
            solveable = false;
        }
    
        sudokuBoard.changeField(
            selectedField[0],
            selectedField[1],
            value
        );

        buttonArray[selectedField[0]][selectedField[1]].setNotValid(!solveable);
    }

    private void numPadHover(double mouseXt, double mouseYt) {
        float xNP = numPad.getX();
        float yNP = numPad.getY();
        float widthNP = numPad.getWidth();
        float heightNP = numPad.getHeight();

        // first layer
        numPad.setSelected(0, mouseXt > xNP && mouseXt < xNP + widthNP && mouseYt > yNP - heightNP && mouseYt < yNP && !gpad.isConnected());
        numPad.setSelected(1,mouseXt > xNP + widthNP && mouseXt < xNP + (widthNP * 2) && mouseYt > yNP - heightNP && mouseYt < yNP && !gpad.isConnected());
        numPad.setSelected(2, mouseXt > xNP + (widthNP * 2) && mouseXt < xNP + (widthNP * 3) && mouseYt > yNP - heightNP && mouseYt < yNP && !gpad.isConnected());
        // second layer
        numPad.setSelected(3, mouseXt > xNP && mouseXt < xNP + widthNP && mouseYt > yNP - (heightNP * 2) && mouseYt < yNP - heightNP && !gpad.isConnected());
        numPad.setSelected(4, mouseXt > xNP + widthNP && mouseXt < xNP + (widthNP * 2) && mouseYt > yNP - (heightNP * 2) && mouseYt < yNP - heightNP && !gpad.isConnected());
        numPad.setSelected(5, mouseXt > xNP + (widthNP * 2) && mouseXt < xNP + (widthNP * 3) && mouseYt > yNP - (heightNP * 2) && mouseYt < yNP - heightNP && !gpad.isConnected());
        // third layer
        numPad.setSelected(6, mouseXt > xNP && mouseXt < xNP + widthNP && mouseYt > yNP - (heightNP * 3) && mouseYt < yNP - (heightNP * 2) && !gpad.isConnected());
        numPad.setSelected(7, mouseXt > xNP + widthNP && mouseXt < xNP + (widthNP * 2) && mouseYt > yNP - (heightNP * 3) && mouseYt < yNP - (heightNP * 2) && !gpad.isConnected());
        numPad.setSelected(8, mouseXt > xNP + (widthNP * 2) && mouseXt < xNP + (widthNP * 3) && mouseYt > yNP - (heightNP * 3) && mouseYt < yNP - (heightNP * 2) && !gpad.isConnected());
        // fourth layer
        numPad.setSelected(9,mouseXt > xNP &&mouseXt < xNP + widthNP &&mouseYt > yNP - (heightNP * 4) &&mouseYt < yNP - (heightNP * 3) &&!gpad.isConnected());
        numPad.setSelected(10,mouseXt > xNP + widthNP &&mouseXt < xNP + (widthNP * 2) &&mouseYt > yNP - (heightNP * 4) &&mouseYt < yNP - (heightNP * 3) &&!gpad.isConnected());
        numPad.setSelected(11,mouseXt > xNP + (widthNP * 2) &&mouseXt < xNP + (widthNP * 3) &&mouseYt > yNP - (heightNP * 4) &&mouseYt < yNP - (heightNP * 3) &&!gpad.isConnected());
        
        // Gamepad stuff
        if (gpad_selected_before && gpad.isSelected(numPad)) {
            numpad_buffer_timestamp = System.currentTimeMillis();
            gpad_selected_before = false;
        }
        long now = System.currentTimeMillis();
        boolean canMove =
                (numpad_buffer_timestamp == -1 ||
                now - numpad_buffer_timestamp >= numpad_buffer);

        if (gpad.isSelected(numPad)) {
            gpad.setMoveLocked(true);
            numPad.setSelected(gpadNumpadIndex, true);
            if (canMove) {
                int x_move = gpad.getXDir();
                int y_move = gpad.getYDir();
                gpadNumpadIndex += 3 * y_move + x_move;
                gpadNumpadIndex = gpadNumpadIndex > 11 ? 8 : gpadNumpadIndex < 0 ? 0 : gpadNumpadIndex;
                numpad_buffer_timestamp = now;
            }
        }
    }

    public void typeSize(int idx) {

        if (numpad_type_selected_before && gpad.isSelected(numPad)) {
            numpad_type_buffer_timestamp = System.currentTimeMillis();
            numpad_type_selected_before = false;
        }

        long now = System.currentTimeMillis();
        boolean canMove =
                (numpad_type_buffer_timestamp == -1 ||
                now - numpad_type_buffer_timestamp >= numpad_type_buffer);
        if (canMove) {
            if (idx == 11) { // enter
                gpad.removeElement(numPad);
                gpad.setPosition(2, 0);
                numpad_for_board = true;
                gpad.setMoveLocked(false);
                numpad_type_selected_before = true;
            } else if (idx == 10) { // backspace
                textField.updateInput();
            } else if (idx == 9) { // 0
                textField.updateInput('0');
            } else { // every other key
                textField.updateInput((char) ('1' + idx));
            }
            createSudoku();
        }
    }

    private void typeBoard(int idx) {
        int value = sudokuBoard.getSingleField(
            selectedField[0],
            selectedField[1]
        ).getValue();
    
        if (idx == 11) { // enter
            gpad.removeElement(numPad);
            gpad.setMoveLocked(false);
            numpad_for_board = false;
            gpad.setPosition(selectedField[0]+2, selectedField[1]+2);
            return;
        }
    
        if (idx == 10) { // backspace
            value /= 10;
        } else if (idx == 9) { // 0
            value = value * 10;
        } else { // 1-9
            value = value * 10 + (idx + 1);
        }
    
        buttonArray[selectedField[0]][selectedField[1]]
            .setError(value > size);

        if (value <= size) {
            updateBoardState(value);
        } else {
            errorDetected = true;
        }
    }

    public void createSudoku() {
        if (textField.getValidity()) {
            size = Integer.parseInt(textField.getInput());
            System.out.println(size);
            sudokuBoard = new SudokuBoard(size);
            buttonArray = new FieldButton[size][size];
            double y;
            double x;

            fieldsize = 1.3 / size;
            x = xStart;
            for (int i = 0; i < size; i++) {
                y = yStart;
                for (int j = 0; j < size; j++) {
                    buttonArray[i][j] = new FieldButton(sudokuBoard.getSingleField(i, j), x, y, fieldsize, fieldsize,
                            sudokuBoard, text, fontShader);
                    addElement(buttonArray[i][j], 0);
                    gpad.addElement(buttonArray[i][j], 2+j, 2+i);
                    y -= fieldsize;
                }
                x += fieldsize;
            }
            sudokuCreated = true;
        } else if (!textField.getValidity() && sudokuCreated) {
            size = standardSize;
            sudokuBoard = new SudokuBoard(size);
            buttonArray = new FieldButton[size][size];
            double y;
            double x;

            fieldsize = 1.3 / size;
            x = xStart;
            for (int i = 0; i < size; i++) {
                y = yStart;
                for (int j = 0; j < size; j++) {
                    buttonArray[i][j] = new FieldButton(sudokuBoard.getSingleField(i, j), x, y, fieldsize, fieldsize,
                            sudokuBoard, text, fontShader);
                    addElement(buttonArray[i][j], 0);
                    gpad.addElement(buttonArray[i][j], 2+j, 2+i);
                    y -= fieldsize;
                }
                x += fieldsize;
            }
            sudokuCreated = false;
        }
    }

    @Override // If you don't need a key callback, just delete this
    public void keyCallback(int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {

            if (textField.getValidity() && sudokuCreated) {
                // sudokuBoard
                errorDetected = false;
                int value = sudokuBoard.getSingleField(selectedField[0], selectedField[1]).getValue();
                if (key >= GLFW_KEY_0 && key <= GLFW_KEY_9 && action == GLFW_PRESS) {
                    value = value * 10 + (key-48);
                    sudokuBoard.changeField(selectedField[0], selectedField[1], value);
                    System.out.println(String.valueOf(value) + " pressed!");
                } 
                if (key == GLFW_KEY_BACKSPACE && action == GLFW_PRESS) {
                    System.out.println();
                    value = value / 10;
                    sudokuBoard.changeField(selectedField[0], selectedField[1], value);
                }
                if (!(value <= size)) {
                    value = value / 10;
                    sudokuBoard.changeField(selectedField[0], selectedField[1], value);
                    errorDetected = true;
                }
                buttonArray[selectedField[0]][selectedField[1]].setError(value > sudokuBoard.getSize());
                if (key >= GLFW_KEY_0 && key <= GLFW_KEY_9) {
                    updateBoardState(value);
                }
            }
            if (textField.isSelected()) {
                if (key >= GLFW_KEY_0 && key <= GLFW_KEY_9) {
                    char c = (char) ('0' + (key - GLFW_KEY_0));
                    textField.updateInput(c);
                    clearSelectField();
                } else if (key == GLFW_KEY_BACKSPACE) {
                    textField.updateInput();
                    clearSelectField();
                }
                createSudoku();

            }

        }

    }

    private void clearSelectField() {
        selectedField[0] = 0;
        selectedField[1] = 0;
    }

    @Override // If you don't need a resize callback, just delete this
    public void resizeCallback(int width, int height) {
        this.width = width;
        this.height = height;
        text.setXY(width, height);
        System.out.println("New size: " + width + "x" + height);
    }

    @Override // If you don't need a mouse button callback, just delete this
    public void mouseButtonCallback(int button, int action, int mods) {

        if (button == GLFW_MOUSE_BUTTON_LEFT &&
            action == GLFW_PRESS) {
            if (!gpad.isConnected()) {
                if (textField.isHeldOver()) {
                    textField.setSelected(true);
                } else {
                    textField.setSelected(false);
                }
            }

            int idx = numPad.getIndexSelec();

            if (numPad.isSelected()[idx]) {
    
                if (textField.isSelected()) {
    
                    // Size input field
                    if (idx == 10) { // <<
                        textField.updateInput();
                    } else if (idx == 9) { // 0
                        textField.updateInput('0');
                    } else if (idx < 9) { // 1-9
                        textField.updateInput((char) ('1' + idx));
                    }
    
                    createSudoku();
                }
    
                else if (textField.getValidity() && sudokuCreated) {
    
                    errorDetected = false;
    
                    int value = sudokuBoard.getSingleField(
                            selectedField[0],
                            selectedField[1])
                            .getValue();
    
                    if (idx == 10) { // <<
                        value /= 10;
                    } else if (idx == 9) { // 0
                        value *= 10;
                    } else if (idx < 9) { // 1-9
                        value = value * 10 + (idx + 1);
                    } else if (idx == 11) { // Enter
                        return;
                    }
    
                    if (value <= size) {
                        sudokuBoard.changeField(
                                selectedField[0],
                                selectedField[1],
                                value);
    
                        updateBoardState(value);
                    } else {
                        errorDetected = true;
                    }
    
                    buttonArray[selectedField[0]][selectedField[1]]
                            .setError(value > size);
                }
            }
            

            // sudokuBoard
            if (textField.getValidity() && sudokuCreated) {
                double[] pos = new double[2];
                double mouseXt = mouseX / (width / 2) - 1;
                double mouseYt = -mouseY / (height / 2) + 1;
                FieldButton fieldButton;
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        fieldButton = buttonArray[i][j];
                        pos = fieldButton.getPos();
                        if ((pos[0] < mouseXt & pos[0] + fieldsize > mouseXt &
                                pos[1] > mouseYt & pos[1] - fieldsize < mouseYt) && !gpad.isConnected()) {
                            fieldButton.selected(true);
                            selectedField[0] = i;
                            selectedField[1] = j;
                        } else {
                            fieldButton.selected(false);
                        }
                    }
                }
            }

            if(returnButton.isHeldOver()){
                new mainMenuWindow(wm, width, height);
            }

        }
    }

    public void windowTransition(MenuButton b, boolean mouseClick) {
        if (mouseClick || gpad.isEntered()) {
            if (b.isHeldOver() && elementExists(b)) {
                if (b == returnButton) {
                    new mainMenuWindow(wm, width, height);
                }
            }
        }
    }


    private double mouseX;
    private double mouseY;

    @Override
    public void cursorPosCallback(double x, double y) {
        this.mouseX = x;
        this.mouseY = y;
    }

}
