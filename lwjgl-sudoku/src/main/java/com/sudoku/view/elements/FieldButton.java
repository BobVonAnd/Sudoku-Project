package com.sudoku.view.elements;

import java.util.ArrayList;

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

import com.sudoku.model.Field;
import com.sudoku.model.SudokuBoard;
import com.sudoku.view.CreateString;
import com.sudoku.view.Shader;

public class FieldButton implements Element {
    private Field field;
    private SudokuBoard sb;
    private int value;
    private double x, y, sizeX, sizeY;
    private boolean selected;
    private double[] colour;
    private CreateString text;
    private Shader fontShader;
    private boolean touching;
    private boolean snumber;

    private boolean notValid = false;
    private boolean error = false;
    private boolean userGess = false;
    private boolean isSolved = false;

    private ArrayList<Float[]> notePos = new ArrayList<>();
    private float xOffset = 0.14f;
    private float yOffset = 0.18f;
    private float xIncrement = 0.27f;
    private float yIncrement = -0.24f;
    private boolean disable = false;

    public FieldButton(Field f, double x, double y, double sizeX, double sizeY, SudokuBoard sudokuBoard,
            CreateString text, Shader fontShader) {
        this.field = f;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.x = x;
        this.y = y;
        colour = new double[] { 1.0f, 0f, 0f };
        this.text = text;
        this.fontShader = fontShader;
        this.sb = sudokuBoard;

        posField();
    }

    // this is used for solved sudoku and it changes the color
    // where there was an input in the finel solved sudoku
    public FieldButton(Field f, double x, double y, double sizeX, double sizeY, SudokuBoard sudokuBoard,
            CreateString text, Shader fontShader, boolean isgess, boolean isSolved) {
        this.field = f;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.x = x;
        this.y = y;
        colour = new double[] { 1.0f, 0f, 0f };
        this.text = text;
        this.fontShader = fontShader;
        this.userGess = isgess;
        this.isSolved = isSolved;
        this.sb = sudokuBoard;

        posField();
    }

    public void posField() {

        float newX = (float) x + xOffset * (float) sizeX;
        float newY = (float) y - yOffset * (float) sizeY;

        for (int i = 0; i < sb.getSize(); i++) {
            newX += xIncrement * sizeX;
            if (i % Math.sqrt(sb.getSize()) == 0) {

                newX = (float) x + xOffset * (float) sizeX;
                newY += yIncrement * sizeY;
            }
            notePos.add(new Float[] { newX, newY });
        }
    }

    public void setDisable(boolean isDisable){
        disable = isDisable;
    }

    public boolean getDisable(){
        return disable;
    }

    public ArrayList<Float[]> getNotePos() {
        return notePos;
    }

    public void setError(boolean isError) {
        error = isError;
    }

    public void setNotValid(boolean notValid) {
        this.notValid = notValid;
    }

    public void setUserGess(boolean isGess) {
        userGess = isGess;
    }

    public void draw() {

        fontShader.detach();
        glBegin(GL_QUADS);
        setColour();

        glColor3d(colour[0], colour[1], colour[2]);
        glVertex2d(x, y - sizeY); // Bottom left
        glVertex2d(x + sizeX, y - sizeY); // Bottom right
        glVertex2d(x + sizeX, y); // Top Right
        glVertex2d(x, y); // Top Left
        glEnd();
        value = field.getValue();
        if (value != 0) {
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

            if (isSolved) {
                if (userGess) {
                    text.makeText("" + value, (float) (x + sizeX / 3.9), (float) (y - sizeY / 0.895),
                            (float) (4.5 * sizeY), new float[] { 0f, 0f, 0f });
                } else {
                    text.makeText("" + value, (float) (x + sizeX / 3.9), (float) (y - sizeY / 0.895),
                            (float) (4.5 * sizeY), new float[] { 0.0471f, 0.6588f, 0.9529f });
                }
            } else if (value < 10) {
                if (error) {
                    text.makeText("" + value, (float) (x + sizeX / 3.9), (float) (y - sizeY / 0.895),
                            (float) (4.5 * sizeY), new float[] { 0f, 0f, 1f });
                } else if (notValid) {
                    text.makeText("" + value, (float) (x + sizeX / 3.9), (float) (y - sizeY / 0.895),
                            (float) (4.5 * sizeY), new float[] { 1f, 0f, 0f });
                } else {
                    text.makeText("" + value, (float) (x + sizeX / 3.9), (float) (y - sizeY / 0.895),
                            (float) (4.5 * sizeY), new float[] { 0.203921569f, 0.278431373f, 0.380392157f });
                }

            } else {
                if (error) {
                    text.makeText("" + value, (float) (x), (float) (y - sizeY / 0.895), (float) (4.5 * sizeY),
                            new float[] { 0f, 0f, 1f });
                } else if (notValid) {
                    text.makeText("" + value, (float) (x), (float) (y - sizeY / 0.895), (float) (4.5 * sizeY),
                            new float[] { 1f, 0f, 0f });
                } else {
                    text.makeText("" + value, (float) (x), (float) (y - sizeY / 0.895), (float) (4.5 * sizeY),
                            new float[] { 0.203921569f, 0.278431373f, 0.380392157f });
                }

            }

        }
        if (!field.getLocked() && !disable && sb.getSize() <= 9) {
            for (int i = 0; i < sb.getSize(); i++) {
                if (field.getNote()[i]) {
                    text.makeText(i + 1 + "", notePos.get(i)[0], notePos.get(i)[1], 2f * (float) sizeX,
                            new float[] { 0.0f, 0.6588f, 1.0f });

                }
            }

        }
        text.flush();
    }

    public double[] getPos() {
        return new double[] { this.x, this.y };
    }

    public double[] getSize() {
        double[] size = { sizeX, sizeY };
        return size;
    }

    public void selected(boolean isIt) {
        this.selected = isIt;
    }

    private void setColour() {
        if (selected) {
            selectedcolour();
        } else if (snumber) {
            samenumber();
        } else if (touching) {
            fieldIsTouching();
        } else {
            whiteColour();
        }
    }

    private void selectedcolour() {
        colour[0] = 0.733333333f;
        colour[1] = 0.870588235f;
        colour[2] = 0.984313725f;
    }

    private void samenumber() {
        colour[0] = 0.764705882f;
        colour[1] = 0.843137255f;
        colour[2] = 0.917647059f;
    }

    private void fieldIsTouching() {
        colour[0] = 0.882352941f;
        colour[1] = 0.921568627f;
        colour[2] = 0.952941176f;
    }

    private void whiteColour() {
        colour[0] = 1.0f;
        colour[1] = 1.0f;
        colour[2] = 1.0f;
    }

    public boolean isSelected() {
        return selected;
    }

    public Field getField() {
        return field;
    }

    public void setXY(double[] xy) {
        this.x = xy[0];
        this.y = xy[1];
    }

    public void setFieldSize(double[] xy) {
        this.sizeX = xy[0];
        this.sizeY = xy[1];
    }

    public void setTouching(boolean touching){
        this.touching = touching;
    }

    public int getnumber(){
        return field.getValue();
    }

    public void setMatchingNumber(boolean snumber){
        this.snumber = snumber;
    }
}