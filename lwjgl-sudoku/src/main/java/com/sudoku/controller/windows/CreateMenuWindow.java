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
import com.sudoku.model.SudokuBoard;
import com.sudoku.view.CreateString;
import com.sudoku.view.Shader;
import com.sudoku.view.elements.FieldButton;
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
    // x, y, scale, width, hight
    private float[] textFieldPrime = new float[] { -0.6f, 0.7f, 0.3f, 0.0f, 0.1f };

    private SudokuBoard sudokuBoard;
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

    public CreateMenuWindow(WindowManager wm, int width, int height) {
        super(wm);
        this.wm = wm;
        wm.setActiveWindow(this);
        this.width = width;
        this.height = height;
    }

    public void create() {
        // This code runs once
        font = wm.getFont();
        // creates a shader and a class that can display strings
        fontShader = wm.getFontShader();
        text = new CreateString(fontShader, font);

        // creates a String and a box, with xyPos, color, width, hight
        textField = new TextFieldButton(text, fontShader, output, textFieldPrime[0], textFieldPrime[1],
                textFieldPrime[2], new float[] { 1.0f, 0.0f, 0.0f }, textFieldPrime[3], textFieldPrime[4]);
        textInfo = new CreateString(fontShader, font);

        float aspect = 1280f / 720f;
        numPad = new NumPadButton(0.65f, 0.6f, 0.1f, 0.1f * aspect, text, fontShader);

    }

    public void step() {
        // This code runs every frame
        double mouseXt = mouseX / (1280 / 2) - 1;
        double mouseYt = -mouseY / (720 / 2) + 1;

        textFieldHover(mouseXt, mouseYt);
        numPadHover(mouseXt, mouseYt);

        textInfo.makeText("You Can Costemize A 4x4, 9x9, 16x16, 25x25", (textFieldPrime[0] - 0.005f),
                (textFieldPrime[1] - 0.06f), 0.2f, new float[] { 1.0f, 0.0f, 0.0f });
        textInfo.flush();
        textField.draw();
        numPad.draw();

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

    private void numPadHover(double mouseXt, double mouseYt) {
        float xNP = numPad.getX();
        float yNP = numPad.getY();
        float widthNP = numPad.getWidth();
        float heightNP = numPad.getHeight();

        // first layer
        numPad.setSelected(0, mouseXt > xNP && mouseXt < xNP + widthNP && mouseYt > yNP - heightNP && mouseYt < yNP);
        numPad.setSelected(1,
                mouseXt > xNP + widthNP && mouseXt < xNP + (widthNP * 2) && mouseYt > yNP - heightNP && mouseYt < yNP);
        numPad.setSelected(2, mouseXt > xNP + (widthNP * 2) && mouseXt < xNP + (widthNP * 3) && mouseYt > yNP - heightNP
                && mouseYt < yNP);
        // second layer
        numPad.setSelected(3,
                mouseXt > xNP && mouseXt < xNP + widthNP && mouseYt > yNP - (heightNP * 2) && mouseYt < yNP - heightNP);
        numPad.setSelected(4, mouseXt > xNP + widthNP && mouseXt < xNP + (widthNP * 2) && mouseYt > yNP - (heightNP * 2)
                && mouseYt < yNP - heightNP);
        numPad.setSelected(5, mouseXt > xNP + (widthNP * 2) && mouseXt < xNP + (widthNP * 3)
                && mouseYt > yNP - (heightNP * 2) && mouseYt < yNP - heightNP);
        // third layer
        numPad.setSelected(6, mouseXt > xNP && mouseXt < xNP + widthNP && mouseYt > yNP - (heightNP * 3)
                && mouseYt < yNP - (heightNP * 2));
        numPad.setSelected(7, mouseXt > xNP + widthNP && mouseXt < xNP + (widthNP * 2) && mouseYt > yNP - (heightNP * 3)
                && mouseYt < yNP - (heightNP * 2));
        numPad.setSelected(8, mouseXt > xNP + (widthNP * 2) && mouseXt < xNP + (widthNP * 3)
                && mouseYt > yNP - (heightNP * 3) && mouseYt < yNP - (heightNP * 2));

    }

    @Override // If you don't need a key callback, just delete this
    public void keyCallback(int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {

            if (textField.getValidity() && sudokuCreated) {
                // sudokuBoard
                if (key == GLFW_KEY_1 && action == GLFW_PRESS) {
                    sudokuBoard.changeField(selectedField[0], selectedField[1], 1);
                    System.out.println("1 pressed!");
                } else if (key == GLFW_KEY_2 && action == GLFW_PRESS) {
                    sudokuBoard.changeField(selectedField[0], selectedField[1], 2);
                    System.out.println("2 pressed!");
                } else if (key == GLFW_KEY_3 && action == GLFW_PRESS) {
                    sudokuBoard.changeField(selectedField[0], selectedField[1], 3);
                    System.out.println("3 pressed!");
                } else if (key == GLFW_KEY_4 && action == GLFW_PRESS) {
                    sudokuBoard.changeField(selectedField[0], selectedField[1], 4);
                    System.out.println("4 pressed!");
                } else if (key == GLFW_KEY_5 && action == GLFW_PRESS) {
                    sudokuBoard.changeField(selectedField[0], selectedField[1], 5);
                    System.out.println("5 pressed!");
                } else if (key == GLFW_KEY_6 && action == GLFW_PRESS) {
                    sudokuBoard.changeField(selectedField[0], selectedField[1], 6);
                    System.out.println("6 pressed!");
                } else if (key == GLFW_KEY_7 && action == GLFW_PRESS) {
                    sudokuBoard.changeField(selectedField[0], selectedField[1], 7);
                    System.out.println("7 pressed!");
                } else if (key == GLFW_KEY_8 && action == GLFW_PRESS) {
                    sudokuBoard.changeField(selectedField[0], selectedField[1], 8);
                    System.out.println("8 pressed!");
                } else if (key == GLFW_KEY_9 && action == GLFW_PRESS) {
                    sudokuBoard.changeField(selectedField[0], selectedField[1], 9);
                    System.out.println("9 pressed!");
                }
            }

            if (textField.isSelected()) {
                if (key >= GLFW_KEY_0 && key <= GLFW_KEY_9) {
                    char c = (char) ('0' + (key - GLFW_KEY_0));
                    textField.updateInput(c);
                } else if (key == GLFW_KEY_BACKSPACE) {
                    textField.updateInput();
                }
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
                            buttonArray[i][j] = new FieldButton(sudokuBoard.getSingleField(i, j), x, y, fieldsize,
                                    sudokuBoard, text, fontShader);
                            addElement(buttonArray[i][j], 0);
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
                            buttonArray[i][j] = new FieldButton(sudokuBoard.getSingleField(i, j), x, y, fieldsize,
                                    sudokuBoard, text, fontShader);
                            addElement(buttonArray[i][j], 0);
                            y -= fieldsize;
                        }
                        x += fieldsize;
                    }
                    sudokuCreated = false;
                }

            }

        }

    }

    @Override // If you don't need a resize callback, just delete this
    public void resizeCallback(int width, int height) {
        this.width = width;
        this.height = height;
        System.out.println("New size: " + width + "x" + height);
    }

    @Override // If you don't need a mouse button callback, just delete this
    public void mouseButtonCallback(int button, int action, int mods) {

        if (button == GLFW_MOUSE_BUTTON_LEFT &&
                action == GLFW_PRESS) {
            if (textField.isHeldOver()) {
                textField.setSelected(true);
            } else {
                textField.setSelected(false);
            }

            if (numPad.isSelected()[numPad.getIndexSelec()]) {
                System.out.println(numPad.getIndexSelec() + 1 + " is pressed");
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
                        if (pos[0] < mouseXt & pos[0] + fieldsize > mouseXt &
                                pos[1] > mouseYt & pos[1] - fieldsize < mouseYt) {
                            fieldButton.selected(true);
                            selectedField[0] = i;
                            selectedField[1] = j;
                        } else {
                            fieldButton.selected(false);
                        }
                    }
                }
            }

        }

        // if (button == GLFW_MOUSE_BUTTON_RIGHT&&
        // action == GLFW_PRESS) {

        // System.out.println("Right click!");
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
