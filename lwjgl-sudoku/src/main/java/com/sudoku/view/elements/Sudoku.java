package com.sudoku.view.elements;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glVertex2f;

import com.sudoku.controller.Window;
import com.sudoku.model.SudokuBoard;
import com.sudoku.view.CreateString;
import com.sudoku.view.Shader;
import com.sudoku.view.fonts.CreateFont;

public class Sudoku implements Element {

    private Window wp;
    private SudokuBoard sudokuBoard;
    private int size, bigfield, width, height;
    private CreateString text;
    private Shader fontShader;
    private FieldButton[][] buttonArray;
    private double fieldsizeX, fieldsizeY, aspect, yStart, xStart, xAspect, yAspect, sudokuSize, xOffset, yOffset;

    public Sudoku(int width, int height, double sudokuSize, double xOffset, double yOffset, SudokuBoard sb,
            CreateFont font, Shader fontShader, Window window) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.width = width;
        this.height = height;
        this.sudokuBoard = sb;
        this.fontShader = fontShader;
        this.sudokuSize = sudokuSize;
        this.wp = window;

        // creates a shader and a class that can display strings
        text = new CreateString(fontShader, font, width, height);

        size = sudokuBoard.getSize();
        this.bigfield = (int) Math.sqrt(size);
        buttonArray = new FieldButton[size][size];

        yStart = sudokuSize / 2;
        xStart = -(sudokuSize / 2);

        setAspect();

        xAspect -= xOffset;
        yAspect += yOffset;

        double y;
        double x;

        x = xAspect;
        for (int i = 0; i < size; i++) {
            y = yAspect;
            for (int j = 0; j < size; j++) {
                buttonArray[i][j] = new FieldButton(sudokuBoard.getSingleField(i, j), x, y, fieldsizeX, fieldsizeY,
                        sudokuBoard, text, fontShader);
                window.addElement(buttonArray[i][j], 0);
                y -= fieldsizeY;
            }
            x += fieldsizeX;
        }
    }

    public Sudoku(int width, int height, double sudokuSize, SudokuBoard sb, SudokuBoard unsolvedSB, CreateFont font,
            Shader fontShader, Window window) {
        this.width = width;
        this.height = height;
        this.sudokuBoard = sb;
        this.fontShader = fontShader;
        this.sudokuSize = sudokuSize;
        this.wp = window;

        // creates a shader and a class that can display strings
        text = new CreateString(fontShader, font, width, height);

        size = sudokuBoard.getSize();
        this.bigfield = (int) Math.sqrt(size);
        buttonArray = new FieldButton[size][size];

        yStart = sudokuSize / 2;
        xStart = -(sudokuSize / 2);

        setAspect();

        double y;
        double x;

        x = xAspect;
        for (int i = 0; i < size; i++) {
            y = yStart;
            for (int j = 0; j < size; j++) {
                boolean gess = unsolvedSB.getSingleField(i, j).getValue() != 0;
                buttonArray[i][j] = new FieldButton(sudokuBoard.getSingleField(i, j), x, y, fieldsizeX, fieldsizeY,
                        sudokuBoard, text, fontShader, gess, true);
                window.addElement(buttonArray[i][j], 0);
                y -= fieldsizeY;
            }
            x += fieldsizeX;
        }
    }

    public void clear(){
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                wp.removeElement(buttonArray[i][j]);
            }
        }
        wp.removeElement(this);
        
    }

    public FieldButton[][] getButtonArray() {
        return buttonArray;
    }

    public void draw() {

        fontShader.detach();
        glBegin(GL_LINES);

        float boardlenth = (float) (size * fieldsizeY);

        regularline();
        float x = (float) xAspect;
        for (int i = 0; i <= size; i++) {
            if (i % bigfield != 0) {
                glVertex2f(x, (float) yAspect);
                glVertex2f(x, (float) yAspect - boardlenth);
            }
            x += (float) fieldsizeX;
        }
        boardlenth = (float) (size * fieldsizeX);

        float y = (float) yAspect;
        for (int i = 0; i <= size; i++) {
            if (i % bigfield != 0) {
                glVertex2f((float) xAspect, y);
                glVertex2f((float) xAspect + boardlenth, y);
            }
            y -= (float) fieldsizeY;
        }

        boardlenth = (float) (size * fieldsizeY);
        bigfieldline();
        x = (float) xAspect;
        for (int i = 0; i <= size; i++) {
            if (i % bigfield == 0) {
                glVertex2f(x, (float) yAspect);
                glVertex2f(x, (float) yAspect - boardlenth);
            }
            x += (float) fieldsizeX;
        }

        boardlenth = (float) (size * fieldsizeX);
        y = (float) yAspect;
        for (int i = 0; i <= size; i++) {
            if (i % bigfield == 0) {
                glVertex2f((float) xAspect, y);
                glVertex2f((float) xAspect + boardlenth, y);
            }
            y -= (float) fieldsizeY;
        }

        glEnd();
    }

    private void bigfieldline() {
        glLineWidth(2.5f);
        glColor3d(0.20392157f, 0.27842137f, 0.38039216f);
    }

    private void regularline() {
        glLineWidth(1.5f);
        glColor3d(0.84705882f, 0.88235294f, 0.91764706f);
    }

    public void setNotValidInput(boolean isValidInput, int[] selectedField) {
        if (buttonArray[selectedField[0]][selectedField[1]].isSelected()) {
            buttonArray[selectedField[0]][selectedField[1]].setNotValid(!isValidInput);
        }

    }

    public void setNotValidInputToFalse(int[] selectedField) {
        buttonArray[selectedField[0]][selectedField[1]].setNotValid(false);
    }

    public int[] leftClick(double x, double y) {
        double[] pos = new double[2];
        double mouseXt = x / (width / 2) - 1;
        double mouseYt = -y / (height / 2) + 1;
        FieldButton fieldButton;
        int[] selectedField = new int[2];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                fieldButton = buttonArray[i][j];
                pos = fieldButton.getPos();
                if (pos[0] < mouseXt & pos[0] + fieldsizeX > mouseXt &
                        pos[1] > mouseYt & pos[1] - fieldsizeY < mouseYt) {
                    if(!sudokuBoard.getSingleField(i, j).getLocked()){
                        fieldButton.selected(true);
                        selectedField[0] = i;
                        selectedField[1] = j;
                    }
            
                    isTouching(i, j);
                } else {
                    fieldButton.selected(false);
                }
            }
        }

        //checks if player clicked outside of the board
        if(!buttonArray[selectedField[0]][selectedField[1]].isSelected()){
            resetIsTouching();
        }

        return selectedField;
    }

    private void resetIsTouching(){
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                buttonArray[i][j].setTouching(false);
            }
        }
    }

    public void openAllLocks(){
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                sudokuBoard.getSingleField(i, j).setLocked(false);;
            }
        }
    }


    private void isTouching(int x, int y) {
        System.out.println(x + "x  " + y + " y" + " bool " + buttonArray[x][y].isSelected());
        int xDiv = x / bigfield;
        int yDiv = y / bigfield;
        for (int i = 0; i < size; i++) {
            int iDiv = i / bigfield;
            for (int j = 0; j < size; j++) {
                if (!sudokuBoard.getSingleField(x, y).getLocked() && buttonArray[x][y].isSelected() && (i == x || j == y || (xDiv == iDiv && yDiv == j / bigfield))) {
                    buttonArray[i][j].setTouching(true);
                } else {
                    buttonArray[i][j].setTouching(false);
                }
            }
        }

    }

    public void setMatchingNumber(int x, int y, int number) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (number > 0 && number == buttonArray[i][j].getnumber()) {
                    buttonArray[i][j].setMatchingNumber(true);
                } else {
                    buttonArray[i][j].setMatchingNumber(true);
                }
            }
        }
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        System.out.println("New size: " + width + "x" + height);

        setAspect();

        xAspect -= xOffset;
        yAspect += yOffset;

        double y;
        double x;

        x = xAspect;
        for (int i = 0; i < size; i++) {
            y = yAspect;
            for (int j = 0; j < size; j++) {
                double[] xy = { x, y };
                buttonArray[i][j].setXY(xy);
                xy[0] = fieldsizeX;
                xy[1] = fieldsizeY;
                buttonArray[i][j].setFieldSize(xy);
                y -= fieldsizeY;
                buttonArray[i][j].posField();
            }
            x += fieldsizeX;
        }
        text.setXY(width, height);

    }

    private void setAspect() {
        if (height <= width) {
            aspect = (double) height / (double) width;
            xAspect = xStart * aspect;
            yAspect = yStart;

            fieldsizeY = sudokuSize / size;
            fieldsizeX = fieldsizeY * aspect;

        } else if (height > width) {
            aspect = (double) width / (double) height;
            yAspect = yStart * aspect;
            xAspect = xStart;

            fieldsizeX = sudokuSize / size;
            fieldsizeY = fieldsizeX * aspect;
        }
    }
}
