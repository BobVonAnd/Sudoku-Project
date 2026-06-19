package com.sudoku.controller.windows;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_0;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_9;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

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
import com.sudoku.view.elements.Sudoku;
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

    private SudokuBoard sudokuBoard = new SudokuBoard(1);
    private SudokuBoard solvedSudokuBoard;
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
    private boolean errorDetected = false;

    private MenuButton returnButton;
    private MenuButton playButton;
    private MenuButton solveButton;
    private Sudoku sudokuFront;

    private FieldButton[][] boardButtons;

    private boolean classCreated = false;

    public CreateMenuWindow(WindowManager wm, int width, int height) {
        super(wm);
        this.wm = wm;
        this.width = width;
        this.height = height;
        wm.setActiveWindow(this);
        classCreated = true;
    }

    public void create() {
        // This code runs once
        if (!classCreated) {
            gpad = new Gamepad();

            font = wm.getFont();
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
            playButton = new MenuButton(-0.8, 0.55, 0.2, text, fontShader, "Play");
            solveButton = new MenuButton(-0.8, 0.30, 0.2, text, fontShader, "Solve");
            addElement(returnButton, 0);
            addElement(playButton, 0);
            addElement(solveButton, 0);

            // Add base elements to gamepad
            gpad.addElement(returnButton, 1, 0);
            gpad.addElement(textField, 2, 0);

        }

    }

    public void step() {
        // This code runs every frame
        gpad.step();
        double mouseXt = mouseX / (1280 / 2) - 1;
        double mouseYt = -mouseY / (720 / 2) + 1;

        holdOver(returnButton);
        holdOver(playButton);
        holdOver(solveButton);

        textFieldHover(mouseXt, mouseYt);
        numPadHover(mouseXt, mouseYt);

        textInfo.makeText("You Can Customize A 4x4, 9x9, 16x16, 25x25, 36x36", (textFieldPrime[0] - 0.005f),
                (textFieldPrime[1] - 0.06f), 0.2f, new float[] { 1.0f, 0.0f, 0.0f });
        textInfo.makeText("Solveable: " + String.valueOf(solveable) + "   Unique: " + String.valueOf(unique), -.9f,
                -.9f, .3f, new float[] { 1.0f, 0.0f, 0.0f });
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
                            fieldButton = sudokuFront.getButtonArray()[i][j];
                            if (gpad.isSelected(gpad.getElementAt(2 + i, 2 + j))) {
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

    }

    // input a MenuButton and it will track if the mouse if hovering the button
    private void holdOver(MenuButton button) {
        double mouseXt = mouseX / (width / 2) - 1;
        double mouseYt = -mouseY / (height / 2) + 1;
        if ((button.getPos()[0] - button.getSize() / 2 < mouseXt &
                button.getPos()[0] + button.getSize() / 2 > mouseXt &
                !gpad.isConnected() &&
                button.getPos()[1] - button.getSize() / 2 < mouseYt &
                button.getPos()[1] + button.getSize() / 2 > mouseYt) || gpad.isSelected(button)) {
            button.heldOver(true);
        } else {
            button.heldOver(false);
        }
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
                0);

        System.out.println(sudokuBoard.isValid(
                selectedField[0],
                selectedField[1],
                value));
                
        if (sudokuBoard.isValid(
                selectedField[0],
                selectedField[1],
                value)) {
            boolean algoUnique = false;
            try {
                algoUnique = sudokuBoard.getAlgoX().algoXIsUnique(sudokuBoard);
                
            } catch (Exception e) {
                System.out.println(e);
            }

            unique = algoUnique;
            solveable = sudokuBoard.getAlgoX().getSolutionCounter() > 0;
            if(solveable){
                playButton.setValid(true);
                solveButton.setValid(true);
            }else{
                playButton.setValid(false);
                solveButton.setValid(false);
            }
    
        } else {
            unique = false;
            solveable = false;
            playButton.setValid(false);
            solveButton.setValid(false);
        }

        sudokuBoard.changeField(
                selectedField[0],
                selectedField[1],
                value);

        sudokuFront.getButtonArray()[selectedField[0]][selectedField[1]].setNotValid(!solveable);
    }

    private void numPadHover(double mouseXt, double mouseYt) {
        float xNP = numPad.getX();
        float yNP = numPad.getY();
        float widthNP = numPad.getWidth();
        float heightNP = numPad.getHeight();

        // first layer
        numPad.setSelected(0, mouseXt > xNP && mouseXt < xNP + widthNP && mouseYt > yNP - heightNP && mouseYt < yNP
                && !gpad.isConnected());
        numPad.setSelected(1, mouseXt > xNP + widthNP && mouseXt < xNP + (widthNP * 2) && mouseYt > yNP - heightNP
                && mouseYt < yNP && !gpad.isConnected());
        numPad.setSelected(2, mouseXt > xNP + (widthNP * 2) && mouseXt < xNP + (widthNP * 3) && mouseYt > yNP - heightNP
                && mouseYt < yNP && !gpad.isConnected());
        // second layer
        numPad.setSelected(3, mouseXt > xNP && mouseXt < xNP + widthNP && mouseYt > yNP - (heightNP * 2)
                && mouseYt < yNP - heightNP && !gpad.isConnected());
        numPad.setSelected(4, mouseXt > xNP + widthNP && mouseXt < xNP + (widthNP * 2) && mouseYt > yNP - (heightNP * 2)
                && mouseYt < yNP - heightNP && !gpad.isConnected());
        numPad.setSelected(5, mouseXt > xNP + (widthNP * 2) && mouseXt < xNP + (widthNP * 3)
                && mouseYt > yNP - (heightNP * 2) && mouseYt < yNP - heightNP && !gpad.isConnected());
        // third layer
        numPad.setSelected(6, mouseXt > xNP && mouseXt < xNP + widthNP && mouseYt > yNP - (heightNP * 3)
                && mouseYt < yNP - (heightNP * 2) && !gpad.isConnected());
        numPad.setSelected(7, mouseXt > xNP + widthNP && mouseXt < xNP + (widthNP * 2) && mouseYt > yNP - (heightNP * 3)
                && mouseYt < yNP - (heightNP * 2) && !gpad.isConnected());
        numPad.setSelected(8, mouseXt > xNP + (widthNP * 2) && mouseXt < xNP + (widthNP * 3)
                && mouseYt > yNP - (heightNP * 3) && mouseYt < yNP - (heightNP * 2) && !gpad.isConnected());
        // fourth layer
        numPad.setSelected(9, mouseXt > xNP && mouseXt < xNP + widthNP && mouseYt > yNP - (heightNP * 4)
                && mouseYt < yNP - (heightNP * 3) && !gpad.isConnected());
        numPad.setSelected(10, mouseXt > xNP + widthNP && mouseXt < xNP + (widthNP * 2)
                && mouseYt > yNP - (heightNP * 4) && mouseYt < yNP - (heightNP * 3) && !gpad.isConnected());
        numPad.setSelected(11, mouseXt > xNP + (widthNP * 2) && mouseXt < xNP + (widthNP * 3)
                && mouseYt > yNP - (heightNP * 4) && mouseYt < yNP - (heightNP * 3) && !gpad.isConnected());

        // Gamepad stuff
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
        boolean canMove = (numpad_type_buffer_timestamp == -1 ||
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
                selectedField[1]).getValue();

        if (idx == 11) {
            gpad.removeElement(numPad);
            gpad.setMoveLocked(false);
            gpad.setPosition(1 + selectedField[0], 1 + selectedField[1]);
            return;
        }

        if (idx == 10) { // backspace
            sudokuFront.setNotValidInputToFalse(selectedField);
            value /= 10;
        } else if (idx == 9) { // 0
            value = value * 10;
        } else { // 1–9
            value = value * 10 + (idx + 1);
        }

        if (value > sudokuBoard.getSize()) {
            return;
        }

        sudokuBoard.changeField(selectedField[0], selectedField[1], value);
    }

    public void createSudoku() {
        if (textField.getValidity()) {
            size = Integer.parseInt(textField.getInput());
            sudokuBoard = new SudokuBoard(size);

            sudokuCreated = true;
            sudokuFront = new Sudoku(width, height, 1.3, 0, -0.1, sudokuBoard, font, fontShader, this);
            addElement(sudokuFront, 0);
            sudokuFront.openAllLocks();

            boardButtons = sudokuFront.getButtonArray();
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    gpad.addElement(boardButtons[i][j], 1 + i, 1 + j);
                }
            }

        } else if (!textField.getValidity() && sudokuCreated == true) {
            sudokuFront.clear();
            sudokuCreated = false;
        }
    }

    @Override // If you don't need a key callback, just delete this
    public void keyCallback(int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {

            //input value in fields and validate sudoku
            if (textField.getValidity() && sudokuCreated) {
              
                errorDetected = false;
                int value = sudokuBoard.getSingleField(selectedField[0], selectedField[1]).getValue();
               
                if (key >= GLFW_KEY_0 && key <= GLFW_KEY_9 && action == GLFW_PRESS) {
                    
                    value = value * 10 + (key - 48);
                    if (!(value <= size)) {
                        value = value / 10;
                        errorDetected = true;
                    }
                    updateBoardState(value);
                    System.out.println(String.valueOf(value) + " pressed!");

                }else if (key == GLFW_KEY_BACKSPACE && action == GLFW_PRESS) {
                
                    value = value / 10;
                    if (!(value <= size)) {
                        value = value / 10;
                        errorDetected = true;
                    }
                    System.out.println(value);
                    updateBoardState(value);
                   
                }
                
               
            }
            //creation of sudoku
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
        sudokuFront.resize(width, height);
        text.setXY(width, height);
        System.out.println("New size: " + width + "x" + height);
    }

    @Override // If you don't need a mouse button callback, just delete this
    public void mouseButtonCallback(int button, int action, int mods) {

        if (button == GLFW_MOUSE_BUTTON_LEFT &&
                action == GLFW_PRESS) {

            if (sudokuCreated) {
                selectedField = sudokuFront.leftClick(mouseX, mouseY);
            }

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
                }
            }

            if (returnButton.isHeldOver()) {
                new mainMenuWindow(wm, width, height);
            } else if (solveable && playButton.isHeldOver()) {
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        if (sudokuBoard.getSingleField(i, j).getValue() == 0) {
                            sudokuBoard.inputRemoved();
                        } else {
                            sudokuBoard.getSingleField(i, j).setLocked(true);
                        }
                    }
                }
                playSudokuWindow pw = new playSudokuWindow(wm, width, height, sudokuBoard, true);
            } else if (solveable && solveButton.isHeldOver()) {

                // creates a copy of the sudokuboard which we solve
                solvedSudokuBoard = new SudokuBoard(sudokuBoard.getSize());
                int[][] integerBoard = SudokuBoard.readOutOffBoard(sudokuBoard);
                solvedSudokuBoard.readIntoBoard(integerBoard);
                solvedSudokuBoard.solve();

                new SolvedWindow(wm, width, height, sudokuBoard, solvedSudokuBoard, this);
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
