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
import com.sudoku.view.fonts.CreateFont;

public class playSudokuWindow extends Window implements WindowInterface {

    private WindowManager wm;
    private SudokuBoard sudokuBoard;
    private SudokuBoard solvedSudokuBoard;
    private int width, height;
    private double mouseX, mouseY;
    private CreateFont font;
    private CreateString text;
    private Shader fontShader;
    private int[] selectedField = new int[2];
    private Sudoku sudokuFront;
    private MenuButton returnButton;
    private Gamepad gpad;
    private NumPadButton numPad;
    private FieldButton[][] boardButtons;
    private int size;

    private MenuButton solveButton;
    private MenuButton hintButton;

    private int gpadNumpadIndex = 0;
    private long numpad_buffer = 167;
    private long numpad_buffer_timestamp = System.currentTimeMillis();
    private boolean gpad_selected_before = true;
    private boolean wasOnBoardLastFrame = false;

    private boolean classIsCreated = false;

    // private EndScreenWindow = endScreen;

    public playSudokuWindow(WindowManager wm, int width, int height, SudokuBoard sb) {
        super(wm);
        this.wm = wm;
        this.width = width;
        this.height = height;
        sudokuBoard = sb;
        sudokuBoard.populate();
        wm.setActiveWindow(this);
        classIsCreated = true;
    }

    public void create() {
        // This code runs once
        if (!classIsCreated) {
            gpad = new Gamepad();
            font = wm.getFont();
            // creates a shader and a class that can display strings
            fontShader = wm.getFontShader();
            text = new CreateString(fontShader, font, width, height);

            sudokuFront = new Sudoku(width, height, 1.6, 0, 0, sudokuBoard, font, fontShader, this);
            addElement(sudokuFront, 0);
            size = sudokuBoard.getSize();

            // return to last window
            returnButton = new MenuButton(-0.7, 0.75, 0.2, text, fontShader, "Back");
            addElement(returnButton, 0);
            gpad.addElement(returnButton, 0, 0);

            solveButton = new MenuButton(0.7, 0.75, 0.2, text, fontShader, "Solve");
            addElement(solveButton, 0);
            gpad.addElement(solveButton, size + 2, 1);

            hintButton = new MenuButton(0.7, 0.50, 0.2, text, fontShader, "Hint");
            addElement(hintButton, 0);
            gpad.addElement(hintButton, size + 2, 2);

            // numpad
            float aspect = 1280f / 720f;
            numPad = new NumPadButton(0.525f, 0.2f, 0.1f, 0.1f * aspect, text, fontShader);
            addElement(numPad, 0);

            boardButtons = sudokuFront.getButtonArray();
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    gpad.addElement(boardButtons[i][j], 1 + i, 1 + j);
                }
            }

            // creates a copy of the sudokuboard which we solve
            solvedSudokuBoard = new SudokuBoard(sudokuBoard.getSize());
            int[][] integerBoard = SudokuBoard.readOutOffBoard(sudokuBoard);
            solvedSudokuBoard.readIntoBoard(integerBoard);
            solvedSudokuBoard.solve();

        }

    }

    public void step() {
        // This code runs every frame
        gpad.step();
        holdOver(returnButton);
        holdOver(solveButton);
        holdOver(hintButton);

        numPadHover();

        if (gpad.isConnected()) {
            boolean onMenu = gpad.isSelected(returnButton)
                    || gpad.isSelected(solveButton)
                    || gpad.isSelected(hintButton)
                    || gpad.isSelected(numPad);

            boolean onBoard = false;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (gpad.isSelected(gpad.getElementAt(1 + i, 1 + j))) {
                        boardButtons[i][j].selected(true);
                        selectedField[0] = i;
                        selectedField[1] = j;
                        onBoard = true;
                    } else {
                        boardButtons[i][j].selected(false);
                    }
                }
            }

            boolean entered = gpad.isEntered();

            if (onBoard && !onMenu && !gpad.isSelected(numPad) && entered && wasOnBoardLastFrame) {
                gpadNumpadIndex = 0;
                gpad.addElement(numPad, 37, 0);
                gpad.setPosition(37, 0);
            }

            if (gpad.isSelected(numPad) && entered) {
                typeBoard(gpadNumpadIndex);
            }

            wasOnBoardLastFrame = onBoard && !gpad.isSelected(numPad);

            windowTransition(returnButton, false);
            windowTransition(solveButton, false);
            windowTransition(hintButton, false);
        } else {
            wasOnBoardLastFrame = false;
        }
    }

    // inout a MenuButton and it will track if the mouse if hovering the button
    private void holdOver(MenuButton button) {
        double mouseXt = mouseX / (width / 2) - 1;
        double mouseYt = -mouseY / (height / 2) + 1;
        if ((button.getPos()[0] - returnButton.getSize() / 2 < mouseXt &
                button.getPos()[0] + returnButton.getSize() / 2 > mouseXt &

                button.getPos()[1] - button.getSize() / 2 < mouseYt &
                button.getPos()[1] + button.getSize() / 2 > mouseYt) || gpad.isSelected(button)) {
            button.heldOver(true);
        } else {
            button.heldOver(false);
        }
    }

    private void numPadHover() {
        float xNP = numPad.getX();
        float yNP = numPad.getY();
        float widthNP = numPad.getWidth();
        float heightNP = numPad.getHeight();

        double mouseXt = mouseX / (width / 2) - 1;
        double mouseYt = -mouseY / (height / 2) + 1;

        numPad.setSelected(0, mouseXt > xNP && mouseXt < xNP + widthNP && mouseYt > yNP - heightNP && mouseYt < yNP
                && !gpad.isConnected());
        numPad.setSelected(1, mouseXt > xNP + widthNP && mouseXt < xNP + widthNP * 2 && mouseYt > yNP - heightNP
                && mouseYt < yNP && !gpad.isConnected());
        numPad.setSelected(2, mouseXt > xNP + widthNP * 2 && mouseXt < xNP + widthNP * 3 && mouseYt > yNP - heightNP
                && mouseYt < yNP && !gpad.isConnected());
        numPad.setSelected(3, mouseXt > xNP && mouseXt < xNP + widthNP && mouseYt > yNP - heightNP * 2
                && mouseYt < yNP - heightNP && !gpad.isConnected());
        numPad.setSelected(4, mouseXt > xNP + widthNP && mouseXt < xNP + widthNP * 2 && mouseYt > yNP - heightNP * 2
                && mouseYt < yNP - heightNP && !gpad.isConnected());
        numPad.setSelected(5, mouseXt > xNP + widthNP * 2 && mouseXt < xNP + widthNP * 3 && mouseYt > yNP - heightNP * 2
                && mouseYt < yNP - heightNP && !gpad.isConnected());
        numPad.setSelected(6, mouseXt > xNP && mouseXt < xNP + widthNP && mouseYt > yNP - heightNP * 3
                && mouseYt < yNP - heightNP * 2 && !gpad.isConnected());
        numPad.setSelected(7, mouseXt > xNP + widthNP && mouseXt < xNP + widthNP * 2 && mouseYt > yNP - heightNP * 3
                && mouseYt < yNP - heightNP * 2 && !gpad.isConnected());
        numPad.setSelected(8, mouseXt > xNP + widthNP * 2 && mouseXt < xNP + widthNP * 3 && mouseYt > yNP - heightNP * 3
                && mouseYt < yNP - heightNP * 2 && !gpad.isConnected());
        numPad.setSelected(9, mouseXt > xNP && mouseXt < xNP + widthNP && mouseYt > yNP - heightNP * 4
                && mouseYt < yNP - heightNP * 3 && !gpad.isConnected());
        numPad.setSelected(10, mouseXt > xNP + widthNP && mouseXt < xNP + widthNP * 2 && mouseYt > yNP - heightNP * 4
                && mouseYt < yNP - heightNP * 3 && !gpad.isConnected());
        numPad.setSelected(11, mouseXt > xNP + widthNP * 2 && mouseXt < xNP + widthNP * 3
                && mouseYt > yNP - heightNP * 4 && mouseYt < yNP - heightNP * 3 && !gpad.isConnected());

        // Gamepad D-pad navigation inside numpad
        if (gpad_selected_before && gpad.isSelected(numPad)) {
            numpad_buffer_timestamp = System.currentTimeMillis();
            gpad_selected_before = false;
        }

        long now = System.currentTimeMillis();
        boolean canMove = (numpad_buffer_timestamp == -1
                || now - numpad_buffer_timestamp >= numpad_buffer);

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
        } else {
            gpad_selected_before = true;
        }
    }

    private void typeBoard(int idx) {
        int value = sudokuBoard.getSingleField(
                selectedField[0],
                selectedField[1]).getValue();

        if (idx == 11) { // Enter — close numpad, return cursor to the cell
            gpad.removeElement(numPad);
            gpad.setMoveLocked(false);
            // wasOnBoardLastFrame stays false for one frame so the return
            // A press doesn't immediately re-open the numpad
            wasOnBoardLastFrame = false;
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
        validateInput(selectedField);
    }

    @Override // If you don't need a key callback, just delete this
    public void keyCallback(int key, int scancode, int action, int mods) {

        int value = sudokuBoard.getSingleField(selectedField[0], selectedField[1]).getValue();

        if (value > 0 && key == GLFW_KEY_0 && action == GLFW_PRESS) {
            value = value * 10 + 0;
            sudokuBoard.changeField(selectedField[0], selectedField[1], value);
            System.out.println("0 pressed!");
        } else if (key == GLFW_KEY_1 && action == GLFW_PRESS) {
            value = value * 10 + 1;
            sudokuBoard.changeField(selectedField[0], selectedField[1], value);
            System.out.println("1 pressed!");
        } else if (key == GLFW_KEY_2 && action == GLFW_PRESS) {
            value = value * 10 + 2;
            sudokuBoard.changeField(selectedField[0], selectedField[1], value);
            System.out.println("2 pressed!");
        } else if (key == GLFW_KEY_3 && action == GLFW_PRESS) {
            value = value * 10 + 3;
            sudokuBoard.changeField(selectedField[0], selectedField[1], value);
            System.out.println("3 pressed!");
        } else if (key == GLFW_KEY_4 && action == GLFW_PRESS) {
            value = value * 10 + 4;
            sudokuBoard.changeField(selectedField[0], selectedField[1], value);
            System.out.println("4 pressed!");
        } else if (key == GLFW_KEY_5 && action == GLFW_PRESS) {
            value = value * 10 + 5;
            sudokuBoard.changeField(selectedField[0], selectedField[1], value);
            System.out.println("5 pressed!");
        } else if (key == GLFW_KEY_6 && action == GLFW_PRESS) {
            value = value * 10 + 6;
            sudokuBoard.changeField(selectedField[0], selectedField[1], value);
            System.out.println("6 pressed!");
        } else if (key == GLFW_KEY_7 && action == GLFW_PRESS) {
            value = value * 10 + 7;
            sudokuBoard.changeField(selectedField[0], selectedField[1], value);
            System.out.println("7 pressed!");
        } else if (key == GLFW_KEY_8 && action == GLFW_PRESS) {
            value = value * 10 + 8;
            sudokuBoard.changeField(selectedField[0], selectedField[1], value);
            System.out.println("8 pressed!");
        } else if (key == GLFW_KEY_9 && action == GLFW_PRESS) {
            value = value * 10 + 9;
            sudokuBoard.changeField(selectedField[0], selectedField[1], value);
            System.out.println("9 pressed!");
        }
        if (key == GLFW_KEY_BACKSPACE && action == GLFW_PRESS) {
            sudokuFront.setNotValidInputToFalse(selectedField);
            value = value / 10;
            sudokuBoard.changeField(selectedField[0], selectedField[1], value);
        }
        if (!(value <= sudokuBoard.getSize())) {
            value = value / 10;
            sudokuBoard.changeField(selectedField[0], selectedField[1], value);
        }

        // if (key == GLFW_KEY_1 && action == GLFW_PRESS){
        // sudokuBoard.changeField(selectedField[0],selectedField[1],1);
        // System.out.println("1 pressed!");
        // }else if (key == GLFW_KEY_2 && action == GLFW_PRESS){
        // sudokuBoard.changeField(selectedField[0],selectedField[1],2);
        // System.out.println("2 pressed!");
        // }else if (key == GLFW_KEY_3 && action == GLFW_PRESS){
        // sudokuBoard.changeField(selectedField[0],selectedField[1],3);
        // System.out.println("3 pressed!");
        // }else if (key == GLFW_KEY_4 && action == GLFW_PRESS){
        // sudokuBoard.changeField(selectedField[0],selectedField[1],4);
        // System.out.println("4 pressed!");
        // }else if (key == GLFW_KEY_5 && action == GLFW_PRESS){
        // sudokuBoard.changeField(selectedField[0],selectedField[1],5);
        // System.out.println("5 pressed!");
        // }else if (key == GLFW_KEY_6 && action == GLFW_PRESS){
        // sudokuBoard.changeField(selectedField[0],selectedField[1],6);
        // System.out.println("6 pressed!");
        // }else if (key == GLFW_KEY_7 && action == GLFW_PRESS){
        // sudokuBoard.changeField(selectedField[0],selectedField[1],7);
        // System.out.println("7 pressed!");
        // }else if (key == GLFW_KEY_8 && action == GLFW_PRESS){
        // sudokuBoard.changeField(selectedField[0],selectedField[1],8);
        // System.out.println("8 pressed!");
        // }else if (key == GLFW_KEY_9 && action == GLFW_PRESS){
        // sudokuBoard.changeField(selectedField[0],selectedField[1],9);
        // System.out.println("9 pressed!");
        // }

        // if (key == GLFW_KEY_SPACE && action == GLFW_PRESS) {
        // System.out.println("Space pressed!");

        // }
    }

    public void validateInput(int[] selectedField) {
        sudokuFront.setNotValidInput(
                sudokuBoard.getSingleField(selectedField[0], selectedField[1]).getValue() != solvedSudokuBoard
                        .getSingleField(selectedField[0], selectedField[1]).getValue(),
                selectedField);
    }

    @Override // If you don't need a resize callback, just delete this
    public void resizeCallback(int width, int height) {
        this.width = width;
        this.height = height;
        sudokuFront.resize(width, height);
        text.setXY(width, height);
    }

    @Override // If you don't need a mouse button callback, just delete this
    public void mouseButtonCallback(int button, int action, int mods) {

        if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
            validateInput(selectedField);

            selectedField = sudokuFront.leftClick(mouseX, mouseY);
            windowTransition(returnButton, true);
            windowTransition(solveButton, true);
            windowTransition(hintButton, true);
        }

        if (!gpad.isConnected()) {
            int idx = numPad.getIndexSelec();
            if (numPad.isSelected()[idx]) {
                typeBoard(idx);
            }
        }
    }

    public void windowTransition(MenuButton b, boolean mouseClick) {
        if (elementExists(b)) {
            if (mouseClick || gpad.isEntered()) {
                if (b.isHeldOver()) {
                    if (b == returnButton) {
                        new PlaySudokuSettingsWindow(wm, width, height);
                    } else if (b == solveButton) {
                        new SolvedWindow(wm, width, height, sudokuBoard, solvedSudokuBoard, this);
                    } else if (b == hintButton) {
                        new EndScreenWindow(wm, sudokuBoard, width, height, "win");
                    }
                }
            }
        }
    }

    @Override
    public void cursorPosCallback(double x, double y) {
        this.mouseX = x;
        this.mouseY = y;
    }

}
